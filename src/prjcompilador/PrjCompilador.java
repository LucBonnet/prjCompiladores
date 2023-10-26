package prjcompilador;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import Lexer.Lexer;
import Parser.Parser;
import Translator.Tree;
import Utils.Token;

public class PrjCompilador {

    public static void main(String[] args) {
        // Leitura do arquivo
        String texto = "";
        String pathFile = System.getProperty("user.dir") + "/src/prjcompilador/arquivo.txt";
        try (BufferedReader buffRead = new BufferedReader(new FileReader(pathFile))) {
            String linha = "";
            while ((linha = buffRead.readLine()) != null) {
                texto += linha + "\n";
            }
        } catch (IOException exception) {
            System.out.println("Erro ao ler o arquivo");
            System.out.println(exception);
        }

        List<Token> tokens = new ArrayList<>();

        // Analisador Lexico
        Lexer lexer = new Lexer(texto);
        tokens = lexer.getTokens();

        for (Token token : tokens) {
            System.out.println(token);
        }

        // Analaisador Sintático

        Parser parser = new Parser(tokens);
        Tree tree = parser.main();
        tree.walk(tree.root);

        tree.print(tree.root);
        System.out.println("\n\n\n\n");
        String code = tree.code;

        System.out.println(code);

        // Analaisador Semântico

        // Criação do arquivo Main.java
        String pathFileOutput = System.getProperty("user.dir") + "./Main.java";
        try {
            FileWriter myWriter = new FileWriter(pathFileOutput);
            myWriter.write(code);
            myWriter.close();
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }

        // Execução do código
        // try {
        // Process proc = Runtime.getRuntime().exec("java Main.java");

        // BufferedReader reader = new BufferedReader(new
        // InputStreamReader(proc.getInputStream()));

        // String linha = "";
        // while ((linha = reader.readLine()) != null) {
        // System.out.print(linha + "\n");
        // }

        // proc.waitFor();
        // } catch (IOException e) {
        // e.printStackTrace();
        // } catch (InterruptedException e) {
        // e.printStackTrace();
        // }

    }

}
