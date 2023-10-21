package Lexer.AFDs;

import Lexer.AFD;
import Utils.Token;

public class LColchete implements AFD {

    @Override
    public Token processa(int pos, String texto) {
        char atual = texto.charAt(pos);
        if (atual == '[') {
            return new Token("L_COLCHETE", "[");
        }
        return null;
    }
}
