package Translator;

public class Translator {
  private String texto;

  public Translator() {
    this.texto = "";
  }

  public void setText(String texto) {
    this.texto += texto;
  }

  public String getTexto() {
    return this.texto;
  }
}
