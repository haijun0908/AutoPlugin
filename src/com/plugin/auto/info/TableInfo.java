package com.plugin.auto.info;

import java.util.ArrayList;
import java.util.List;

public class TableInfo {
    /**
     * 表明
     **/
    private String tableName;
    /**
     * 字段
     **/
    private List<ColumnInfo> columnInfoList;
    /**
     * 注释
     **/
    private String comment;
    /**
     * 原始的表名
     **/
    private String originTableName;
    /**
     * 表索引
     **/
    private List<TableIndex> tableIndexList;

    public List<TableIndex> getTableIndexList() {
        return tableIndexList;
    }

    public void setTableIndexList(List<TableIndex> tableIndexList) {
        this.tableIndexList = tableIndexList;
    }

    public String getOriginTableName() {
        return originTableName;
    }

    public void setOriginTableName(String originTableName) {
        this.originTableName = originTableName;
    }

    private List<ColumnInfo> primaryColumns;

    public List<ColumnInfo> getColumnInfoList() {
        return columnInfoList;
    }

    public void setColumnInfoList(List<ColumnInfo> columnInfoList) {
        this.columnInfoList = columnInfoList;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public List<ColumnInfo> getPrimaryColumns() {
        if (columnInfoList == null || columnInfoList.size() <= 0) {
            return null;
        }
        primaryColumns = new ArrayList<>();
        for (ColumnInfo columnInfo : columnInfoList) {
            if (columnInfo.isPrimaryKey()) primaryColumns.add(columnInfo);
        }
        return primaryColumns;
    }

    public ColumnInfo getAutoIncrementColumn() {
        if (columnInfoList == null || columnInfoList.size() <= 0) {
            return null;
        }
        return columnInfoList.stream().filter(ColumnInfo::isAutoIncrement).findFirst().orElse(null);
    }

    /**
     * 获取_前缀
     * eg:
     * test_plugin
     * 返回.test
     *
     * @return
     */
    public String getFirstSubPackage() {
        if (originTableName.indexOf("_") > -1) {
            return "." + originTableName.substring(0, originTableName.indexOf("_"));
        } else {
            return "";
        }
    }

}
