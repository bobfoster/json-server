/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.genantics;

import java.util.Iterator;
import org.codehaus.jackson.JsonNode;

/**
 * @author Bob Foster
 */
public class JsonStore extends JsonStoreAPI {
    
    static JsonStoreAPI defaultHandler = JsonStoreDefaultHandler.getInstance();
    static JsonStoreAPI handler = null;
    static JsonStore instance = new JsonStore();
    
    public static JsonStore getInstance() {
        return instance;
    }
    
    public static void setJsonStoreHandler(JsonStoreAPI handler) {
        JsonStore.handler = handler;
    }
    
    private JsonStoreAPI getHandler() {
        return handler != null ? handler : defaultHandler;
    }
    
    public void save(String jsonString, JsonNode json) {
        getHandler().save(jsonString, json);
    }

    public Iterator<JsonStored> iterator() {
        return getHandler().iterator();
    }
    
    public Iterator<JsonStored> indexedIterator(String name) {
        return getHandler().indexedIterator(name);
    }
}
