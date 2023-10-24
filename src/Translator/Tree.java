package Translator;

public class Tree {
  public Node root;

  public Tree(Node root) {
    this.root = root;
  }

  public void print(Node node) {
    simpleWalk(node, 0);
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

  public void walk(Node node) {
    System.out.print(node.enter);
    if (node.IsLeaf()) {
      System.out.print(" " + node.data + " ");
    }
    for (Node child : node.getChildren()) {
      walk(child);
    }
    System.out.print(node.exit);
  }
}
