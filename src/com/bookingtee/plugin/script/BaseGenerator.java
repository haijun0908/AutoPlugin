package com.bookingtee.plugin.script;

import com.bookingtee.plugin.info.DatabaseConfigInfo;
import com.bookingtee.plugin.info.TableInfo;

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
