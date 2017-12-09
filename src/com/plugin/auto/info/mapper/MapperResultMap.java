package com.plugin.auto.info.mapper;

public class MapperResultMap extends MapperBase {
    private String type;

    public String getType() {
        return type;
    }

    @Override
    public String element() {
        return "resultMap";
    }

    public MapperResultMap type(String type) {
        this.type = type;
        return this;
    }
}
