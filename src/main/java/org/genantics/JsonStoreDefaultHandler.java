/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.genantics;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.TimeZone;
import org.codehaus.jackson.JsonNode;

/**
 *
 * @author Bob Foster
 */
public class JsonStoreDefaultHandler extends JsonStoreAPI {
    
    List<JsonStored> list = new ArrayList<JsonStored>();
    
    static JsonStoreDefaultHandler instance = new JsonStoreDefaultHandler();
    
    public static JsonStoreAPI getInstance() {
        return instance;
    }
    
    private JsonStoreDefaultHandler() {
    }

    /**
     * This implementation saves both the string and Node form of JSON.
     * Obviously, the latter can be reconstructed from the former
     * and any real implementation would be expected to do so.
     */
    private static class JsonStoredDefault implements JsonStored {

        private String jsonString;
        private JsonNode jsonNode;
        private String timestamp;
        
        public JsonStoredDefault(String jsonString, JsonNode json) {
            this.jsonString = jsonString;
            this.jsonNode = json;
            TimeZone tz = TimeZone.getTimeZone("UTC");
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
            df.setTimeZone(tz);
            timestamp = df.format(new Date());
        }
        public String getTimeStamp() {
            return timestamp;
        }

        public String getJsonString() {
            return jsonString;
        }

        public JsonNode getJson() {
            return jsonNode;
        }
        
    }
    
    public synchronized void save(String jsonString, JsonNode json) {
        list.add(new JsonStoredDefault(jsonString, json));
    }

    public synchronized Iterator<JsonStored> iterator() {
        return new ArrayList<JsonStored>(list).iterator();
    }
}
