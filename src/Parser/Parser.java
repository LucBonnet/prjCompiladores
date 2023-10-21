package Parser;

import java.util.List;

import Utils.Token;

public class Parser {
    // TODO LINHA E COLUNA DO ERRO

    List<Token> tokens;
    Token token;

    public Parser(List<Token> tokens) {
        this.tokens = tokens;
        this.token = getNextToken();
    }

    public Token getNextToken() {
        if (tokens.size() > 0) {
            return tokens.remove(0);
        } else {
            return null;
        }
    }

    // metodos
    private void erro(Token token) {
        System.out.println("Erro: " + token);
        throw new Error("Erro");
    }

    private void erro() {
        System.out.println("Erro");
    }

    private void expressao() {
        termo();
        expressaoL();
    }

    private void expressaoL() {
        if (Gramaticas.matchLex(token, "+") || Gramaticas.matchLex(token, "-")) {
            token = getNextToken();
            termo();
            expressaoL();
        }
    }

    private void termo() {
        fator();
        termoL();
    }

    private void termoL() {
        if (Gramaticas.opM(token) || Gramaticas.opL(token)) {
            token = getNextToken();
            fator();
            termoL();
        }
    }

    private void fator() {
        if (Gramaticas.matchTipo(token, "ID")) {
            token = getNextToken();
        } else if (Gramaticas.valor(token)) {
            token = getNextToken();
        } else if (Gramaticas.matchLex(token, "(")) {
            token = getNextToken();
            expressao();
            if (Gramaticas.matchLex(token, ")")) {
                token = getNextToken();
            } else {
                erro(token);
            }
        } else {
            erro(token);
        }
    }

    private void atribuicao() {
        if (Gramaticas.id(token)) {
            // id
            token = getNextToken();
            if (Gramaticas.opAtribuicao(token)) {
                // ->
                token = getNextToken();
                expressao();
            } else {
                erro(token);
            }
        } else {
            erro(token);
        }
    }

    private void declaracao() {
        // tipo
        token = getNextToken();
        if (Gramaticas.id(token)) {
            // id
            token = getNextToken();
            if (Gramaticas.opAtribuicao(token)) {
                // ->
                token = getNextToken();
                expressao();
            } else {
                erro(token);
            }
        } else {
            erro(token);
        }
    }

    public void main() {
        if (Gramaticas.id(token)) {
            atribuicao();
        } else if (Gramaticas.tipo(token)) {
            declaracao();
        } else {
            erro(token);
            return;
        }

        if (tokens.size() > 0) {
            main();
        }
    }
}
