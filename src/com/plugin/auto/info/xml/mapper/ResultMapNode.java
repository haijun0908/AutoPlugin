package com.plugin.auto.info.xml.mapper;

public class ResultMapNode extends Node {
    public ResultMapNode(String node) {
        super(node);
    }

    public ResultMapNode column(String column) {
        put("column",column);
        return this;
    }

    public ResultMapNode property(String property) {
        put("property",property);
        return this;
    }
}
