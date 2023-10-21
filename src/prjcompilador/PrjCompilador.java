package prjcompilador;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import Lexer.Lexer;
import Parser.Parser;
import Utils.Token;

public class PrjCompilador {

    public static void main(String[] args) {
        // Leitura do arquivo
        String texto = "";
        try (BufferedReader buffRead = new BufferedReader(new FileReader(
                System.getProperty("user.dir") + "/src/prjcompilador/arquivo.txt"))) {
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
        System.out.println("Lexer>: ");
        Lexer lexer = new Lexer(texto);
        tokens = lexer.getTokens();

//         for (Token token : tokens) {
//         System.out.println(token);
//         }

        // Analaisador Sintático
        Parser parser = new Parser(tokens);
        parser.main();
        
        // Analaisador Semântico

    }

}
