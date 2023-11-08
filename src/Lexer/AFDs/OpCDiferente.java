package Lexer.AFDs;

import Lexer.AFD;
import Lexer.AFDPReservada;
import Utils.Token;

public class OpCDiferente implements AFD {
  String palavra = "dif";

  @Override
  public Token processa(int pos, String texto) {
    AFDPReservada afdpReservada = new AFDPReservada(palavra);
    if (afdpReservada.processa(pos, texto)) {
      return new Token("OP_COMP_DIF", palavra);
    }

    return null;
  }
}
