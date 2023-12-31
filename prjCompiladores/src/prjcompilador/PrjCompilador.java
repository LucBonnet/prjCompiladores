package prjcompilador;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
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
        Path root = FileSystems.getDefault().getPath("").toAbsolutePath();
        String pathFile = Paths.get(root.toString(), "").toAbsolutePath().toString() + "\\arquivo.txt";
        try (BufferedReader buffRead = new BufferedReader(new FileReader(pathFile))) {
            String linha = "";
            while ((linha = buffRead.readLine()) != null) {
                texto += linha + "\n";
            }
        } catch (IOException exception) {
            System.out.println("Erro ao ler o arquivo");
            System.out.println(exception);
        }

        if (texto.equals("")) {
            System.out.println("Arquivo vazio");
            return;
        }

        List<Token> tokens = new ArrayList<>();

        // Analisador Lexico
        Lexer lexer = new Lexer(texto);
        tokens = lexer.getTokens();

        // for (Token token : tokens) {
        // System.out.println(token);
        // }

        // Analaisador Sintático
        Parser parser = new Parser(tokens);
        Tree tree = parser.main();
        tree.walkPrint(tree.root);

        // tree.print(tree.root);
        // System.out.println("\n\n\n\n");
        String code = tree.code;

        // Criação do arquivo Main.java
        if (code.equals(""))
            return;

        String pathFileOutput = System.getProperty("user.dir") + "./Main.java";
        try {
            FileWriter myWriter = new FileWriter(pathFileOutput);
            myWriter.write(code);
            myWriter.close();
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }
}
