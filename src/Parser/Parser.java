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

    private void condicao() {
        if (Gramaticas.matchTipo(token, "ID")) {
            token = getNextToken();
            if (Gramaticas.opL(token)) {
                token = getNextToken();
                if (Gramaticas.matchTipo(token, "ID")) {
                    token = getNextToken();
                } else {
                    expressao();
                }
            }
        } else {
            expressao();
            if (Gramaticas.opL(token)) {
                token = getNextToken();
                if (Gramaticas.matchTipo(token, "ID")) {
                    token = getNextToken();
                } else {
                    expressao();
                }
            } else {
                erro(token);
            }
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
        }
    }

    private void declaracao() {
        if (Gramaticas.tipo(token)) {
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
    }

    private void elseMain() {
        if (Gramaticas.matchLex(token, "&")) {
            token = getNextToken();
            if (Gramaticas.matchLex(token, "{")) {
                token = getNextToken();

                while (!Gramaticas.matchLex(token, "}")) {
                    instrucao();
                }

                if (Gramaticas.matchLex(token, "}")) {
                    token = getNextToken();
                } else {
                    erro(token);
                }
            } else {
                erro(token);
            }
        }
    }

    private void ifMain() {
        if (Gramaticas.matchLex(token, "?")) {
            // ?
            token = getNextToken();
            if (Gramaticas.matchLex(token, "<")) {
                // <
                token = getNextToken();
                condicao();
                if (Gramaticas.matchLex(token, ">")) {
                    token = getNextToken();
                    if (Gramaticas.matchLex(token, "{")) {
                        token = getNextToken();

                        while (!Gramaticas.matchLex(token, "}")) {
                            instrucao();
                        }

                        if (Gramaticas.matchLex(token, "}")) {
                            token = getNextToken();
                            elseMain();
                        } else {
                            erro(token);
                        }
                    } else {
                        erro(token);
                    }
                } else {
                    erro(token);
                }
            } else {
                erro(token);
            }
        }
    }

    private void whileMain() {
        if (Gramaticas.matchLex(token, "loop")) {
            token = getNextToken();
            if (Gramaticas.matchLex(token, "<")) {
                token = getNextToken();
                condicao();
                if (Gramaticas.matchLex(token, ">")) {
                    token = getNextToken();
                    if (Gramaticas.matchLex(token, "{")) {
                        token = getNextToken();

                        while (!Gramaticas.matchLex(token, "}")) {
                            instrucao();
                        }

                        if (Gramaticas.matchLex(token, "}")) {
                            token = getNextToken();
                        } else {
                            erro(token);
                        }
                    } else {
                        erro(token);
                    }
                } else {
                    erro(token);
                }
            } else {
                erro(token);
            }
        }
    }

    private void forMain() {
        if (Gramaticas.matchLex(token, "looplim")) {
            token = getNextToken();
            if (Gramaticas.matchLex(token, "<")) {
                token = getNextToken();
                if (Gramaticas.tipo(token)) {
                    declaracao();
                } else if (Gramaticas.id(token)) {
                    atribuicao();
                } else {
                    erro(token);
                }

                if (Gramaticas.matchLex(token, "|")) {
                    token = getNextToken();
                    condicao();
                    if (Gramaticas.matchLex(token, "|")) {
                        token = getNextToken();
                        atribuicao();
                        if (Gramaticas.matchLex(token, ">")) {
                            token = getNextToken();
                            if (Gramaticas.matchLex(token, "{")) {
                                token = getNextToken();

                                while (!Gramaticas.matchLex(token, "}")) {
                                    instrucao();
                                }

                                if (Gramaticas.matchLex(token, "}")) {
                                    token = getNextToken();
                                } else {
                                    erro(token);
                                }
                            } else {
                                erro(token);
                            }
                        } else {
                            erro(token);
                        }
                    } else {
                        erro(token);
                    }
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
            if (Gramaticas.matchLex(token, "<")) {
                token = getNextToken();
                expressao();
                if (Gramaticas.matchLex(token, ">")) {
                    token = getNextToken();
                } else {
                    erro(token);
                }
            } else {
                erro(token);
            }
        }
    }

    private void ent() {
        if (Gramaticas.matchLex(token, "ent")) {
            token = getNextToken();
            if (Gramaticas.matchLex(token, "<")) {
                token = getNextToken();
                if (Gramaticas.tipo(token)) {
                    token = getNextToken();
                    if (Gramaticas.matchLex(token, ">")) {
                        token = getNextToken();
                    } else {
                        erro(token);
                    }
                } else {
                    erro(token);
                }
            } else {
                erro(token);
            }
        }
    }

    public void instrucao() {
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
            return;
        }
    }

    public void main() {
        instrucao();

        if (tokens.size() > 0) {
            main();
        }
    }
}
