package Parser;

import java.util.List;

import Translator.Node;
import Translator.Tree;
import Utils.Token;

public class Parser {
    List<Token> tokens;
    Token token;
    Token tokenAnterior;
    Tree tree;
    boolean erro = false;

    public Parser(List<Token> tokens) {
        this.tokens = tokens;
        getNextToken();
    }

    public void getNextToken() {
        tokenAnterior = token;
        if (tokens.size() > 0) {
            this.token = tokens.remove(0);
        } else {
            this.token = null;
        }
    }

    // metodos
    private void erro() {
        if (erro) {
            getNextToken();
            return;
        }

        erro = true;
        if (this.token == null) {
            System.out.println("Erro: " + tokenAnterior);
        } else {
            System.out.println("Erro: " + this.token);
            System.out.println("Linha: " + this.token.linha);
            System.out.println("Coluna: " + this.token.coluna);
        }

        getNextToken();

        if (this.token == null) {
            System.exit(0);
        }
    }

    private void tipo(Node node) {
        String tipos[] = { "int", "dec", "txt", "lgc", "ltr", "lst" };
        String tr[] = { "int", "double", "String", "boolean", "char", "String[]" };

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
            getNextToken();
        } else {
            erro();
        }
    }

    private void idLista(Node node) {
        if (Gramaticas.matchLex(token, "[")) {
            node.exit += "[";
            getNextToken();

            if (Gramaticas.matchTipo(token, "INT")) {
                valor(node);
            } else {
                id(node);
            }

            if (Gramaticas.matchLex(token, "]")) {
                node.exit += "]";
                getNextToken();
            } else {
                erro();
            }
        }
    }

    private void id(Node node) {
        if (Gramaticas.matchTipo(token, "ID")) {
            Node newNode = new Node("id");
            node.addChild(newNode);

            newNode.data = token.lexema;
            getNextToken();

            idLista(newNode);
        } else {
            erro();
        }
    }

    private void idDec(Node node) {
        if (Gramaticas.matchTipo(token, "ID")) {
            Node newNode = new Node("id");
            node.addChild(newNode);

            newNode.data = token.lexema;
            getNextToken();

            idListaDec(newNode);
        } else {
            erro();
        }
    }

    private void idListaDec(Node node) {
        if (Gramaticas.matchLex(token, "[")) {
            node.exit += "[";
            getNextToken();

            if (Gramaticas.matchLex(token, "]")) {
                node.exit += "]";
                getNextToken();
            } else {
                erro();
            }
        }
    }

    private void opAtribuicao(Node node) {
        if (Gramaticas.matchTipo(token, "OP_ATRIBUICAO")) {
            Node newNode = new Node("op");
            node.addChild(newNode);

            newNode.data = "=";
            getNextToken();
        } else {
            erro();
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
            getNextToken();
        } else {
            erro();
        }
    }

    private void op(Node node) {
        String ops[] = { "*", "/", "rst", "^", "mnr", "mar", "equ", "mnri", "mari" };
        String tr[] = { "*", "/", "%", "^", "<", ">", "==", "<=", ">=" };

        int index = -1;
        for (int i = 0; i < ops.length; i++) {
            if (Gramaticas.matchLex(token, ops[i])) {
                index = i;
                break;
            }
        }

        if (index > -1) {
            node.data = tr[index];
            getNextToken();
        } else {
            erro();
        }
    }

    private void opPow(Node nodeExp, Node node) {
        if (Gramaticas.matchLex(token, "^")) {
            getNextToken();
            nodeExp.enter = "Math.pow(Double.parseDouble(";
            Node newNode = new Node("),Double.parseDouble(");
            node.addChild(newNode);
            nodeExp.exit = "))";
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
            getNextToken();
        } else {
            erro();
        }
    }

    private void valor(Node node) {
        String valores[] = { "INT", "DEC", "TEXT", "RES_FLS", "RES_VER", "CHAR" };
        Boolean reconhecido = false;

        for (String valor : valores) {
            if (Gramaticas.matchTipo(token, valor)) {
                reconhecido = true;
                break;
            }
        }

        if (reconhecido) {
            node.exit += token.lexema;
            getNextToken();
        } else {
            erro();
        }
    }

    private void compLexema(String lexema) {
        if (Gramaticas.matchLex(token, lexema)) {
            getNextToken();
        } else {
            erro();
        }
    }

    private void expressao(Node node) {
        // TODO
        // Node nodeTermo = new Node("termo");
        // node.addChild(nodeTermo);
        termo(node);

        expressaoL(node);
    }

    private void expressaoL(Node node) {
        if (Gramaticas.matchLex(token, "+") || Gramaticas.matchLex(token, "-")) {
            Node nodeOperador = new Node(token.lexema);
            node.addChild(nodeOperador);

            Node newNode = new Node("expressaoL");
            node.addChild(newNode);
            getNextToken();

            Node nodeTermo = new Node("termo");
            newNode.addChild(nodeTermo);
            termo(nodeTermo);

            Node nodeExpressaoL = new Node("");
            newNode.addChild(nodeExpressaoL);
            expressaoL(nodeExpressaoL);
        }
    }

    private void termo(Node node) {
        Node nodeFator = new Node("");
        node.addChild(nodeFator);
        fator(nodeFator);

        termoL(node);
    }

    private void termoL(Node node) {
        if (Gramaticas.opL(token) || Gramaticas.opM(token)) {
            Node newNode = new Node("termoL");
            node.addChild(newNode);

            if (Gramaticas.matchLex(token, "^")) {
                opPow(node, newNode);
            } else {
                Node nodeOp = new Node("operador");
                newNode.addChild(nodeOp);
                op(nodeOp);
            }

            Node nodeFator = new Node("fator");
            node.addChild(nodeFator);
            fator(nodeFator);

            Node nodeTermoL = new Node("");
            newNode.addChild(nodeTermoL);
            termoL(nodeTermoL);
        }
    }

    private void fator(Node node) {
        if (Gramaticas.matchTipo(token, "ID")) {
            id(node);
        } else if (Gramaticas.valor(token)) {
            valor(node);
        } else if (Gramaticas.matchLex(token, "(")) {
            node.enter = "(";
            compLexema("(");

            Node nodeExpressao = new Node("expressao");
            node.addChild(nodeExpressao);
            expressao(nodeExpressao);

            compLexema(")");
            node.exit = ")";
        } else {
            erro();
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

        if (Gramaticas.matchLex(token, "[")) {
            Node nodeLista = new Node("");
            node.addChild(nodeLista);
            nodeLista.enter = "{";
            getNextToken();

            if (Gramaticas.valor(token)) {
                Node nodeValorP = new Node("");
                nodeLista.addChild(nodeValorP);
                valor(nodeValorP);

                while (!Gramaticas.matchLex(token, "]")) {
                    compLexema(",");

                    Node nodeValorP2 = new Node("");
                    nodeLista.addChild(nodeValorP2);
                    nodeValorP2.enter = ",";
                    valor(nodeValorP2);
                }
            }

            compLexema("]");
            node.exit = "}";
        } else {
            Node nodeExpressao = new Node("expressao");
            node.addChild(nodeExpressao);
            expressao(nodeExpressao);
        }
    }

    private void declaracao(Node node) {
        Node nodeTipo = new Node("tipo");
        node.addChild(nodeTipo);
        tipo(nodeTipo);

        Node nodeId = new Node("id");
        node.addChild(nodeId);
        idDec(nodeId);

        if (Gramaticas.matchLex(token, "->")) {
            Node nodeDA = new Node("decAtri");
            node.addChild(nodeDA);
            declaracaoAtribuicao(nodeDA);
        }
    }

    private void rtn(Node node) {
        if (Gramaticas.matchLex(token, "rtn")) {
            node.enter = "return";
            getNextToken();
            Node nodeExpressao = new Node("expressao");
            node.addChild(nodeExpressao);
            expressao(nodeExpressao);
            node.exit = ";";
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
            erro();
        }
    }

    private void elseMain(Node node) {
        if (Gramaticas.matchLex(token, "&")) {
            Node newNode = new Node("else");
            node.addChild(newNode);
            newNode.enter = "else";
            getNextToken();

            Node nodeBloco = new Node("bloco");
            newNode.addChild(nodeBloco);
            bloco(nodeBloco, false);
        }
    }

    private void ifMain(Node node) {
        if (Gramaticas.matchLex(token, "?")) {
            node.enter = "if";
            getNextToken();
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

    private void whileMain(Node node) {
        if (Gramaticas.matchLex(token, "loop")) {
            node.enter = "while";
            getNextToken();
            compLexema("<");

            Node nodeCond = new Node("condicao");
            node.addChild(nodeCond);
            condicao(nodeCond);
            nodeCond.enter = "(";
            nodeCond.exit = ")";

            compLexema(">");

            Node nodeBloco = new Node("bloco");
            node.addChild(nodeBloco);
            bloco(nodeBloco, false);
        }
    }

    private void forAtribuicaoDeclaracao(Node node) {
        if (Gramaticas.tipo(token)) {
            declaracao(node);
        } else if (Gramaticas.id(token)) {
            atribuicao(node);
        } else {
            erro();
        }
        node.exit = ";";
    }

    private void forParametros(Node node) {
        node.enter = "(";
        Node nodeAtribFor = new Node("atribuicao for");
        node.addChild(nodeAtribFor);
        forAtribuicaoDeclaracao(nodeAtribFor);

        if (Gramaticas.matchLex(token, "|")) {
            getNextToken();

            Node nodeCondicao = new Node("condicao");
            node.addChild(nodeCondicao);
            condicao(nodeCondicao);
            nodeCondicao.exit = ";";

            if (Gramaticas.matchLex(token, "|")) {
                getNextToken();

                Node nodeAtribuicao = new Node("atribuicao");
                node.addChild(nodeAtribuicao);
                atribuicao(nodeAtribuicao);

                compLexema(">");
            } else {
                erro();
            }
        } else {
            erro();
        }
        node.exit = ")";
    }

    private void forMain(Node node) {
        if (Gramaticas.matchLex(token, "looplim")) {
            getNextToken();
            compLexema("<");
            node.enter = "for";

            Node nodeParametros = new Node(" parametros for");
            node.addChild(nodeParametros);
            forParametros(nodeParametros);

            Node nodeBloco = new Node("");
            node.addChild(nodeBloco);
            bloco(nodeBloco, false);
        }
    }

    private void comentario() {
        if (Gramaticas.matchTipo(token, "COMENTARIO")) {
            getNextToken();
        }
    }

    private void prt(Node node) {
        if (Gramaticas.matchLex(token, "prt") || Gramaticas.matchLex(token, "prtln")) {
            if (Gramaticas.matchLex(token, "prt"))
                node.enter = "System.out.print(";
            else
                node.enter = "System.out.println(";

            getNextToken();
            compLexema("<");

            Node nodeExpressao = new Node("expressao");
            node.addChild(nodeExpressao);
            expressao(nodeExpressao);

            compLexema(">");
            node.exit = ");";
        }
    }

    private void ent(Node node) {
        if (Gramaticas.matchLex(token, "ent")) {
            String numScanner = token.linha + "" + token.coluna;
            node.enter = "Scanner sc" + numScanner + " = new Scanner(System.in);";
            getNextToken();
            compLexema("<");
            Node nodeTipo = new Node("tipo");
            node.addChild(nodeTipo);
            tipo(nodeTipo);

            if (Gramaticas.matchLex(token, ",")) {
                getNextToken();
                Node nodeId = new Node("id");
                node.addChild(nodeId);
                id(nodeId);
                compLexema(">");
            } else {
                erro();
            }

            node.exit = " = sc" + numScanner + ".next";

            switch (nodeTipo.children.get(0).data) {
                case "double":
                    node.exit += "Double";
                    break;
                case "int":
                    node.exit += "Int";
                    break;
                case "boolean":
                    node.exit += "Boolean";
                    break;
                case "String":
                    node.exit += "Line";
                    break;
            }

            node.exit += "();";

        }
    }

    private void func(Node node) {
        if (Gramaticas.matchLex(token, "fnc")) {
            Node nodeFunc = new Node("func");
            getNextToken();
            nodeFunc.enter = "public static ";
            node.addChild(nodeFunc);

            // tipo do retorno
            if (Gramaticas.tipo(token)) {
                Node nodetipo = new Node("tipo");
                nodeFunc.addChild(nodetipo);
                tipo(nodetipo);
            } else {
                nodeFunc.enter += "void";
            }

            // nome da função
            Node nodeId = new Node("id");
            nodeFunc.addChild(nodeId);
            id(nodeId);

            Node nodeParams = new Node("params");
            nodeFunc.addChild(nodeParams);
            compLexema("<");
            nodeParams.enter = "(";

            if (Gramaticas.tipo(token)) {
                Node nodeTipoP = new Node("id");
                nodeParams.addChild(nodeTipoP);
                tipo(nodeTipoP);

                Node nodeIdP = new Node("id");
                nodeParams.addChild(nodeIdP);
                id(nodeIdP);

                while (!Gramaticas.matchLex(token, ">")) {
                    compLexema(",");

                    Node nodeTipoP2 = new Node("tipo");
                    nodeTipoP2.enter = ",";
                    nodeParams.addChild(nodeTipoP2);
                    tipo(nodeTipoP2);
                    Node nodeIdP2 = new Node("id");
                    nodeParams.addChild(nodeIdP2);
                    id(nodeIdP2);
                }
                compLexema(">");
                nodeParams.exit = ")";

                Node nodeBloco = new Node("");
                nodeFunc.addChild(nodeBloco);
                bloco(nodeBloco, true);
            } else if (Gramaticas.matchLex(token, ">")) {
                compLexema(">");
                nodeParams.exit = ")";
                Node nodeBloco = new Node("");
                nodeFunc.addChild(nodeBloco);
                bloco(nodeBloco, true);
            } else {
                erro();
            }
        }

    }

    public Boolean instrucao(Node node) {
        if (Gramaticas.id(token)) {
            erro = false;
            Node newNode = new Node("atribuicao");
            node.addChild(newNode);
            atribuicao(newNode);
            newNode.exit += ";";
        } else if (Gramaticas.tipo(token)) {
            erro = false;
            Node newNode = new Node("declaracao");
            node.addChild(newNode);
            declaracao(newNode);
            newNode.exit += ";";
        } else if (Gramaticas.matchLex(token, "?")) {
            erro = false;
            Node newNode = new Node("if");
            node.addChild(newNode);
            ifMain(newNode);
        } else if (Gramaticas.matchLex(token, "loop")) {
            erro = false;
            Node newNode = new Node("while");
            node.addChild(newNode);
            whileMain(newNode);
        } else if (Gramaticas.matchLex(token, "looplim")) {
            erro = false;
            Node newNode = new Node("for");
            node.addChild(newNode);
            forMain(newNode);
        } else if (Gramaticas.matchLex(token, "prt") || Gramaticas.matchLex(token, "prtln")) {
            erro = false;
            Node newNode = new Node("prt");
            node.addChild(newNode);
            prt(newNode);
        } else if (Gramaticas.matchLex(token, "ent")) {
            erro = false;
            Node newNode = new Node("ent");
            node.addChild(newNode);
            ent(newNode);
        } else if (Gramaticas.matchTipo(token, "COMENTARIO")) {
            comentario();
        } else {
            erro();
            return false;
        }

        return true;
    }

    public void code(Node nodeClass, Node nodeMain) {
        if (Gramaticas.matchLex(this.token, "fnc")) {
            func(nodeClass);
        } else {
            instrucao(nodeMain);
        }

        if (tokens.size() > 0) {
            code(nodeClass, nodeMain);
        }
    }

    public Tree main() {
        Node nodeRoot = new Node("root");

        Node nodeClass = new Node("");
        nodeClass.enter = "import java.util.Scanner;\nclass Main {\n";
        nodeRoot.addChild(nodeClass);

        Node nodeMain = new Node("main");
        nodeMain.enter = "\npublic static void main (String args[]){\n";
        nodeMain.exit = "\n}";
        nodeRoot.addChild(nodeMain);

        nodeRoot.exit = "\n}";
        tree = new Tree(nodeRoot);

        code(nodeClass, nodeMain);

        // TODO ERRO;
        if (erro) {
            tree.root = null;
        }

        return tree;
    }
}
