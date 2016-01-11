package com.sankuai.xm.search.domain.trietree;

/**
 * @author zhanghao
 * @version 1.0
 * @created 15/12/4
 */
public class TrieTree {

    Node root;

    public TrieTree(String name) {
        root = new Node(name);
        root.setFre(0);
        root.setEnd(false);
        root.setRoot(true);
    }

    public void insert(String word) {

        Node node = root;
        char[] words = word.toCharArray();
        for (int i = 0; i < words.length; i++) {
            if (node.getChildrens().containsKey(words[i] + "")) {
                if (i == words.length - 1) {
                    Node endNode = node.getChildrens().get(words[i] + "");
                    endNode.setFre(endNode.getFre() + 1);
                    endNode.setEnd(true);
                }
            } else {
                Node newNode = new Node(words[i] + "");
                if (i == words.length - 1) {
                    newNode.setFre(1);
                    newNode.setEnd(true);
                    newNode.setRoot(false);
                }

                node.getChildrens().put(words[i] + "", newNode);
            }

            node = node.getChildrens().get(words[i] + "");
        }

    }

    public int searchFre(String word) {
        int fre = -1;

        Node node = root;
        char[] words = word.toCharArray();
        for (int i = 0; i < words.length; i++) {
            if (node.getChildrens().containsKey(words[i] + "")) {
                node = node.getChildrens().get(words[i] + "");
                fre = node.getFre();
            } else {
                fre = -1;
                break;
            }
        }
        return fre;
    }

    public Node getRoot() {
        return root;
    }

    public void setRoot(Node root) {
        this.root = root;
    }
}
