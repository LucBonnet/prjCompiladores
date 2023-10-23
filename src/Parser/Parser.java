package Parser;

import java.util.List;

import Translator.Node;
import Translator.Tree;
import Utils.Token;

public class Parser {

    // TODO Mudar métodos
    // TODO Criar árvore

    List<Token> tokens;
    Token token;
    Tree tree;

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

    private void tipo(Node node) {
        String tipos[] = { "int", "dec", "txt", "lgc", "ltr", "lst" };
        String tr[] = { "int", "float", "char[]", "boolean", "char", "String[]" };

        int index = -1;
        for (int i = 0; i < tipos.length; i++) {
            if (Gramaticas.matchLex(token, tipos[i])) {
                index = i;
                break;
            }
        }

        if (index > -1) {
            Node newNode = new Node(tr[index]);
            node.addChild(newNode);
            token = getNextToken();
        } else {
            erro(token);
        }
    }

    private void id(Node node) {
        if (Gramaticas.matchTipo(token, "ID")) {
            Node newNode = new Node("id");
            node.addChild(newNode);

            newNode.data = token.lexema;
            token = getNextToken();
        } else {
            erro(token);
        }
    }

    private void opAtribuicao(Node node) {
        if (Gramaticas.matchTipo(token, "OP_ATRIBUICAO")) {
            Node newNode = new Node("op");
            node.addChild(newNode);

            newNode.data = "=";
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

    private void op(Node node) {
        String ops[] = { "*", "/", "rst", "^", "mnr", "mar", "equ", "mnri", "mari" };
        Boolean reconhecido = false;

        for (String op : ops) {
            if (Gramaticas.matchLex(token, op)) {
                reconhecido = true;
                break;
            }
        }

        if (reconhecido) {
            node.data = token.lexema;
            token = getNextToken();
        } else {
            erro(token);
        }
    }

    private void opL(Node node) {
        String ops[] = { "mnr", "mar", "equ", "mnri", "mari" };
        String tr[] = { "<", ">", "==", "<=", ">=" };

        int index = -1;
        for (int i = 0; i < ops.length; i++) {
            if (Gramaticas.matchLex(token, ops[i])) {
                index = i;
                break;
            }
        }

        if (index > -1) {
            Node newNode = new Node(tr[index]);
            node.addChild(newNode);
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

    private void expressao(Node node) {
        Node nodeTermo = new Node("termo");
        node.addChild(nodeTermo);
        termo(nodeTermo);

        expressaoL(node);
    }

    private void expressaoL(Node node) {
        if (Gramaticas.matchLex(token, "+") || Gramaticas.matchLex(token, "-")) {
            Node newNode = new Node("expressaoL");
            node.addChild(newNode);

            token = getNextToken();
            Node nodeTermo = new Node("termo");
            newNode.addChild(nodeTermo);
            termo(nodeTermo);

            Node nodeExpressaoL = new Node("");
            newNode.addChild(nodeExpressaoL);
            expressaoL(nodeExpressaoL);
        }
    }

    private void termo(Node node) {
        Node nodeFator = new Node("fator");
        node.addChild(nodeFator);
        fator(nodeFator);

        termoL(node);
    }

    private void termoL(Node node) {
        if (Gramaticas.opL(token) || Gramaticas.opM(token)) {
            Node newNode = new Node("");
            node.addChild(newNode);

            Node nodeOp = new Node("operador");
            newNode.addChild(nodeOp);
            op(nodeOp);

            Node nodeFator = new Node("fator");
            newNode.addChild(nodeFator);
            fator(nodeFator);

            Node nodeTermoL = new Node("");
            newNode.addChild(nodeTermoL);
            termoL(nodeTermoL);
        }
    }

    private void fator(Node node) {
        if (Gramaticas.matchTipo(token, "ID")) {
            node.data = token.lexema;
            token = getNextToken();
        } else if (Gramaticas.valor(token)) {
            node.data = token.lexema;
            token = getNextToken();
        } else if (Gramaticas.matchLex(token, "(")) {
            node.enter = "(";
            compLexema("(");

            Node nodeExpressao = new Node("expressao");
            node.addChild(nodeExpressao);
            expressao(nodeExpressao);

            compLexema(")");
            node.exit = ")";
        } else {
            erro(token);
        }
    }

    private void condicao(Node node) {
        if (Gramaticas.matchTipo(token, "ID")) {
            Node nodeId = new Node("id");
            node.addChild(nodeId);
            id(nodeId);

            Node nodeOp = new Node("operador logico");
            node.addChild(nodeOp);
            opL(nodeOp);
            if (Gramaticas.matchTipo(token, "ID")) {
                nodeId = new Node("id");
                node.addChild(nodeId);
                id(nodeId);
            } else {
                Node nodeExpressao = new Node("expressao");
                node.addChild(nodeExpressao);
                expressao(nodeExpressao);
            }
        } else {
            Node nodeExpressao = new Node("id");
            node.addChild(nodeExpressao);
            expressao(nodeExpressao);
            opL(node);
            if (Gramaticas.matchTipo(token, "ID")) {
                Node nodeId = new Node("id");
                node.addChild(nodeId);
                id(nodeId);
            } else {
                nodeExpressao = new Node("expressao");
                node.addChild(nodeExpressao);
                expressao(nodeExpressao);
            }
        }
    }

    private void atribuicao(Node node) {
        Node nodeId = new Node("id");
        node.addChild(nodeId);
        id(nodeId);

        Node nodeOperador = new Node("op");
        node.addChild(nodeOperador);
        opAtribuicao(nodeOperador);

        Node nodeExpressao = new Node("expressao");
        node.addChild(nodeExpressao);
        expressao(nodeExpressao);
    }

    private void declaracaoAtribuicao(Node node) {
        Node nodeOp = new Node("op");
        node.addChild(nodeOp);
        opAtribuicao(nodeOp);

        Node nodeExpressao = new Node("expressao");
        node.addChild(nodeExpressao);
        expressao(nodeExpressao);
    }

    private void declaracao(Node node) {
        Node nodeTipo = new Node("tipo");
        node.addChild(nodeTipo);
        tipo(nodeTipo);

        Node nodeId = new Node("id");
        node.addChild(nodeId);
        id(nodeId);

        Node nodeDA = new Node("decAtri");
        node.addChild(nodeDA);
        declaracaoAtribuicao(nodeDA);
    }

    private void rtn(Node node) {
        if (Gramaticas.matchLex(token, "rtn")) {
            token = getNextToken();
            Node nodeId = new Node("id");
            node.addChild(nodeId);
            id(nodeId);
        }
    }

    private void bloco(Node node, Boolean func) {
        if (Gramaticas.matchLex(token, "{")) {
            node.enter = "{";
            compLexema("{");

            while (!Gramaticas.matchLex(token, "}")) {
                if (Gramaticas.matchLex(token, "rtn") && func) {
                    Node nodeReturn = new Node("return");
                    node.addChild(nodeReturn);
                    rtn(nodeReturn);
                } else {
                    Node nodeInst = new Node("instrucao");
                    node.addChild(nodeInst);
                    instrucao(nodeInst);
                }
            }

            compLexema("}");
            node.exit = "}";
        } else {
            erro(token);
        }
    }

    private void elseMain(Node node) {
        if (Gramaticas.matchLex(token, "&")) {
            Node newNode = new Node("else");
            node.addChild(newNode);
            newNode.enter = "else";
            token = getNextToken();

            Node nodeBloco = new Node("bloco");
            newNode.addChild(nodeBloco);
            bloco(nodeBloco, false);
        }
    }

    private void ifMain(Node node) {
        if (Gramaticas.matchLex(token, "?")) {
            node.enter = "if";
            token = getNextToken();
            compLexema("<");

            Node nodeCondicao = new Node("condicao");
            node.addChild(nodeCondicao);
            nodeCondicao.enter = "(";
            condicao(nodeCondicao);
            nodeCondicao.exit = ")";
            compLexema(">");

            Node nodeBloco = new Node("bloco");
            node.addChild(nodeBloco);
            bloco(nodeBloco, false);

            elseMain(node);
        }
    }

    // private void whileMain() {
    // if (Gramaticas.matchLex(token, "loop")) {
    // token = getNextToken();
    // compLexema("<");
    // condicao();
    // compLexema(">");
    // bloco(false);
    // }
    // }

    // private void forAtribuicaoDeclaracao() {
    // if (Gramaticas.tipo(token)) {
    // declaracao();
    // } else if (Gramaticas.id(token)) {
    // // atribuicao();
    // // TODO NODE
    // } else {
    // erro(token);
    // }
    // }

    // private void forMain() {
    // if (Gramaticas.matchLex(token, "looplim")) {
    // token = getNextToken();
    // compLexema("<");
    // forAtribuicaoDeclaracao();

    // if (Gramaticas.matchLex(token, "|")) {
    // token = getNextToken();
    // condicao();
    // if (Gramaticas.matchLex(token, "|")) {
    // token = getNextToken();
    // // atribuicao();
    // // TODO NODE
    // compLexema(">");
    // bloco(false);
    // } else {
    // erro(token);
    // }
    // } else {
    // erro(token);
    // }
    // }
    // }

    // private void prt() {
    // if (Gramaticas.matchLex(token, "prt")) {
    // token = getNextToken();
    // compLexema("<");
    // expressao();
    // compLexema(">");
    // }
    // }

    // private void ent() {
    // if (Gramaticas.matchLex(token, "ent")) {
    // token = getNextToken();
    // compLexema("<");
    // if (Gramaticas.tipo(token)) {
    // token = getNextToken();
    // if (Gramaticas.matchLex(token, ",")) {
    // token = getNextToken();
    // id();
    // compLexema(">");
    // } else {
    // erro(token);
    // }
    // } else {
    // erro(token);
    // }
    // }
    // }

    // private void func() {
    // if (Gramaticas.matchLex(token, "fnc")) {
    // token = getNextToken();

    // tipo();
    // compLexema("<");

    // if (Gramaticas.tipo(token)) {
    // token = getNextToken();
    // id();
    // while (!Gramaticas.matchLex(token, ">")) {
    // compLexema(",");
    // tipo();
    // id();
    // }
    // compLexema(">");
    // bloco(true);
    // } else if (Gramaticas.matchLex(token, ">")) {
    // compLexema(">");
    // bloco(true);
    // } else {
    // erro(token);
    // }
    // }

    // }

    public Boolean instrucao(Node node) {
        if (Gramaticas.id(token)) {
            Node newNode = new Node("atribuicao");
            node.addChild(newNode);
            atribuicao(newNode);
            newNode.exit = ";";
        } else if (Gramaticas.tipo(token)) {
            Node newNode = new Node("declaracao");
            node.addChild(newNode);
            declaracao(newNode);
            newNode.exit = ";";
        } else if (Gramaticas.matchLex(token, "?")) {
            Node newNode = new Node("if");
            node.addChild(newNode);
            ifMain(newNode);
            // } else if (Gramaticas.matchLex(token, "loop")) {
            // whileMain();
            // } else if (Gramaticas.matchLex(token, "looplim")) {
            // forMain();
            // } else if (Gramaticas.matchLex(token, "prt")) {
            // prt();
            // } else if (Gramaticas.matchLex(token, "ent")) {
            // ent();
        } else {
            erro(token);
            return false;
        }

        return true;
    }

    public void code(Node node) {
        if (Gramaticas.matchLex(token, "fnc")) {
            // func();
        } else {
            instrucao(node);
        }

        if (tokens.size() > 0) {
            code(node);
        }
    }

    public Tree main() {
        Node node;
        String mainEnter = "#include <stdlib.h>\n#include <stdio.h>\n\nint main() {\n";
        String mainExit = "\nreturn 0;\n}";
        node = new Node("main");
        node.enter = mainEnter;
        node.exit = mainExit;
        tree = new Tree(node);

        code(node);

        return tree;
    }
}
