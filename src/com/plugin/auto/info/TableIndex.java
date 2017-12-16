package com.plugin.auto.info;

import com.plugin.auto.utils.PluginUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 表索引
 */
public class TableIndex {
    private boolean isUnique = false;
    private List<ColumnInfo> columnInfoList;


    public boolean isUnique() {
        return isUnique;
    }

    public void setUnique(boolean unique) {
        isUnique = unique;
    }

    public List<ColumnInfo> getColumnInfoList() {
        return columnInfoList;
    }

    public void setColumnInfoList(List<ColumnInfo> columnInfoList) {
        this.columnInfoList = columnInfoList;
    }

    public String getMethod() {
        if (isUnique && columnInfoList.size() > 1) {
            return "getBy" + columnInfoList.stream().map(ColumnInfo::upperField).collect(Collectors.joining("And"));
        } else if (!isUnique) {
            return "getListBy" + columnInfoList.stream().map(ColumnInfo::upperField).collect(Collectors.joining("And"));
        }
        return null;
    }

    public String getReturnType(String className) {
        if (isUnique && columnInfoList.size() > 1) {
            return className;
        } else if (!isUnique) {
            return "List<" + className + ">";
        }
        return null;
    }

    public String getParams(boolean isDao) {
        return columnInfoList.stream().map(columnInfo -> columnInfo.showParam(isDao)).collect(Collectors.joining(", "));
    }

}
