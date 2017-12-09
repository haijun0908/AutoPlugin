package com.plugin.auto.info.mapper;

import java.util.ArrayList;
import java.util.List;

public class Node {
    private List<String> keys = new ArrayList<>();
    private List<String> values = new ArrayList<>();
    private String node;

    public Node(String node) {
        this.node = node;
        keys = new ArrayList<>();
        values = new ArrayList<>();
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
}
