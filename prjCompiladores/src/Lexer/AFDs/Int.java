package Lexer.AFDs;

import Lexer.AFD;
import Utils.Token;

public class Int implements AFD {
  int pos;
  char atual;

  public String getInteger(String texto) {
    String result = "";
    while (this.atual != '@' && Character.isDigit(this.atual)) {
      result += this.atual;
      avancar(texto);
    }
    return result;
  }

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

    if (Character.isDigit(this.atual)) {
      String digit = getInteger(texto);
      return new Token("INT", digit);
    }

    return null;
  }
}
