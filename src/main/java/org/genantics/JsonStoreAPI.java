/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.genantics;

import java.util.Iterator;
import java.util.NoSuchElementException;
import org.codehaus.jackson.JsonNode;

/**
 * Simple API for storing/retreiving JSON data.
 * 
 * @author bobfoster
 */
public abstract class JsonStoreAPI {
    
    /**
     * Save JSON value.
     * 
     * @param jsonString
     * @param json 
     */
    public abstract void save(String jsonString, JsonNode json);
    
    public abstract Iterator<JsonStored> iterator();
    
    /**
     * Return an iterator over only those JSON objects that have
     * a given top-level key.
     * 
     * Very simple default implementation.
     * @param key
     * @return interator
     */
    public Iterator<JsonStored> indexedIterator(final String key) {
        return new Iterator<JsonStored>() {

            Iterator<JsonStored> it = iterator();
            JsonStored next;
            
            public boolean hasNext() {
                if (next != null)
                    return true;
                while (it.hasNext()) {
                    next = it.next();
                    JsonNode json = next.getJson();
                    if (json.has(key))
                        return true;
                }
                return false;
            }

            public JsonStored next() {
                if (next == null)
                    throw new NoSuchElementException();
                JsonStored tmp = next;
                next = null;
                return tmp;
            }

            public void remove() {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }
            
        };
    }
}
