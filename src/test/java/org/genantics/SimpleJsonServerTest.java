package org.genantics;

import com.sun.net.httpserver.HttpServer;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.net.HttpURLConnection;
import java.net.ServerSocket;
import java.net.URL;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Unit test for SimpleJsonServer.
 */
public class SimpleJsonServerTest 
    extends TestCase
{
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public SimpleJsonServerTest( String testName )
    {
        super( testName );
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite()
    {
        return new TestSuite( SimpleJsonServerTest.class );
    }
    
    /**
     * Rigourous Test :-)
     */
    public void testServer() throws IOException
    {
        ServerSocket socket = null;
        for (int i = 49152; i <= 65535; i++) {
          try {
            socket = new ServerSocket(i);
            break;
          } catch (IOException e) {
            // keep trying
          }
        }
        assertNotNull(socket);
        int port = socket.getLocalPort();
        socket.close();
        
        HttpServer server = SimpleJsonServer.startServer(port);
        assertNotNull(server);
        
        URL url = new URL("http://localhost:"+port);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        Reader in = new InputStreamReader(connection.getInputStream());
        StringWriter out = new StringWriter();
        int c = -1;
        while ((c = in.read()) >= 0) {
          out.write(c);
        }
        in.close();
        out.close();
        connection.disconnect();
        assertEquals("JSON Server", out.toString());
        
        // TODO test POST
        
        server.stop(0);
    }
}
