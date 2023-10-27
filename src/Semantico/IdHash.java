package Semantico;

import java.util.HashMap;
import java.util.Map;

public class IdHash {
    public Map<String, String> tblHash;
    
    public IdHash() {
        tblHash = new HashMap<String, String>();
    }
    
    public void addItem(String key, String value) {
        // idName, idType
        tblHash.put(key, value);
    }
    
    public boolean itemExists(String key) {
        if(tblHash == null) return false;
        
        return tblHash.containsKey(key);
    }
    
    public void print() {
        for(String key : tblHash.keySet()) {
            System.out.println(key + ": " + tblHash.get(key));
        }
    }
}
