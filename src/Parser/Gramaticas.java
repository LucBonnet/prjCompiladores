package Parser;

import Utils.Token;

public class Gramaticas {
  public static Boolean matchLex(Token token, String lexema) {
    if (token == null)
      return false;

    if (token.lexema.equals(lexema)) {
      return true;
    }
    return false;
  }

  public static Boolean matchTipo(Token token, String tipo) {
    if (token == null)
      return false;

    if (token.tipo.equals(tipo)) {
      return true;
    }
    return false;
  }

  public static Boolean tipo(Token token) {
    String tipos[] = { "int", "dec", "txt", "lgc", "ltr", "lst" };

    for (String tipo : tipos) {
      if (matchLex(token, tipo)) {
        return true;
      }
    }

    return false;
  }

  public static Boolean id(Token token) {
    if (matchTipo(token, "ID")) {
      return true;
    }
    return false;
  }

  public static Boolean opAtribuicao(Token token) {
    if (matchTipo(token, "OP_ATRIBUICAO")) {
      return true;
    }
    return false;
  }

  public static Boolean opM(Token token) {
    String ops[] = { "*", "/", "rst", "^" };

    for (String op : ops) {
      if (matchLex(token, op)) {
        return true;
      }
    }

    return false;
  }

  public static Boolean opL(Token token) {
    String ops[] = { "mnr", "mar", "equ", "mnri", "mari" };

    for (String op : ops) {
      if (matchLex(token, op)) {
        return true;
      }
    }

    return false;
  }

  public static Boolean valor(Token token) {
    String valores[] = { "INT", "DEC", "TEXT", "RES_FLS", "RES_VER", "CHAR" };

    for (String valor : valores) {
      if (matchTipo(token, valor)) {
        return true;
      }
    }

    return false;
  }
}
