package Lexer.AFDs;

import Lexer.AFD;
import Utils.Token;

public class Dec implements AFD {
  int pos;
  char atual;
  int cont;

  public String getDec(String texto) {
    String result = "";
    while (this.atual != '@' && (Character.isDigit(this.atual) || (this.atual == '.') && cont < 1)) {
      if (this.atual == '.')
        cont++;
      result += this.atual;
      avancar(texto);
    }
    return result;
  }

  public void avancar(String texto) {
    pos++;
    if (pos > (texto.length() - 1) || cont > 1) {
      this.atual = '@';
    } else {
      this.atual = texto.charAt(pos);
    }
  }

  @Override
  public Token processa(int pos, String texto) {
    this.pos = pos;
    this.atual = texto.charAt(pos);
    this.cont = 0;

    if (Character.isDigit(this.atual)) {
      String dec = getDec(texto);
      if (cont != 1)
        return null;
      return new Token("DEC", dec);
    }

    return null;
  }
}