package Utils;

public class Token {
  public String tipo;
  public String lexema;
  public int linha;
  public int coluna;
  
  public Token() {
      
  }

  public Token(String tipo, String lexema) {
    this.tipo = tipo;
    this.lexema = lexema;
  }

  public void setLinhaColuna(int linha, int coluna) {
    this.linha = linha + 1;
    this.coluna = coluna + 1;
  }

  public String getTipo() {
    return this.tipo;
  }

  public String getLexema() {
    return this.lexema;
  }

  public int getLength() {
    return lexema.length();
  }

  @Override
  public String toString() {
    return "< " + tipo + ", " + lexema + " >";
  }
}
