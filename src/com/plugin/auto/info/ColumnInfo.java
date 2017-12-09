package com.plugin.auto.info;

import org.apache.commons.lang3.StringUtils;

public class ColumnInfo {
    private String field;
    private String type;
    private boolean isPrimaryKey;
    private Object defaultVal;
    private String comment;
    private boolean isAutoIncrement;
    private String customField;

    public boolean isAutoIncrement() {
        return isAutoIncrement;
    }

    public void setAutoIncrement(boolean autoIncrement) {
        isAutoIncrement = autoIncrement;
    }

    public String getCustomField() {
        if(StringUtils.isBlank(customField)){
            return field;
        }
        return customField;
    }

    public void setCustomField(String customField) {
        this.customField = customField;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isPrimaryKey() {
        return isPrimaryKey;
    }

    public void setPrimaryKey(boolean primaryKey) {
        isPrimaryKey = primaryKey;
    }

    public Object getDefaultVal() {
        return defaultVal;
    }

    public void setDefaultVal(Object defaultVal) {
        this.defaultVal = defaultVal;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
