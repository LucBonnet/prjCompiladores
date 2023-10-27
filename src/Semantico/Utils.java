package Semantico;


import Semantico.IdHash;
import Translator.Node;
import Utils.Token;

public class Utils {
    public static String addHash(Node nodeTipo, Node nodeId, IdHash hash, Token tk){
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
            hash.addItem(strIdName, strTipo);
            return null;
        } else {
            tk = temp.token;
            return "Variável \"" + strIdName + "\" já declarada";
        }
    }
}
