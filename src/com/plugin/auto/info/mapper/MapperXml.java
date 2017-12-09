package com.plugin.auto.info.mapper;

import java.util.List;

public class MapperXml {
    private List<MapperBase> elementList;
    private String namespace;
    private String fileName;
    private String filePath;

    public String getFileName() {
        return fileName;
    }

    public String getFilePath() {
        return filePath;
    }

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

    public MapperXml fileName(String fileName) {
        this.fileName = fileName;
        return this;
    }

    public MapperXml filePath(String filePath) {
        this.filePath = filePath;
        return this;
    }
}
