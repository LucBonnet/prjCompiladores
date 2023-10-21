package Lexer.AFDs;

import Lexer.AFD;
import Utils.Token;

public class Text implements AFD {
  int pos;
  char atual;

  public String getText(String texto) {
    String result = "" + '"';
    while (this.atual != '@' && this.atual != '"') {
      result += this.atual;
      avancar(texto);
    }

    return result + '"';
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

    if (this.atual != '"') {
      return null;
    }

    avancar(texto);
    String text = getText(texto);

    return new Token("TEXT", text);
  }
}