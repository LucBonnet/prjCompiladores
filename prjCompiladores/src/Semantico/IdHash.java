package Semantico;

import java.util.HashMap;
import java.util.Map;

import Utils.Variavel;

public class IdHash {
    public Map<String, Variavel> tblHash;
    public int escopo = 0;

    public IdHash() {
        tblHash = new HashMap<String, Variavel>();
    }

    public void addItem(String key, boolean value, String tipo) {
        // idName, idValue, idType
        Variavel v = new Variavel(key, value, tipo, escopo);
        tblHash.put(key, v);
    }

    public boolean itemExists(String key) {
        if (tblHash == null)
            return false;

        return tblHash.containsKey(key);
    }

    public Variavel getItem(String key) {
        return tblHash.get(key);
    }

    public void print() {
        for (String key : tblHash.keySet()) {
            System.out.println(key + ": " + tblHash.get(key));
        }
    }
}
