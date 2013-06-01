package org.genantics;

import org.genantics.json.query.JsonQuery;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import java.io.InputStream;
import java.net.URI;
import java.util.Scanner;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.ObjectMapper;

/**
 * Real simple JSON server.
 * 
 * @author Bob Foster
 */
public class SimpleJsonServer {

    public static void main(String[] args) throws IOException {
        int port = args.length > 1 ? Integer.parseInt(args[1]) : 0;
        if (args.length != 2 || !"-port".equals(args[0]) || port < 1 || port > 0xFFFF) {
            System.err.println("Usage: java -jar target/json-server.jar -port NNNN");
            System.err.println("       Where NNNN is a valid http port");
            System.exit(1);
        }
        startServer(port);
    }

    static HttpServer startServer(int port) throws IOException {
        InetSocketAddress addr = new InetSocketAddress(port);
        HttpServer server = HttpServer.create(addr, 0);
        server.createContext("/", new JsonHandler());
        server.setExecutor(Executors.newCachedThreadPool());
        server.start();
        System.out.println("JSON server listening on port " + port);
        return server;
    }

    private static class JsonHandler implements HttpHandler {

        public void handle(HttpExchange exchange) throws IOException {
            String requestMethod = exchange.getRequestMethod();
            if (requestMethod.equalsIgnoreCase("GET")) {
                Headers responseHeaders = exchange.getResponseHeaders();
                responseHeaders.set("Content-Type", "text/plain");
                exchange.sendResponseHeaders(200, 0);
                OutputStream responseBody = exchange.getResponseBody();
                URI uri = exchange.getRequestURI();
                String query = uri.getQuery();
                if (query == null) {
                    responseBody.write("JSON Server".getBytes());
                } else {
                    responseBody.write(new JsonQuery().eval(query).getBytes("UTF-8"));
                }
                responseBody.close();
            } else if (requestMethod.equalsIgnoreCase("POST")) {
                InputStream in = exchange.getRequestBody();
                Scanner scanner = new java.util.Scanner(in).useDelimiter("\\A");
                String s = scanner.hasNext() ? scanner.next() : "Error reading request body";
                System.out.println(s);
                Headers responseHeaders = exchange.getResponseHeaders();
                responseHeaders.set("Content-Type", "text/plain");
                ObjectMapper mapper = new ObjectMapper();
                OutputStream responseBody = exchange.getResponseBody();
                try {
                    // Very simple test to see if the contents are JSON
                    JsonNode json = mapper.readTree(s);
                    exchange.sendResponseHeaders(201, 0);
                    JsonStore store = new JsonStore();
                    store.save(s, json);
                } catch (JsonProcessingException e) {
                    exchange.sendResponseHeaders(400, 0);
                }
                responseBody.close();
            }
        }
    }
}
