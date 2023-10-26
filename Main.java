import java.util.Scanner;
class Main {
public static  int  fatorial ( int  num ){if( num  ==  1 ){return 1 ;
}return num  *  fatorial ( num - 1 );
}
public static void main (String args[]){
System.out.print( "Digite um numero: " );Scanner sc102 = new Scanner(System.in); int  num  = sc102.nextInt();
sc102.close();
System.out.println(  "");System.out.println( "Fatorial de " + num + ": " + fatorial ( num ));
}
}