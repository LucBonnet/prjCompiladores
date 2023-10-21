package Parser;

import java.util.List;

import Utils.Token;
import java.util.ArrayList;

public class Parser {

    private List<IParser> parsers;
    List<Token> tokens;
    Token token;

    public Parser(List<Token> tokens) {
        this.tokens = tokens;
    }

    public void main() {
        if(token == null) return;
        
        String tipos[] = {"int", "dec", "txt", "lgc", "ltr"};
        Boolean atribuicao = false;
        for(String tipo : tipos) {
            if(token.lexema.equals(tipo)) {
                atribuicao = true;
                break;
            }
        } 
        
        if(atribuicao) {
           (new Atribuicao(tokens)).main();
        } else if(false) {
            
        } else {
            return;
        }
        
        main();
    }
}
