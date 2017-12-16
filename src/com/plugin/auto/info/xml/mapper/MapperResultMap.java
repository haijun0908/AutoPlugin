package com.plugin.auto.info.xml.mapper;

import com.google.gson.annotations.SerializedName;

public class MapperResultMap extends MapperBase {
    private String type;

    @SerializedName("extends")
    private String extendsStr;



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

    public MapperResultMap extendsStr(String extendsStr) {
        this.extendsStr = extendsStr;
        return this;
    }
}
