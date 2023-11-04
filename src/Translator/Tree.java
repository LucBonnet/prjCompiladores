package Translator;

public class Tree {
  public Node root;
  public String code;

  public Tree(Node root) {
    this.root = root;
    this.code = "";
  }

  public void print(Node node) {
    if (node == null)
      return;

    simpleWalk(node, 0);
    System.out.println("");
  }

  public void simpleWalk(Node node, int tab) {
    System.out.print("\n");
    for (int i = 0; i < tab; i++) {
      System.out.print(" | ");
    }
    System.out.print(" " + node.data + " ");
    for (Node child : node.getChildren()) {
      simpleWalk(child, tab + 1);
    }
  }

  public void walkPrint(Node node) {
    if (node == null)
      return;

    code += node.enter;
    if (node.IsLeaf()) {
      code += " " + node.data + " ";
    }
    for (Node child : node.getChildren()) {
      walkPrint(child);
    }
    code += node.exit;
  }
}
