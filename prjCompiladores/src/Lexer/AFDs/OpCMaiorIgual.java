package Lexer.AFDs;

import Lexer.AFD;
import Lexer.AFDPReservada;
import Utils.Token;

public class OpCMaiorIgual implements AFD {
  String palavra = "mari";

  @Override
  public Token processa(int pos, String texto) {
    AFDPReservada afdpReservada = new AFDPReservada(palavra);
    if (afdpReservada.processa(pos, texto)) {
      return new Token("OP_COMP_MAR_IGUAL", palavra);
    }

    return null;
  }
}
