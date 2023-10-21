package Lexer.AFDs;

import Lexer.AFD;
import Lexer.AFDPReservada;
import Utils.Token;

public class OpResto implements AFD {
  String palavra = "rst";

  @Override
  public Token processa(int pos, String texto) {
    AFDPReservada afdpReservada = new AFDPReservada(palavra);
    if (afdpReservada.processa(pos, texto)) {
      return new Token("OP_MAT_RESTO", palavra);
    }

    return null;
  }
}
