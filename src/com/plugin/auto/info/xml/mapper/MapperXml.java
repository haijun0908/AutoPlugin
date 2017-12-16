package com.plugin.auto.info.xml.mapper;

import com.plugin.auto.info.xml.XmlInfo;

import java.util.List;

public class MapperXml extends XmlInfo{
    private List<MapperBase> elementList;
    private String namespace;

    public List<MapperBase> getElementList() {
        return elementList;
    }

    public String getNamespace() {
        return namespace;
    }

    public MapperXml elementList(List<MapperBase> elementList) {
        this.elementList = elementList;
        return this;
    }

    public MapperXml namespace(String namespace) {
        this.namespace = namespace;
        return this;
    }

    @Override
    public String getRoot() {
        return "mapper";
    }

    @Override
    public String getDtd() {
        return "\"-//mybatis.org//DTD Mapper 3.0//EN\"\n" +
                "        \"http://mybatis.org/dtd/mybatis-3-mapper.dtd\"";
    }
}
