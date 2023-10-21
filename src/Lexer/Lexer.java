package Lexer;

import java.util.ArrayList;
import java.util.List;

import Utils.Token;
import Utils.InstanceAFDS;

public class Lexer {
  private String texto;
  private int pos;
  private int line;
  private int col;
  private char atual;
  private List<AFD> afds;

  public Lexer(String texto) {
    this.texto = texto;
    this.pos = 0;
    this.atual = texto.charAt(pos);
    
    this.afds = InstanceAFDS.getAFDs();
  }

  public void error() {
    // throw new RuntimeException("Caractere Inválido\nLinha: " + (this.line + 1) +
    // "\nColuna: " + col);
    System.out
        .println("Caractere Inválido: Linha: " + (this.line + 1) + "; Coluna: " + col + "; Caractere: " + this.atual);
    avancar(1);
  }

  public void avancar(int qtde) {
    pos += qtde;
    col += qtde;
    if (pos > (texto.length() - 1)) {
      this.atual = '@';
    } else {
      this.atual = texto.charAt(this.pos);
    }

    if (this.atual == '\n') {
      line++;
      col = 0;
    }
  }

  public void pulaEspacoBranco() {
    while (this.atual != '@' && (this.atual == ' ' || this.atual == '\n')) {
      avancar(1);
    }
  }

  public Token proximoToken() {
    while (this.atual != '@') {
      if (this.atual == ' ' || this.atual == '\n') {
        pulaEspacoBranco();
        continue;
      }

      for (AFD afd : afds) {
        Token reconhecido = afd.processa(pos, texto);
        if (reconhecido != null) {
          avancar(reconhecido.getLength());
          return reconhecido;
        }
      }
      error();
    }
    return new Token("EOF", "@");
  }

  public List<Token> getTokens() {
    List<Token> tokens = new ArrayList<>();

    try {
      Token token = proximoToken();

      while (!token.getTipo().equals("EOF")) {
        System.out.println(token);
        tokens.add(token);
        token = proximoToken();
      }
    } catch (RuntimeException re) {
      System.out.println("Erro: " + re.getMessage());
      re.printStackTrace();
    }

    return tokens;
  }
}
