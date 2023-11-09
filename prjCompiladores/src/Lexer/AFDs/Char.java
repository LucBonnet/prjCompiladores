package Lexer.AFDs;

import Lexer.AFD;
import Utils.Token;

public class Char implements AFD {
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
    
    String text = this.atual + "";
    if (this.atual != "'".charAt(0)) {
      return null;
    }
    avancar(texto);
    text += this.atual + "";
    
    if(atual == "'".charAt(0)) {
        return null;
    }
    avancar(texto);
    text += this.atual + "";
    
    if (this.atual != "'".charAt(0)) {
      return null;
    }

    return new Token("CHAR", text);
  }
}