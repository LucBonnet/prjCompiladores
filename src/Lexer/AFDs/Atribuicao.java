package Lexer.AFDs;

import Lexer.AFD;
import Utils.Token;

public class Atribuicao implements AFD {
  int pos;
  char atual;

  public void avancar(String texto) {
    pos++;
    if (pos > (texto.length() - 1)) {
      this.atual = '@';
    } else {
      this.atual = texto.charAt(pos);
    }
  }

  @Override
  public Token processa(int pos, String texto) {
    this.pos = pos;
    this.atual = texto.charAt(pos);

    if (this.atual == '-') {
      avancar(texto);
      if (this.atual == '>') {
        avancar(texto);
        return new Token("OP_ATRIBUICAO", "->");
      }
      return null;
    }
    return null;
  }

}
