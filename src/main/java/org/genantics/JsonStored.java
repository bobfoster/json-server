/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.genantics;

import org.codehaus.jackson.JsonNode;

/**
 *
 * @author bobfoster
 */
public interface JsonStored {
    
    String getTimeStamp();
    
    String getJsonString();
    
    JsonNode getJson();
}
