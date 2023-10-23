package Parser;

import java.util.List;

import Translator.Translator;
import Utils.Token;

public class Parser {

    // TODO Mudar métodos
    // TODO Criar árvore

    List<Token> tokens;
    Token token;
    Translator tr = new Translator();

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
        if (token == null) {
            System.out.println("Erro ");
        } else {
            System.out.println("Erro: " + token);
            System.out.println("Linha: " + token.linha);
            System.out.println("Coluna: " + token.coluna);
        }

        throw new Error("Erro");
    }

    private void tipo() {
        String tipos[] = { "int", "dec", "txt", "lgc", "ltr", "lst" };
        Boolean reconhecido = false;

        for (String tipo : tipos) {
            if (Gramaticas.matchLex(token, tipo)) {
                reconhecido = true;
            }
        }

        if (reconhecido) {
            token = getNextToken();
        } else {
            erro(token);
        }
    }

    private void id() {
        if (Gramaticas.matchTipo(token, "ID")) {
            token = getNextToken();
        } else {
            erro(token);
        }
    }

    private void opAtribuicao() {
        if (Gramaticas.matchTipo(token, "OP_ATRIBUICAO")) {
            token = getNextToken();
        } else {
            erro(token);
        }
    }

    private void opM() {
        String ops[] = { "*", "/", "rst", "^" };
        Boolean reconhecido = false;

        for (String op : ops) {
            if (Gramaticas.matchLex(token, op)) {
                reconhecido = true;
            }
        }

        if (reconhecido) {
            token = getNextToken();
        } else {
            erro(token);
        }
    }

    private void op() {
        String ops[] = { "INT", "DEC", "TEXT", "RES_FLS", "RES_VER", "CHAR", "mnr", "mar", "equ", "mnri", "mari" };
        Boolean reconhecido = false;

        for (String op : ops) {
            if (Gramaticas.matchLex(token, op)) {
                reconhecido = true;
            }
        }

        if (reconhecido) {
            token = getNextToken();
        } else {
            erro(token);
        }
    }

    private void opL() {
        String ops[] = { "mnr", "mar", "equ", "mnri", "mari" };
        Boolean reconhecido = false;

        for (String op : ops) {
            if (Gramaticas.matchLex(token, op)) {
                reconhecido = true;
                break;
            }
        }

        if (reconhecido) {
            token = getNextToken();
        } else {
            erro(token);
        }
    }

    private void valor() {
        String valores[] = { "INT", "DEC", "TEXT", "RES_FLS", "RES_VER", "CHAR" };
        Boolean reconhecido = false;

        for (String valor : valores) {
            if (Gramaticas.matchTipo(token, valor)) {
                reconhecido = true;
            }
        }

        if (reconhecido) {
            token = getNextToken();
        } else {
            erro(token);
        }
    }

    private void compLexema(String lexema) {
        if (Gramaticas.matchLex(token, lexema)) {
            token = getNextToken();
        } else {
            erro(token);
        }
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
        if (Gramaticas.opL(token) || Gramaticas.opM(token)) {
            op();
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
            compLexema("(");
            expressao();
            compLexema(")");
        } else {
            erro(token);
        }
    }

    private void condicao() {
        if (Gramaticas.matchTipo(token, "ID")) {
            token = getNextToken();
            opL();
            if (Gramaticas.matchTipo(token, "ID")) {
                id();
            } else {
                expressao();
            }
        } else {
            expressao();
            opL();
            if (Gramaticas.matchTipo(token, "ID")) {
                id();
            } else {
                expressao();
            }
        }
    }

    private void atribuicao() {
        id();
        opAtribuicao();
        expressao();
    }

    private void declaracaoAtribuicao() {
        opAtribuicao();
        expressao();
    }

    private void declaracao() {
        tipo();
        id();
        declaracaoAtribuicao();
    }

    private void rtn() {
        if (Gramaticas.matchLex(token, "rtn")) {
            token = getNextToken();
            id();
        }
    }

    private void bloco(Boolean func) {
        if (Gramaticas.matchLex(token, "{")) {
            compLexema("{");

            while (!Gramaticas.matchLex(token, "}")) {
                if (Gramaticas.matchLex(token, "rtn") && func) {
                    rtn();
                } else {
                    instrucao();
                }
            }

            compLexema("}");
        } else {
            erro(token);
        }
    }

    private void elseMain() {
        if (Gramaticas.matchLex(token, "&")) {
            token = getNextToken();
            bloco(false);
        }
    }

    private void ifMain() {
        if (Gramaticas.matchLex(token, "?")) {
            token = getNextToken();
            compLexema("<");
            condicao();
            compLexema(">");
            bloco(false);
            elseMain();
        }
    }

    private void whileMain() {
        if (Gramaticas.matchLex(token, "loop")) {
            token = getNextToken();
            compLexema("<");
            condicao();
            compLexema(">");
            bloco(false);
        }
    }

    private void forAtribuicaoDeclaracao() {
        if (Gramaticas.tipo(token)) {
            declaracao();
        } else if (Gramaticas.id(token)) {
            atribuicao();
        } else {
            erro(token);
        }
    }

    private void forMain() {
        if (Gramaticas.matchLex(token, "looplim")) {
            token = getNextToken();
            compLexema("<");
            forAtribuicaoDeclaracao();

            if (Gramaticas.matchLex(token, "|")) {
                token = getNextToken();
                condicao();
                if (Gramaticas.matchLex(token, "|")) {
                    token = getNextToken();
                    atribuicao();
                    compLexema(">");
                    bloco(false);
                } else {
                    erro(token);
                }
            } else {
                erro(token);
            }
        }
    }

    private void prt() {
        if (Gramaticas.matchLex(token, "prt")) {
            token = getNextToken();
            compLexema("<");
            expressao();
            compLexema(">");
        }
    }

    private void ent() {
        if (Gramaticas.matchLex(token, "ent")) {
            token = getNextToken();
            compLexema("<");
            if (Gramaticas.tipo(token)) {
                token = getNextToken();
                if (Gramaticas.matchLex(token, ",")) {
                    token = getNextToken();
                    id();
                    compLexema(">");
                } else {
                    erro(token);
                }
            } else {
                erro(token);
            }
        }
    }

    private void func() {
        if (Gramaticas.matchLex(token, "fnc")) {
            token = getNextToken();

            tipo();
            compLexema("<");

            if (Gramaticas.tipo(token)) {
                token = getNextToken();
                id();
                while (!Gramaticas.matchLex(token, ">")) {
                    compLexema(",");
                    tipo();
                    id();
                }
                compLexema(">");
                bloco(true);
            } else if (Gramaticas.matchLex(token, ">")) {
                compLexema(">");
                bloco(true);
            } else {
                erro(token);
            }
        }

    }

    public Boolean instrucao() {
        if (Gramaticas.id(token)) {
            atribuicao();
        } else if (Gramaticas.tipo(token)) {
            declaracao();
        } else if (Gramaticas.matchLex(token, "?")) {
            ifMain();
        } else if (Gramaticas.matchLex(token, "loop")) {
            whileMain();
        } else if (Gramaticas.matchLex(token, "looplim")) {
            forMain();
        } else if (Gramaticas.matchLex(token, "prt")) {
            prt();
        } else if (Gramaticas.matchLex(token, "ent")) {
            ent();
        } else {
            erro(token);
            return false;
        }

        return true;
    }

    public void main() {
        if (Gramaticas.matchLex(token, "fnc")) {
            func();
        } else {
            instrucao();
        }

        if (tokens.size() > 0) {
            main();
        }
    }
}
