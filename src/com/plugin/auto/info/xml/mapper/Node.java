package com.plugin.auto.info.xml.mapper;

import java.util.ArrayList;
import java.util.List;

public class Node {
    private List<String> keys = new ArrayList<>();
    private List<String> values = new ArrayList<>();
    private String node;

    private List<Node> subNodeList;

    public Node(String node) {
        this.node = node;
        keys = new ArrayList<>();
        values = new ArrayList<>();
    }

    public List<Node> getSubNodeList() {
        return subNodeList;
    }


    public Node put(String key, String value) {
        keys.add(key);
        values.add(value);
        return this;
    }

    public List<String> getKeys() {
        return keys;
    }

    public void setKeys(List<String> keys) {
        this.keys = keys;
    }

    public List<String> getValues() {
        return values;
    }

    public void setValues(List<String> values) {
        this.values = values;
    }

    public String getNode() {
        return node;
    }

    public Node keys(List<String> keys) {
        this.keys = keys;
        return this;
    }

    public Node values(List<String> values) {
        this.values = values;
        return this;
    }

    public Node node(String node) {
        this.node = node;
        return this;
    }

    public Node subNodeList(List<Node> subNodeList) {
        this.subNodeList = subNodeList;
        return this;
    }

    public String getAttributeValues() {
        if (keys != null && keys.size() > 0
                && values != null && values.size() > 0
                && keys.size() == values.size()) {
            String s = "";
            for (int i = 0; i < keys.size(); i++) {
                s += " " + keys.get(i) + "=\"" + values.get(i) + "\"";
            }
            return s;
        }
        return "";
    }
}
