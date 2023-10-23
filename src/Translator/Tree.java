package Translator;

public class Tree {
  Node root;

  public Tree(Node root) {
    this.root = root;
  }

  public void simpleWalk(Node node) {
    System.out.print(" " + node.data + " ");
    for (Node child : node.getChildren()) {
      simpleWalk(child);
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
