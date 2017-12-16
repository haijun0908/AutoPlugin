package com.plugin.auto.info;

import com.plugin.auto.utils.PluginUtils;
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
        if (StringUtils.isBlank(customField)) {
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

    public String lowerField() {
        return PluginUtils.javaName(this.field, false);
    }

    public String upperField() {
        return PluginUtils.javaName(this.field, true);
    }

    public String showParam(boolean isDao) {
        String type = PluginUtils.reg(this).type;
        if ("Date".equals(type) || "Time".equals(type)) {
            return (isDao ? ("@Param(\"begin" + upperField() + "\") ") : "") + type + " begin" + upperField() + ", " + ((isDao ? ("@Param(\"end" + upperField() + "\") ") : "") + type + " end" + upperField());
        } else {
            return (isDao ? ("@Param(\"" + lowerField() + "\") ") : "") + type + " " + lowerField();
        }
    }

    public String getSql() {
        String type = PluginUtils.reg(this).type;
        if ("Date".equals(type) || "Time".equals(type)) {
            return this.field + " &gt;= #{begin" + upperField() + "} AND " + this.field + " &lt;= #{end" + upperField() + "}";
        } else {
            return this.field + " = #{" + lowerField() + "}";
        }
    }

    public String getParams() {
        String type = PluginUtils.reg(this).type;
        if ("Date".equals(type) || "Time".equals(type)) {
            return "begin" + upperField() + ", " + "end" + upperField();
        } else {
            return lowerField();
        }
    }
}
