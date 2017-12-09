package com.plugin.auto.info;

import com.intellij.ui.tabs.TabInfo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
        if(columnInfoList == null || columnInfoList.size() <= 0){
            return null;
        }
        return columnInfoList.stream().filter(ColumnInfo::isAutoIncrement).findFirst().orElse(null);
    }

    public static void main(String[] args){
        List<Integer> list = new ArrayList<>();
        list.add(1);
        list.add(2);
        list.add(3);
        list.add(4);
        list.add(5);
        System.out.println(list.stream()
                .filter(integer -> integer > 3)
                .map(integer -> integer + "aaa")
                .collect(Collectors.joining()));
    }


}
