package Semantico;

import Translator.Node;
import Utils.Token;

public class Utils {
    public static Token addHash(Node nodeTipo, Node nodeId, IdHash hash, boolean func) {
        String strTipo = "";
        Node temp = nodeTipo;
        while (temp.getChildren().size() > 0) {
            temp = temp.getChild(0);
        }
        strTipo = temp.data;

        String strIdName = "";
        temp = nodeId;
        while (temp.getChildren().size() > 0) {
            temp = temp.getChild(0);
        }
        strIdName = temp.data;

        if (!hash.itemExists(strIdName)) {
            hash.addItem(strIdName, func, strTipo);
            return null;
        } else {
            return temp.token;
        }
    }
}
