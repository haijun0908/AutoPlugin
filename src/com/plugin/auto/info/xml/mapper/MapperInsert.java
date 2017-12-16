package com.plugin.auto.info.xml.mapper;

public class MapperInsert extends MapperBase {
    private boolean useGeneratedKeys;
    private String keyProperty;
    private String parameterType;
    private String keyColumn;

    @Override
    public String element() {
        return "insert";
    }


    public String getKeyColumn() {
        return keyColumn;
    }

    public String getKeyProperty() {
        return keyProperty;
    }

    public String getParameterType() {
        return parameterType;
    }

    public boolean isUseGeneratedKeys() {

        return useGeneratedKeys;
    }

    public MapperInsert useGeneratedKeys(boolean useGeneratedKeys) {
        this.useGeneratedKeys = useGeneratedKeys;
        return this;
    }

    public MapperInsert keyProperty(String keyProperty) {
        this.keyProperty = keyProperty;
        return this;
    }

    public MapperInsert parameterType(String parameterType) {
        this.parameterType = parameterType;
        return this;
    }

    public MapperInsert keyColumn(String keyColumn) {
        this.keyColumn = keyColumn;
        return this;
    }
}
