package Lexer;

import Utils.Token;

public interface AFD {
    public Token processa(int pos, String texto);
}
