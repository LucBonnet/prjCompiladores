package Lexer.AFDs;

import Lexer.AFD;
import Utils.Token;

public class ID implements AFD {
  int pos;
  char atual;

  public String getId(String texto) {
    String result = "";
    while (this.atual != '@' && (Character.isAlphabetic(this.atual) || Character.isDigit(this.atual))) {
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

    if (Character.isAlphabetic(this.atual)) {
      String id = getId(texto);
      return new Token("ID", id);
    }

    return null;
  }
}