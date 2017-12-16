package com.plugin.auto.info.xml.mapper;

import com.google.gson.annotations.Expose;

import java.util.List;

public abstract class MapperBase {
    @Expose
    private String id;
    @Expose
    private List<Node> nodeList;
    @Expose
    private String value;

    public abstract String element();

    public String getId() {
        return id;
    }

    public String getValue() {
        return value;
    }

    public List<Node> getNodeList() {
        return nodeList;
    }

    public MapperBase id(String id) {
        this.id = id;
        return this;
    }

    public MapperBase value(String value) {
        this.value = value;
        return this;
    }

    public MapperBase nodeList(List<Node> nodeList) {
        this.nodeList = nodeList;
        return this;
    }
}
