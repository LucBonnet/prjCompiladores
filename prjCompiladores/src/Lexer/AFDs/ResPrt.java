package Lexer.AFDs;

import Lexer.AFD;
import Lexer.AFDPReservada;
import Utils.Token;

public class ResPrt implements AFD {
    String palavra = "prt";

    @Override
    public Token processa(int pos, String texto) {
        AFDPReservada afdpReservada = new AFDPReservada(palavra);
        if (afdpReservada.processa(pos, texto)) {
            return new Token("RES_PRT", "prt");
        }

        return null;
    }
}
