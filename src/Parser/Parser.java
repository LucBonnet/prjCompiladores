package Parser;

import java.util.List;

import javax.xml.transform.Source;

import Translator.Node;
import Translator.Tree;
import Utils.Token;

public class Parser {
    boolean erro = false;
    List<Token> tokens;
    Token token;
    Token tokenAnterior;
    Tree tree;

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

    private boolean matchL(String lexema) {
        if (token == null)
            return false;

        if (token.lexema.equals(lexema)) {
            return true;
        }

        return false;
    }

    private boolean matchT(String tipo) {
        if (token == null)
            return false;

        if (token.tipo.equals(tipo)) {
            return true;
        }

        return false;
    }

    public Boolean matchTipo() {
        String tipos[] = { "int", "dec", "txt", "lgc", "ltr" };

        for (String tipo : tipos) {
            if (matchL(tipo)) {
                return true;
            }
        }

        return false;
    }

    public Boolean matchValor() {
        String valores[] = { "INT", "DEC", "TEXT", "RES_FLS", "RES_VER", "CHAR" };

        for (String valor : valores) {
            if (matchT(valor)) {
                return true;
            }
        }

        return false;
    }

    public Boolean matchOpT() {
        String opTs[] = { "^", "*", "rst", "/", "mnr", "mar", "equ", "mnri", "mari" };

        for (String op : opTs) {
            if (matchL(op)) {
                return true;
            }
        }

        return false;
    }

    public Boolean matchId() {
        if (matchT("ID")) {
            return true;
        }

        return false;
    }

    private void tipo(Node node) {
        System.out.println(token);
        String tipos[] = { "dec", "int", "txt", "lgc", "ltr" };
        String tr[] = { "double", "int", "String", "boolean", "char" };

        int index = -1;
        for (int i = 0; i < tipos.length; i++) {
            if (matchL(tipos[i])) {
                index = i;
                break;
            }
        }

        if (index > -1) {
            Node tipo = new Node(tr[index]);
            node.addChild(tipo);
            getNextToken();
        } else {
            erro();
        }
    }

    private void valor(Node node) {
        String valores[] = { "INT", "DEC", "TEXT", "RES_FLS", "RES_VER", "CHAR" };
        Boolean reconhecido = false;

        for (String valor : valores) {
            if (matchT(valor)) {
                reconhecido = true;
                break;
            }
        }

        if (reconhecido) {
            Node valor = new Node(token.lexema);
            node.addChild(valor);
            getNextToken();
        } else {
            erro();
        }
    }

    private void id(Node node) {
        if (matchT("ID")) {
            Node id = new Node(token.lexema);
            node.addChild(id);
            getNextToken();
        } else {
            erro();
        }
    }

    private void idDec(Node node) {
        Node idDeclaracao = new Node("idDeclaracao");
        node.addChild(idDeclaracao);
        id(idDeclaracao);

        if (matchL("[")) {
            getNextToken();
            if (matchL("]")) {
                getNextToken();
                node.exit = "[]";
            } else {
                erro();
            }
        }
    }

    private void idLista(Node node) {
        System.out.println(token);
        if (matchL("[")) {
            Node idLista = new Node("idLista");
            node.addChild(idLista);

            idLista.enter = "[";
            getNextToken();

            if (matchT("INT") || matchId()) {
                idLista.data = token.lexema;
                getNextToken();
                if (matchL("]")) {
                    idLista.exit = "]";
                    getNextToken();
                } else {
                    erro();
                }
            } else {
                erro();
            }
        }
    }

    public void idFator(Node node) {
        if (matchId()) {
            id(node);
            if (matchL("[")) {
                idLista(node);
            }
        } else if (matchL("[")) {
            System.out.println(token);
            Node nodeLista = new Node("lista");
            node.addChild(nodeLista);
            nodeLista.enter = "{";
            getNextToken();

            Node nodeExpressaoP = new Node("item");
            nodeLista.addChild(nodeExpressaoP);
            expressao(nodeExpressaoP);

            while (!matchL("]")) {
                if (matchL(",")) {
                    getNextToken();
                } else {
                    break;
                }

                Node nodeExpressaoP2 = new Node("");
                nodeLista.addChild(nodeExpressaoP2);
                nodeExpressaoP2.enter = ",";
                expressao(nodeExpressaoP2);
            }

            if (matchL("]")) {
                node.exit = "}";
            } else {
                erro();
            }
        } else {
            erro();
        }
    }

    private void opPow(Node nodeExp, Node node) {
        if (Gramaticas.matchLex(token, "^")) {
            getNextToken();
            nodeExp.enter = "Math.pow(Double.valueOf(";
            Node newNode = new Node("),Double.valueOf(");
            node.addChild(newNode);
            nodeExp.exit = "))";
        }
    }

    public void opT(Node node) {
        String opTs[] = { "^", "*", "rst", "/", "mnr", "mar", "equ", "mnri", "mari" };
        String tr[] = { "^", "*", "%", "/", "<", ">", "==", "<=", ">=" };

        int index = -1;
        for (int i = 0; i < opTs.length; i++) {
            if (matchL(opTs[i])) {
                index = i;
                break;
            }
        }

        if (index > -1) {
            Node op = new Node(tr[index]);
            node.addChild(op);
            getNextToken();
        } else {
            erro();
        }
    }

    public void fator(Node node) {
        System.out.println(token);
        Node fator = new Node("");
        node.addChild(fator);

        if (matchT("ID") || matchL("[")) {
            idFator(fator);
        } else if (matchValor()) {
            valor(fator);
        } else if (matchL("(")) {
            getNextToken();

            Node expressao = new Node("expressao");
            expressao.enter = "(";
            node.addChild(expressao);

            expressao(expressao);
            if (matchL(")")) {
                getNextToken();
                expressao.exit = ")";
            } else {
                erro();
            }
        } else {
            erro();
        }
    }

    public void termoL(Node node) {
        if (matchOpT()) {
            if (Gramaticas.matchLex(token, "^")) {
                Node opPow = new Node("opPow");
                node.addChild(opPow);
                opPow(node, opPow);
            } else {
                opT(node);
            }

            fator(node);
            termoL(node);
        }
    }

    public void termo(Node node) {
        Node termo = new Node("termo");
        node.addChild(termo);

        fator(termo);
        termoL(termo);
    }

    public void expressaoL(Node node) {
        if (matchL("+") || matchL("-")) {
            Node expressaoL = new Node("expressaoL");
            node.addChild(expressaoL);

            expressaoL.enter = token.lexema;

            getNextToken();

            termo(expressaoL);
            expressaoL(expressaoL);
        }
    }

    public void expressao(Node node) {

        Node expressao = new Node("expressao");
        node.addChild(expressao);

        termo(expressao);
        expressaoL(expressao);
    }

    public void atribuicao(Node node) {
        idFator(node);
        if (matchL("->")) {
            Node op = new Node("=");
            node.addChild(op);
            getNextToken();

            expressao(node);
        } else {
            erro();
        }
    }

    public void declaracao(Node node) {
        tipo(node);

        Node nodeId = new Node("id");
        node.addChild(nodeId);
        idDec(nodeId);

        if (matchL("->")) {
            getNextToken();
            Node op = new Node("=");
            node.addChild(op);
            expressao(node);
        }
    }

    private void opL(Node node) {
        String opLs[] = { "mnr", "mar", "equ", "mnri", "mari" };
        String tr[] = { "<", ">", "==", "<=", ">=" };

        int index = -1;
        for (int i = 0; i < opLs.length; i++) {
            if (matchL(opLs[i])) {
                index = i;
                break;
            }
        }

        if (index > -1) {
            Node op = new Node(tr[index]);
            node.addChild(op);
            getNextToken();
        } else {
            erro();
        }
    }

    private void condicao(Node node) {
        Node expressao1 = new Node("expressao");
        node.addChild(expressao1);
        expressao(expressao1);
    }

    private void rtn(Node node) {
        if (matchL("rtn")) {
            node.enter = "return";
            getNextToken();
            Node nodeExpressao = new Node("expressao");
            node.addChild(nodeExpressao);
            expressao(nodeExpressao);
            node.exit = ";\n";
        }
    }

    private void bloco(Node node, boolean func) {
        if (matchL("{")) {
            Node bloco = new Node("bloco");
            node.addChild(bloco);
            bloco.enter = "{";
            getNextToken();

            if (matchL("}")) {
                bloco.data = "";
            }
            System.out.println(token);
            while (!matchL("}")) {
                if (matchL("rtn") && func) {
                    Node nodeReturn = new Node("return");
                    bloco.addChild(nodeReturn);
                    rtn(nodeReturn);
                } else {
                    Node nodeInst = new Node("instrucao");
                    bloco.addChild(nodeInst);
                    instrucao(nodeInst);
                }
            }

            if (matchL("}")) {
                getNextToken();
                bloco.exit = "}";
            }
        } else if (instrucao(node)) {

        } else if (matchL("rtn") && func) {
            Node bloco = new Node("bloco");
            node.addChild(bloco);
            rtn(bloco);
        } else {
            erro();
        }
    }

    private void elseM(Node node) {
        if (matchL("&")) {
            Node elseN = new Node("else");
            elseN.enter = "else";
            node.addChild(elseN);

            getNextToken();

            bloco(elseN, false);
        }
    }

    private void ifM(Node node) {
        node.enter = "if";
        getNextToken();
        if (matchL("<")) {
            getNextToken();
            Node condicao = new Node("condicao");
            condicao.enter = "(";
            node.addChild(condicao);
            condicao(condicao);

            if (matchL(">")) {
                condicao.exit = ")";
                getNextToken();
                bloco(node, false);
                elseM(node);
            } else {
                erro();
            }
        } else {
            erro();
        }
    }

    private void whileM(Node node) {
        getNextToken();
        if (matchL("<")) {
            getNextToken();
            node.enter = "while";

            Node condicao = new Node("condicao");
            node.addChild(condicao);
            condicao(condicao);

            if (matchL(">")) {
                getNextToken();

                Node bloco = new Node("bloco");
                node.addChild(bloco);
                bloco(bloco, false);
            } else {
                erro();
            }
        } else {
            erro();
        }
    }

    private void forAtribuicaoDeclaracao(Node node) {
        if (matchTipo()) {
            declaracao(node);
        } else if (matchId()) {
            atribuicao(node);
        } else {
            erro();
        }
        node.exit = ";\n";
    }

    private void forParametros(Node node) {
        node.enter = "(";
        Node nodeAtribFor = new Node("atribuicao for");
        node.addChild(nodeAtribFor);
        forAtribuicaoDeclaracao(nodeAtribFor);

        if (matchL("|")) {
            getNextToken();

            Node nodeCondicao = new Node("condicao");
            node.addChild(nodeCondicao);
            condicao(nodeCondicao);
            nodeCondicao.exit = ";";

            if (matchL("|")) {
                getNextToken();

                Node nodeAtribuicao = new Node("atribuicao");
                node.addChild(nodeAtribuicao);
                atribuicao(nodeAtribuicao);

                if (matchL(">")) {
                    getNextToken();
                } else {
                    erro();
                }
            } else {
                erro();
            }
        } else {
            erro();
        }
        node.exit = ")";
    }

    private void forM(Node node) {
        getNextToken();
        if (matchL("<")) {
            getNextToken();
            node.enter = "for";

            Node nodeParametros = new Node(" parametros for");
            node.addChild(nodeParametros);
            forParametros(nodeParametros);

            Node nodeBloco = new Node("bloco");
            node.addChild(nodeBloco);
            bloco(nodeBloco, false);
        } else {
            erro();
        }
    }

    private void comentario() {
        if (matchT("COMENTARIO")) {
            getNextToken();
        }
    }

    private void prt(Node node) {
        System.out.println(token);
        if (matchL("prt") || matchL("prtln")) {
            if (matchL("prt"))
                node.enter = "System.out.print(";
            else
                node.enter = "System.out.println(";

            getNextToken();

            if (matchL("<")) {
                getNextToken();

                Node nodeExpressao = new Node("expressao");
                node.addChild(nodeExpressao);
                expressao(nodeExpressao);

                if (matchL(">")) {
                    getNextToken();
                    node.exit = ");";
                } else {
                    erro();
                }
            } else {
                erro();
            }

        }
    }

    private void ent(Node node) {
        if (matchL("ent")) {
            String numScanner = token.linha + "" + token.coluna;
            node.enter = "Scanner sc" + numScanner + " = new Scanner(System.in);";
            getNextToken();

            if (matchL("<")) {
                getNextToken();

                Node nodeTipo = new Node("tipo");
                node.addChild(nodeTipo);
                tipo(nodeTipo);

                if (matchL(",")) {
                    getNextToken();
                    Node nodeId = new Node("id");
                    node.addChild(nodeId);
                    id(nodeId);
                    if (matchL(">")) {
                        getNextToken();
                    } else {
                        erro();
                    }
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

                node.exit += "();\nsc" + numScanner + ".close();\n";
            }
        } else {
            erro();
        }
    }

    private void func(Node node) {

        Node nodeFunc = new Node("func");
        getNextToken();
        nodeFunc.enter = "public static ";
        node.addChild(nodeFunc);

        // tipo do retorno
        if (matchTipo()) {
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
        if (matchL("<")) {
            getNextToken();

            nodeParams.enter = "(";

            if (matchTipo()) {
                Node nodeTipoP = new Node("id");
                nodeParams.addChild(nodeTipoP);
                tipo(nodeTipoP);

                Node nodeIdP = new Node("id");
                nodeParams.addChild(nodeIdP);
                id(nodeIdP);
                System.out.println(token);
                while (!matchL(">")) {
                    if (matchL(",")) {
                        getNextToken();
                    } else {
                        break;
                    }

                    Node nodeTipoP2 = new Node("tipo");
                    nodeTipoP2.enter = ",";
                    nodeParams.addChild(nodeTipoP2);
                    tipo(nodeTipoP2);
                    Node nodeIdP2 = new Node("id");
                    nodeParams.addChild(nodeIdP2);
                    id(nodeIdP2);
                }

                if (matchL(">")) {
                    getNextToken();
                    nodeParams.exit = ")";

                    Node nodeBloco = new Node("");
                    nodeFunc.addChild(nodeBloco);
                    bloco(nodeBloco, true);
                } else {
                    erro();
                }
            } else if (matchL(">")) {
                getNextToken();
                nodeParams.exit = ")";
                Node nodeBloco = new Node("");
                nodeFunc.addChild(nodeBloco);
                bloco(nodeBloco, true);
            } else {
                erro();
            }
        } else {
            erro();
        }
    }

    public Boolean instrucao(Node node) {
        if (matchId()) {
            Node newNode = new Node("atribuicao");
            node.addChild(newNode);
            atribuicao(newNode);
            newNode.exit += ";\n";
        } else if (matchTipo()) {
            Node newNode = new Node("declaracao");
            node.addChild(newNode);
            declaracao(newNode);
            newNode.exit += ";\n";
        } else if (Gramaticas.matchLex(token, "?")) {
            Node newNode = new Node("if");
            node.addChild(newNode);
            ifM(newNode);
        } else if (Gramaticas.matchLex(token, "loop")) {
            erro = false;
            Node newNode = new Node("while");
            node.addChild(newNode);
            whileM(newNode);
        } else if (Gramaticas.matchLex(token, "looplim")) {
            erro = false;
            Node newNode = new Node("for");
            node.addChild(newNode);
            forM(newNode);
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
        if (matchL("fnc")) {
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

        Node nodeMain = new Node("");
        nodeMain.enter = "\npublic static void main (String args[]){\n";
        nodeMain.exit = "\n}";
        nodeRoot.addChild(nodeMain);

        nodeRoot.exit = "\n}";
        tree = new Tree(nodeRoot);

        code(nodeClass, nodeMain);

        tree = new Tree(nodeRoot);

        // if (erro) {
        // tree.root = null;
        // }

        return tree;
    }
}
