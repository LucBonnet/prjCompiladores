package Lexer.AFDs;

import Lexer.AFD;
import Utils.Token;

public class Sub implements AFD {
    @Override
    public Token processa(int pos, String texto) {
        char atual = texto.charAt(pos);
        if (atual == '-') {
            return new Token("SUB", "-");
        }
        return null;
    }

}
