package com.plugin.auto.script;

import com.plugin.auto.info.DatabaseConfigInfo;
import com.plugin.auto.info.TableInfo;

import java.util.List;

public abstract class BaseGenerator {

    protected DatabaseConfigInfo configInfo;
    private List<TableInfo> tableInfoList;

    protected TableInfo currentTable;

    public BaseGenerator(DatabaseConfigInfo configInfo, List<TableInfo> tableInfoList) {
        this.configInfo = configInfo;
        this.tableInfoList = tableInfoList;
    }

    protected abstract void generator();

    public void startGeneratorList() {
        if (tableInfoList != null && tableInfoList.size() > 0) {
            for (TableInfo tableInfo : tableInfoList) {
                this.currentTable = tableInfo;
                generator();
            }
        }
    }
}
