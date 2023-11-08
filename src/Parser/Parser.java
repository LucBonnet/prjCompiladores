package Parser;

import Semantico.IdHash;
import Semantico.Utils;

import java.util.ArrayList;
import java.util.List;

import Translator.Node;
import Translator.Tree;
import Utils.Token;

public class Parser {

    boolean erro;
    int numErros;
    List<Token> tokens;
    Token token;
    Token tokenAnterior;
    Tree tree;
    IdHash hash;

    public Parser(List<Token> tokens) {
        this.tokens = tokens;
        this.hash = new IdHash();
        this.erro = false;
        this.numErros = 0;

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
        numErros++;

        erro = true;
        if (this.token == null) {
            System.out.println("Erro: " + tokenAnterior);
        } else {
            System.out.println("Erro: " + this.token);
            System.out.println("Linha: " + this.token.linha);
            System.out.println("Coluna: " + this.token.coluna);
            System.out.println("");
        }

        getNextToken();

        if (this.token == null) {
            System.exit(0);
        }
    }

    private void erro(String msg, Token token) {
        if (erro) {
            getNextToken();
            return;
        }
        numErros++;

        erro = true;
        if (token == null) {
            System.out.println("Erro: " + tokenAnterior);
        } else {
            System.out.println("Erro: " + token);
            System.out.println("Linha: " + token.linha);
            System.out.println("Coluna: " + token.coluna);
            System.out.println(msg);
            System.out.println("");
        }

        if (token == null) {
            System.exit(0);
        }
    }

    private boolean matchL(String lexema) {
        if (token == null) {
            return false;
        }

        if (token.lexema.equals(lexema)) {
            return true;
        }

        return false;
    }

    private boolean matchT(String tipo) {
        if (token == null) {
            return false;
        }

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
            String data = token.lexema;
            if (token.lexema.equals("fls")) {
                data = "false";
            } else if (token.lexema.equals("ver")) {
                data = "true";
            }
            Node valor = new Node(data);
            node.addChild(valor);
            valor.token = token;
            getNextToken();
        } else {
            erro();
        }
    }

    private void id(Node node, boolean isDec, boolean isAtrib) {

        if (matchT("ID")) {
            if (!isDec) {
                if (!hash.itemExists(token.lexema)) {
                    erro("Variável \"" + token.lexema + "\" não declarada", token);
                } else if (!isAtrib) {
                    if (!hash.getItem(token.lexema).valor) {
                        erro("Variável \"" + token.lexema + "\" não instanciada", token);
                    }
                }
            }

            Node id = new Node(token.lexema);
            id.token = this.token;
            node.addChild(id);
            getNextToken();
        } else {
            erro();
        }
    }

    private void idDec(Node node) {
        Node idDeclaracao = new Node("idDeclaracao");
        node.addChild(idDeclaracao);
        id(idDeclaracao, true, true);

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
            id(node, false, true);
            if (matchL("[")) {
                idLista(node);
            }
        } else if (matchL("[")) {
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

                Node nodeExpressaoP2 = new Node("expressao");
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
        if (matchL("^")) {
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
            if (tr[index].equals("==")) {
                Node id = node.getFirstLeaf(node);
                String tipo = hash.getItem(id.data).tipo;
                if (tipo == "String" || id.data.contains('"' + "")) {
                    node.enter = "(" + node.enter;
                    op.data = ").equals(";
                    node.exit += ")";
                }
            }
            node.addChild(op);
            op.token = token;
            getNextToken();
        } else {
            erro();
        }

    }

    public void fator(Node node) {
        Node fator = new Node("fator");
        node.addChild(fator);
        System.out.println(token);
        if (matchT("ID")) {
            Node nodeId = new Node("id");
            fator.addChild(nodeId);
            id(nodeId, false, false);

            if (matchL("<")) {
                chamadaFuncao(nodeId);
            } else {
                idLista(nodeId);
            }
        } else if (matchValor()) {
            valor(fator);
        } else if (matchL("(")) {
            getNextToken();

            Node expressao = new Node("expressao");
            expressao.enter = "(";
            fator.addChild(expressao);

            expressao(expressao);
            if (matchL(")")) {
                getNextToken();
                expressao.exit = ")";
            } else {
                erro();
            }
        } else if (matchL("[")) {
            getNextToken();

            Node expressao = new Node("expressao");
            expressao.enter = "{";
            fator.addChild(expressao);
            expressao(expressao);

            while (matchL(",")) {
                getNextToken();

                Node expressao1 = new Node("expressao");
                expressao1.enter = ",";
                fator.addChild(expressao1);
                expressao(expressao1);
            }

            if (matchL("]")) {
                fator.exit = "}";
                getNextToken();
            } else {
                erro();
            }

        } else {
            erro();
        }
    }

    public void termoL(Node node) {
        if (matchOpT()) {
            if (matchL("^")) {
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
        termo(node);
        expressaoL(node);
    }

    public boolean verTipoDeclaracao(ArrayList<Token> termos, String tipo) {
        switch (tipo) {
            case "int": {
                for (Token termo : termos) {
                    if (termo.tipo.equals("TEXT") ||
                            termo.tipo.equals("DEC") ||
                            termo.tipo.equals("CHAR") ||
                            termo.tipo.equals("RES_FLS") ||
                            termo.tipo.equals("RES_VER")) {
                        return false;
                    }
                }
                break;
            }
            case "double": {
                for (Token termo : termos) {
                    if (termo.tipo.equals("TEXT") ||
                            termo.tipo.equals("CHAR") ||
                            termo.tipo.equals("RES_FLS") ||
                            termo.tipo.equals("RES_VER")) {
                        return false;
                    }
                }
                break;
            }
            case "String": {
                int numStrings = 0;
                for (Token termo : termos) {
                    if (termo.tipo.equals("TEXT")) {
                        numStrings++;
                    }
                }

                if (numStrings == 0) {
                    return false;
                }
                break;
            }
            case "char": {
                if (termos.size() > 1) {
                    return false;
                }
                for (Token termo : termos) {
                    if (termo.tipo.equals("TEXT") ||
                            termo.tipo.equals("DEC") ||
                            termo.tipo.equals("INT") ||
                            termo.tipo.equals("RES_FLS") ||
                            termo.tipo.equals("RES_VER")) {
                        return false;
                    }
                }
                break;
            }
            case "boolean": {
                for (Token termo : termos) {
                    if (termo.tipo.equals("TEXT") ||
                            termo.tipo.equals("DEC") ||
                            termo.tipo.equals("INT") ||
                            termo.tipo.equals("CHAR")) {
                        return false;
                    }
                }
                break;
            }
            case "opL": {
                for (Token termo : termos) {
                    if (termo.lexema.equals("equ") ||
                            termo.lexema.equals("mnr") ||
                            termo.lexema.equals("mar") ||
                            termo.lexema.equals("mari") ||
                            termo.lexema.equals("mnri")) {
                        return true;
                    }
                }
                return false;
            }
        }

        return true;
    }

    public void atribuicao(Node node, Node nodeId) {
        if (nodeId == null) {
            Node id = new Node("id");
            node.addChild(id);
            idFator(id);

            if (matchL("->")) {
                Node op = new Node("=");
                node.addChild(op);
                getNextToken();

                Node expressao = new Node("expressao");
                node.addChild(expressao);
                expressao(expressao);

                ArrayList<Token> termos = new ArrayList<>();
                expressao.getLeafs(termos);

                String tipo = hash.getItem(id.getFirstLeaf(id).data).tipo;

                if (!verTipoDeclaracao(termos, tipo)) {
                    erro("Tipagem incorreta", termos.get(0));
                }

                hash.getItem(id.getFirstLeaf(id).data).valor = true;
            } else {
                erro();
            }
        } else if (matchL("->")) {
            Node op = new Node("=");
            node.addChild(op);
            getNextToken();

            String tipo = hash.getItem(nodeId.getFirstLeaf(nodeId).data).tipo;

            if (!matchL("["))
                op.exit += "(" + tipo + ")";

            Node expressao = new Node("expressao");
            node.addChild(expressao);
            expressao(expressao);

            ArrayList<Token> termos = new ArrayList<>();
            expressao.getLeafs(termos);

            if (!verTipoDeclaracao(termos, tipo)) {
                erro("Tipagem incorreta", termos.get(0));
            }

            hash.getItem(nodeId.getFirstLeaf(nodeId).data).valor = true;
        } else if (matchL("[")) {
            idLista(nodeId);

            if (matchL("->")) {
                Node op = new Node("=");
                node.addChild(op);
                getNextToken();

                expressao(node);

                hash.getItem(nodeId.getFirstLeaf(nodeId).data).valor = true;
            } else {
                erro();
            }
        }
    }

    public void declaracao(Node node) {
        Node nodeTipo = new Node("tipo");
        node.addChild(nodeTipo);
        tipo(nodeTipo);

        Node nodeId = new Node("id");
        node.addChild(nodeId);
        idDec(nodeId);

        Token tk = Utils.addHash(nodeTipo, nodeId, hash, false);
        if (tk != null) {
            erro("Variável \"" + tk.lexema + "\" já declarada", tk);
        }

        atribuicao(node, nodeId);

        if (matchL("<")) {
            erro();
        }
    }

    private void condicao(Node node) {
        Node expressao = new Node("expressao");
        node.addChild(expressao);
        System.out.println(token);
        expressao(expressao);

        // int controle = expressao.IsString();
        // if (controle > 0) {
        // node.enter += "Boolean.parseBoolean(";
        // node.exit += ")";
        // } else if (controle == 0) {
        // node.enter += "(";
        // node.exit += ") == 0 ? false : true";
        // }

        ArrayList<Token> termos = new ArrayList<>();
        expressao.getLeafs(termos);
        if (verTipoDeclaracao(termos, "opL")) {

        } else if (verTipoDeclaracao(termos, "int") || verTipoDeclaracao(termos, "double")) {
            node.enter += "(";
            node.exit += ") == 0 ? false : true";
        } else if (verTipoDeclaracao(termos, "String")) {
            node.enter += "Boolean.parseBoolean(";
            node.exit += ")";
        } else if (verTipoDeclaracao(termos, "char")) {
            node.enter += "Boolean.parseBoolean(";
            node.exit += "+ \"\")";
        }
    }

    private void rtn(Node node) {
        if (matchL("rtn")) {
            node.enter = "return ";
            getNextToken();
            Node nodeExpressao = new Node("expressao");
            node.addChild(nodeExpressao);
            expressao(nodeExpressao);
            node.exit = ";\n";
        }
    }

    private void bloco(Node node) {
        Node bloco = new Node("bloco");
        node.addChild(bloco);
        if (matchL("{")) {
            bloco.enter = "{";
            getNextToken();

            if (matchL("}")) {
                bloco.data = "";
            }
            while (!matchL("}")) {
                Node nodeInst = new Node("instrucao");
                bloco.addChild(nodeInst);
                instrucao(nodeInst);
            }

            if (matchL("}")) {
                getNextToken();
                bloco.exit = "}";
            }
        } else {
            instrucao(bloco);
        }
    }

    private void elseM(Node node) {
        if (matchL("&")) {
            Node elseN = new Node("else");
            elseN.enter = "else ";
            node.addChild(elseN);

            getNextToken();

            bloco(elseN);
        }
    }

    private void ifM(Node node) {
        node.enter = "if";
        getNextToken();
        if (matchL("<")) {
            getNextToken();
            Node condicao = new Node("condicao");
            condicao.enter += "(";
            node.addChild(condicao);
            condicao(condicao);

            if (matchL(">")) {
                condicao.exit += ")";
                getNextToken();
                bloco(node);
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
            condicao.enter = "(";
            condicao(condicao);

            if (matchL(">")) {
                condicao.exit = ")";
                getNextToken();

                Node bloco = new Node("bloco");
                node.addChild(bloco);
                bloco(bloco);
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
            atribuicao(node, null);
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
                atribuicao(nodeAtribuicao, null);

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
            bloco(nodeBloco);
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
        if (matchL("prt") || matchL("prtln")) {
            if (matchL("prt")) {
                node.enter = "System.out.print(";
            } else {
                node.enter = "System.out.println(";
            }

            getNextToken();

            if (matchL("<")) {
                getNextToken();

                if (matchL(">")) {
                    getNextToken();
                    node.data = "";
                    node.exit = "\"\");";
                } else {
                    Node nodeExpressao = new Node("expressao");
                    node.addChild(nodeExpressao);
                    expressao(nodeExpressao);

                    if (matchL(">")) {
                        getNextToken();
                        node.exit = ");";
                    } else {
                        erro();
                    }
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
                    id(nodeId, false, true);

                    hash.getItem(nodeId.getFirstLeaf(nodeId).data).valor = true;

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

                node.exit += "();\n";
                nodeTipo.getFirstLeaf(nodeTipo).data = "";
            }
        } else {
            erro();
        }
    }

    private void func(Node node) {
        ArrayList<String> params = new ArrayList<>();

        Node nodeFunc = new Node("func");
        getNextToken();
        nodeFunc.enter = "public static ";
        node.addChild(nodeFunc);

        // tipo do retorno
        String strTipo = "";
        if (matchTipo()) {
            Node nodeTipo = new Node("tipo");
            nodeFunc.addChild(nodeTipo);
            tipo(nodeTipo);

            Node temp = nodeTipo;
            while (temp.getChildren().size() > 0) {
                temp = temp.getChild(0);
            }
            strTipo = temp.data;
        } else {
            nodeFunc.enter += "void";

            strTipo = "void";
        }

        // nome da função
        Node nodeId = new Node("id");
        nodeFunc.addChild(nodeId);
        id(nodeId, true, true);

        String strIdName = "";
        Node temp = nodeId;
        while (temp.getChildren().size() > 0) {
            temp = temp.getChild(0);
        }
        strIdName = temp.data;

        if (!hash.itemExists(strIdName)) {
            hash.addItem(strIdName, true, strTipo);
        } else {
            erro("Variável \"" + strIdName + "\" já declarada", temp.token);
        }

        Node nodeParams = new Node("params");
        nodeFunc.addChild(nodeParams);
        if (matchL("<")) {
            getNextToken();

            nodeParams.enter = "(";

            if (matchTipo()) {
                Node nodeTipoP = new Node("tipo");
                nodeParams.addChild(nodeTipoP);
                tipo(nodeTipoP);

                Node nodeIdP = new Node("id");
                nodeParams.addChild(nodeIdP);
                id(nodeIdP, true, true);
                params.add(nodeId.getFirstLeaf(nodeIdP).data);

                Token tk = Utils.addHash(nodeTipoP, nodeIdP, hash, true);
                if (tk != null) {
                    erro("Variável \"" + tk.lexema + "\" já declarada", tk);
                }

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
                    id(nodeIdP2, true, true);
                    params.add(nodeId.getFirstLeaf(nodeIdP2).data);

                    tk = Utils.addHash(nodeTipoP2, nodeIdP2, hash, true);
                    if (tk != null) {
                        erro("Variável \"" + tk.lexema + "\" já declarada", tk);
                    }
                }

                if (matchL(">")) {
                    getNextToken();
                    nodeParams.exit = ")";

                    bloco(nodeFunc);

                    for (String param : params) {
                        hash.tblHash.remove(param);
                    }
                } else {
                    erro();
                }
            } else if (matchL(">")) {
                getNextToken();
                nodeParams.exit = ")";
                Node nodeBloco = new Node("");
                nodeFunc.addChild(nodeBloco);
                bloco(nodeBloco);
            } else {
                erro();
            }
        } else {
            erro();
        }
    }

    public void chamadaFuncao(Node node) {
        if (matchL("<")) {
            getNextToken();
            Node param = new Node("params");
            node.addChild(param);
            param.enter = "(";

            if (matchL(">")) {
                param.data = ")";
            } else {
                Node nodeP = new Node("param" + 1);
                param.addChild(nodeP);
                expressao(nodeP);

                int i = 2;
                while (!matchL(">")) {
                    if (matchL(",")) {
                        getNextToken();
                    } else {
                        break;
                    }

                    Node nodeP2 = new Node("param" + i);
                    param.addChild(nodeP2);
                    nodeP2.enter = ",";
                    expressao(nodeP2);
                    i++;
                }

                if (matchL(">")) {
                    getNextToken();
                    param.exit = ")";
                } else {
                    erro();
                }
            }
        } else {
            erro();
        }
    }

    public Boolean instrucao(Node node) {
        if (matchId()) {
            erro = false;
            Node newNode = new Node(""); // atribuicao ou chamada de funcao
            node.addChild(newNode);

            Node nodeId = new Node("id");
            newNode.addChild(nodeId);
            id(nodeId, false, true);

            if (matchL("->") || matchL("[")) {
                newNode.data = "atribuicao";
                atribuicao(newNode, nodeId);
            } else if (matchL("<")) {
                newNode.data = "chamada funcao";
                chamadaFuncao(newNode);
            } else {
                erro();
            }

            newNode.exit += ";\n";
        } else if (matchTipo()) {
            erro = false;
            Node newNode = new Node("declaracao");
            node.addChild(newNode);
            declaracao(newNode);
            newNode.exit += ";\n";
        } else if (matchL("?")) {
            erro = false;
            Node newNode = new Node("if");
            node.addChild(newNode);
            ifM(newNode);
        } else if (matchL("loop")) {
            erro = false;
            Node newNode = new Node("while");
            node.addChild(newNode);
            whileM(newNode);
        } else if (matchL("looplim")) {
            erro = false;
            Node newNode = new Node("for");
            node.addChild(newNode);
            forM(newNode);
        } else if (matchL("prt") || matchL("prtln")) {
            erro = false;
            Node newNode = new Node("prt");
            node.addChild(newNode);
            prt(newNode);
        } else if (matchL("ent")) {
            erro = false;
            Node newNode = new Node("ent");
            node.addChild(newNode);
            ent(newNode);
        } else if (matchT("COMENTARIO")) {
            comentario();
        } else if (matchL("rtn")) {
            erro = false;
            Node newNode = new Node("rtn");
            node.addChild(newNode);
            rtn(newNode);
        } else {
            node.data = "";
            erro();
            return false;
        }

        return true;
    }

    public void code(Node nodeClass, Node nodeMain) {
        if (matchL("fnc")) {
            func(nodeClass);
            if (!nodeMain.IsLeaf()) {
                nodeClass.data = "classe escopo";
            }
        } else {
            instrucao(nodeMain);
            if (!nodeMain.IsLeaf()) {
                nodeMain.data = "main";
            }
        }

        if (tokens.size() > 0 || token != null) {
            code(nodeClass, nodeMain);
        }
    }

    public Tree main() {
        Node nodeRoot = new Node("programa");

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

        // System.out.println("===============");
        // hash.print();
        // System.out.println("===============");

        if (numErros > 0) {
            tree.root = null;
        }

        return tree;
    }
}
