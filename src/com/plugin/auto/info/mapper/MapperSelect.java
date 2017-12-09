package com.plugin.auto.info.mapper;

public class MapperSelect extends MapperBase{
    private String resultMap;
    @Override
    public String element() {
        return "select";
    }

    public MapperSelect resultMap(String resultMap) {
        this.resultMap = resultMap;
        return this;
    }
}
