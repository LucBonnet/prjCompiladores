@echo off
copy /y .\arquivo.txt .\prjCompiladores\dist\ > NUL
cd prjCompiladores/dist

rm -f Main.java 

java -jar ./prjCompilador.jar

if exist ./Main.java (
  java Main.java    
)
