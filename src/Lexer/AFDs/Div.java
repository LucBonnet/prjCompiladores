package Lexer.AFDs;

import Lexer.AFD;
import Utils.Token;

public class Div implements AFD {

    @Override
    public Token processa(int pos, String texto) {
        char atual = texto.charAt(pos);
        if (atual == '/') {
            return new Token("DIV", "/");
        }

        return null;
    }

}
