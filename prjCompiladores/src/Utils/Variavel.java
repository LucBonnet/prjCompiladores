package Utils;

public class Variavel {
  public boolean valor;
  public String tipo;
  public String id;
  public int escopo;

  public Variavel(String id, boolean valor, String tipo, int escopo) {
    this.id = id;
    this.valor = valor;
    this.tipo = tipo;
    this.escopo = escopo;
  }
}
