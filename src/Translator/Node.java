package Translator;

import java.util.ArrayList;
import java.util.List;

import Utils.Token;

public class Node {
  public String data;
  public List<Node> children;
  public String enter;
  public String exit;
  public Token token;

  public Node(String data) {
    this.data = data;
    children = new ArrayList<>();
    enter = "";
    exit = "";
  }

  public Node(String data, Token t) {
    this.data = data;
    children = new ArrayList<>();
    enter = "";
    exit = "";
    this.token = t;
  }

  public void addChild(Node child) {
    children.add(child);
  }

  public List<Node> getChildren() {
    return children;
  }

  public Node getChild(int i) {
    return this.getChildren().get(i);
  }

  public boolean IsLeaf() {
    return children.size() == 0;
  }

  private int c = 0;

  public int IsString() {
    c = 0;
    walk(this);

    return c;
  }

  public void walk(Node node) {
    if (node.IsLeaf()) {
      if (node.data.contains('"' + "")) {
        if (c != -1)
          c++;
      }

      String ops[] = { "==", ">", "<", ">=", "<=" };
      for (String op : ops) {
        if (node.data.equals(op)) {
          c = -1;
          break;
        }
      }
    }

    for (Node child : node.getChildren()) {
      walk(child);
    }
  }

  public Node getFirstLeaf(Node node) {
    if (node.IsLeaf()) {
      return node;
    }

    return getFirstLeaf(node.getChild(0));
  }

  public void walkLeafs(Node node, ArrayList<Token> leafs) {
    if (node == null)
      return;

    if (node.IsLeaf()) {
      leafs.add(node.token);
    }
    for (Node child : node.getChildren()) {
      walkLeafs(child, leafs);
    }
  }

  public void getLeafs(ArrayList<Token> leafs) {
    walkLeafs(this, leafs);
  }
}
