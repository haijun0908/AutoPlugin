package com.plugin.auto.info.xml.mapper;

public class MapperUpdate extends MapperBase {
    private String parameterType;
    @Override
    public String element() {
        return "update";
    }

    public MapperUpdate parameterType(String parameterType) {
        this.parameterType = parameterType;
        return this;
    }
}
