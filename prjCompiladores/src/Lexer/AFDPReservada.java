package Lexer;

public class AFDPReservada {

    private int pos;
    private char atual;
    private String palavra;
    private int cont;

    public AFDPReservada(String palavra) {
        this.palavra = palavra;
    }

    public void avancar(String texto) {
        pos++;
        if (pos > (texto.length())) {
            this.atual = '@';
        } else {
            this.atual = texto.charAt(pos);
        }
    }

    public Boolean processa(int pos, String texto) {
        this.pos = pos;
        this.atual = texto.charAt(pos);
        this.cont = 0;

        while (this.atual != '@') {
            if (cont > palavra.length() - 1) {
                return true;
            }

            if (this.atual != this.palavra.charAt(cont)) {
                return false;
            }
            cont++;
            avancar(texto);

        }

        return true;
    }
}
