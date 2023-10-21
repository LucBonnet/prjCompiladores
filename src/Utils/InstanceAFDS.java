package Utils;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;

import Lexer.AFD;

public class InstanceAFDS {
  public static List<AFD> getAFDs() {
    List<AFD> afds = new ArrayList<>();
    List<String> classesNomes = getClassesNomes();

    for (String className : classesNomes) {
      try {
        Class<?> classe = Class.forName(className);
        Constructor<?> c = classe.getConstructor();
        afds.add((AFD) c.newInstance());
      } catch (Exception e) {
        System.out.println(e);
      }
    }

    return afds;
  }

  private static List<String> getClassesNomes() {
    List<String> classesNomes = new ArrayList<>();

    String packageName = "Lexer.AFDs";
    InputStream stream = ClassLoader.getSystemClassLoader()
        .getResourceAsStream(packageName.replaceAll("[.]", "/"));
    try (BufferedReader buffRead = new BufferedReader(new InputStreamReader(stream))) {
      String linha = "";
      while ((linha = buffRead.readLine()) != null) {
        if (linha.contains("Res") || linha.contains("Op"))
          classesNomes.add(0, packageName + "." + linha.replace(".class", ""));
        else
          classesNomes.add(packageName + "." + linha.replace(".class", ""));
      }
    } catch (Exception e) {
      System.out.println("Erro ao instanciar as classes\n" + e);
    }

    return classesNomes;
  }
}
