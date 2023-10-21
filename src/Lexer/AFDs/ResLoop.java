package Lexer.AFDs;

import Lexer.AFD;
import Lexer.AFDPReservada;
import Utils.Token;

public class ResLoop implements AFD {
    String palavra = "loop";

    @Override
    public Token processa(int pos, String texto) {
        AFDPReservada afdpReservada = new AFDPReservada(palavra);
        if (afdpReservada.processa(pos, texto)) {
            return new Token("RES_LOOP", palavra);
        }

        return null;
    }
}
