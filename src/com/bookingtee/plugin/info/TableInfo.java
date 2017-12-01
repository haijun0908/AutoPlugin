package com.bookingtee.plugin.info;

import java.util.ArrayList;
import java.util.List;

public class TableInfo {
    private String tableName;
    private List<ColumnInfo> columnInfoList;
    private String comment;

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
        if(columnInfoList == null || columnInfoList.size() <= 0){
            return null;
        }
        primaryColumns = new ArrayList<>();
        for (ColumnInfo columnInfo : columnInfoList) {
            if(columnInfo.isPrimaryKey()) primaryColumns.add(columnInfo);
        }
        return primaryColumns;
    }

}
