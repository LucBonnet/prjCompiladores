package Parser;

import Utils.Token;
import java.util.List;

public class Atribuicao implements IParser {

    List<Token> tokens;
    Token token;
    Boolean erro;
    int numTokens;

    public Atribuicao(List<Token> tokens) {
        this.tokens = tokens;
        this.erro = false;
    }

    private void erro() {
        this.erro = true;
    }

    public Token getNextToken() {
        if (numTokens > 4) {
            numTokens++;
            return tokens.remove(0);
        } else {
            return null;
        }
    }

    @Override
    public Boolean main() {
        token = getNextToken();
        tipo();
        id();
        if (matchLex("->")) {
            token = getNextToken();
            valor();
        } else {
            erro();
        }

        if (this.erro) {
            System.out.println("Erro");
            return false;
        }

        System.out.println(tokens);
        return true;
    }

    private Boolean matchLex(String lexema) {
        if (token == null)
            return false;

        if (token.lexema.equals(lexema)) {
            return true;
        }
        return false;
    }

    private Boolean matchTipo(String tipo) {
        if (token == null)
            return false;

        if (token.tipo.equals(tipo)) {
            return true;
        }
        return false;
    }

    private void id() {
        if (matchTipo("ID")) {
            token = getNextToken();
        } else {
            erro();
        }
    }

    private void tipo() {
        String tipos[] = { "int", "dec", "txt", "lgc", "ltr" };
        Boolean achou = false;
        for (String tipo : tipos) {
            if (matchLex(tipo)) {
                token = getNextToken();
                achou = true;
                break;
            }
        }

        if (!achou) {
            erro();
        }
    }

    private void valor() {
        String valores[] = { "INT", "DEC", "TEXT", "RES_FLS", "RES_VER", "CHAR" };
        Boolean achou = false;
        for (String valor : valores) {
            if (matchTipo(valor)) {
                token = getNextToken();
                achou = true;
                break;
            }
        }

        if (!achou) {
            erro();
        }
    }
}
