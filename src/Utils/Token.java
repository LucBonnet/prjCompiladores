package Utils;

public class Token {
  public String tipo;
  public String lexema;

  public Token(String tipo, String lexema) {
    this.tipo = tipo;
    this.lexema = lexema;
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
