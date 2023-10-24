package Lexer.AFDs;

import Lexer.AFD;
import Lexer.AFDPReservada;
import Utils.Token;

public class ResComentario implements AFD {
  int pos;
  char atual;
  String palavra = "cmt";

  public String getComentario(String texto) {
    String result = palavra;
    while (this.atual != '@'
        && (Character.isDigit(this.atual) || Character.isAlphabetic(this.atual) || this.atual == ' ')) {
      result += this.atual;
      avancar(texto);

      AFDPReservada afdpReservada = new AFDPReservada("/&/");
      if (afdpReservada.processa(pos, texto)) {
        break;
      }
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

  public void avancar(String texto, int qtd) {
    for (int i = 0; i < qtd; i++) {
      avancar(texto);
    }
  }

  @Override
  public Token processa(int pos, String texto) {
    this.pos = pos;
    this.atual = texto.charAt(pos);

    AFDPReservada afdpReservada = new AFDPReservada(palavra);
    if (afdpReservada.processa(pos, texto)) {
      avancar(texto, palavra.length());

      String comentario = getComentario(texto);
      return new Token("COMENTARIO", comentario);
    }

    return null;
  }
}
