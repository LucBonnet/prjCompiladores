package Lexer;

import Lexer.AFDs.*;
import java.util.ArrayList;
import java.util.List;

import Utils.Token;
import Utils.InstanceAFDS;

public class Lexer {

    private String texto;
    private int pos;
    private int line;
    private int col;
    private char atual;
    private List<AFD> afds;

    public Lexer(String texto) {
        this.texto = texto;
        this.pos = 0;
        this.atual = texto.charAt(pos);

        this.afds = new ArrayList<>();
        
        this.afds.add(new ResComentario());
        this.afds.add(new ResDec());
        this.afds.add(new ResEnt());
        this.afds.add(new ResFalse());
        this.afds.add(new ResFunc());
        this.afds.add(new ResInt());
        this.afds.add(new ResLetra());
        this.afds.add(new ResLogic());
        this.afds.add(new ResLoopLim());
        this.afds.add(new ResLoop());
        this.afds.add(new ResPrtln());
        this.afds.add(new ResPrt());
        this.afds.add(new ResReturn());
        this.afds.add(new ResText());
        this.afds.add(new ResTrue());
        this.afds.add(new OpCDiferente());
        this.afds.add(new OpCIgual());
        this.afds.add(new OpCMaiorIgual());
        this.afds.add(new OpCMaior());
        this.afds.add(new OpCMenorIgual());
        this.afds.add(new OpCMenor());
        this.afds.add(new OpComEqu());
        this.afds.add(new OpResto());
        this.afds.add(new Atribuicao());
        this.afds.add(new Char());
        this.afds.add(new Dec());
        this.afds.add(new Div());
        this.afds.add(new Else());
        this.afds.add(new ID());
        this.afds.add(new If());
        this.afds.add(new LChave());
        this.afds.add(new Int());
        this.afds.add(new LColchete());
        this.afds.add(new LLSeparator());
        this.afds.add(new LParen());
        this.afds.add(new LParenS());
        this.afds.add(new Mul());
        this.afds.add(new Pow());
        this.afds.add(new RChave());
        this.afds.add(new RColchete());
        this.afds.add(new RParen());
        this.afds.add(new RParenS());
        this.afds.add(new Soma());
        this.afds.add(new Sub());
        this.afds.add(new Text());
        this.afds.add(new Virgula());
    }

    public void error() {
        // throw new RuntimeException("Caractere Inválido\nLinha: " + (this.line + 1) +
        // "\nColuna: " + col);
        System.out
                .println("Caractere Inválido: Linha: " + (this.line + 1) + "; Coluna: " + col + "; Caractere: " + this.atual);
        avancar(1);
    }

    public void avancar(int qtde) {
        pos += qtde;
        col += qtde;
        if (pos > (texto.length() - 1)) {
            this.atual = '@';
        } else {
            this.atual = texto.charAt(this.pos);
        }

        if (this.atual == '\n') {
            line++;
            col = 0;
        }
    }

    public void pulaEspacoBranco() {
        while (this.atual != '@' && (this.atual == ' ' || this.atual == '\n')) {
            avancar(1);
        }
    }

    public Token proximoToken() {
        while (this.atual != '@') {
            if (this.atual == ' ' || this.atual == '\n') {
                pulaEspacoBranco();
                continue;
            }

            for (AFD afd : afds) {
                Token reconhecido = afd.processa(pos, texto);

                if (reconhecido != null) {
                    reconhecido.setLinhaColuna(line, col);
                    avancar(reconhecido.getLength());
                    return reconhecido;
                }
            }
            error();
        }
        return new Token("EOF", "@");
    }

    public List<Token> getTokens() {
        List<Token> tokens = new ArrayList<>();

        try {
            Token token = proximoToken();

            while (!token.getTipo().equals("EOF")) {
                tokens.add(token);
                token = proximoToken();
            }
        } catch (RuntimeException re) {
            System.out.println("Erro: " + re.getMessage());
            re.printStackTrace();
        }

        return tokens;
    }
}
