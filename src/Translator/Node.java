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
}
