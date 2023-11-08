import java.util.Scanner;
class Main {
public static void quocienteResto ( double  dividendo , double  divisor ){ double  vezes  = (double) 0 ;
while( dividendo  >=  divisor ){ vezes  = (double) vezes + divisor ;
 dividendo  = (double) dividendo - divisor ;
}System.out.println( "Quociente: " + vezes );System.out.println( "Resto: " + dividendo );}
public static void main (String args[]){
 double  dividendo ;
 double  divisor ;
System.out.print( "Digite o dividendo: " );Scanner sc172 = new Scanner(System.in);   dividendo  = sc172.nextDouble();
System.out.print( "Digite o divisor: " );Scanner sc202 = new Scanner(System.in);   divisor  = sc202.nextDouble();
System.out.println( "" ); quocienteResto ( dividendo , divisor );

}
}