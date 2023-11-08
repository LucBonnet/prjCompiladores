import java.util.Scanner;
class Main {
  
public static void main (String args[]){
 double  notas [] = { 0 , 0 , 0 , 0 };
 double  soma  = (double) 0 ;
for( int  i  = (int) 0 ;
 i  <  4 ; i  =  i + 1 ){System.out.println( "Ditgite a nota " +( i + 1 )+ ":" );Scanner sc64 = new Scanner(System.in);   notas [ i ] = sc64.nextDouble();
 soma  = (double) soma + notas [ i ];
} double  media  = (double)( soma  /  4 );
System.out.println( "Media das notas: " + media );if( media  >=  5 ){System.out.println( "Aprovado" );}else {System.out.println( "Reprovado" );}
}
}