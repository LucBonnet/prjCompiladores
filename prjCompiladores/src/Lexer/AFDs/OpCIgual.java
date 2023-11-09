package Lexer.AFDs;

import Lexer.AFD;
import Lexer.AFDPReservada;
import Utils.Token;

public class OpCIgual implements AFD {
  String palavra = "equ";

  @Override
  public Token processa(int pos, String texto) {
    AFDPReservada afdpReservada = new AFDPReservada(palavra);
    if (afdpReservada.processa(pos, texto)) {
      return new Token("OP_COMP_EQU", palavra);
    }

    return null;
  }
}
