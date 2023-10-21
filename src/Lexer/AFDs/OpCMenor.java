package Lexer.AFDs;

import Lexer.AFD;
import Lexer.AFDPReservada;
import Utils.Token;

public class OpCMenor implements AFD {
  String palavra = "mnr";

  @Override
  public Token processa(int pos, String texto) {
    AFDPReservada afdpReservada = new AFDPReservada(palavra);
    if (afdpReservada.processa(pos, texto)) {
      return new Token("OP_COMP_MNR", palavra);
    }

    return null;
  }
}
