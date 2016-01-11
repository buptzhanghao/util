package com.sankuai.xm.search.domain.trietree;

import java.util.HashMap;
import java.util.Map;

/**
 * @author zhanghao
 * @version 1.0
 * @created 15/12/4
 */
public class Node {

    String name; // 结点的字符名称
    int fre; // 单词的词频
    boolean end; // 是否是单词结尾
    boolean root; // 是否是根结点
    Map<String, Node> childrens; // 子节点信息

    public Node(String name) {
        this.name = name;
        if (childrens == null) {
            childrens = new HashMap<String, Node>();
        }
        setFre(0);
        setRoot(false);
        setEnd(false);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getFre() {
        return fre;
    }

    public void setFre(int fre) {
        this.fre = fre;
    }

    public boolean isEnd() {
        return end;
    }

    public void setEnd(boolean end) {
        this.end = end;
    }

    public boolean isRoot() {
        return root;
    }

    public void setRoot(boolean root) {
        this.root = root;
    }

    public Map<String, Node> getChildrens() {
        return childrens;
    }

    public void setChildrens(Map<String, Node> childrens) {
        this.childrens = childrens;
    }

}
