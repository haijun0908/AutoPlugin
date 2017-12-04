package com.plugin.auto.script.groovy;

import com.plugin.auto.info.DatabaseConfigInfo;
import com.plugin.auto.info.TableInfo;

import java.util.List;

public class DaoGenerator extends GroovyGenerator {
    public DaoGenerator(DatabaseConfigInfo configInfo, List<TableInfo> tableInfoList) {
        super(configInfo, tableInfoList);
    }

    @Override
    String getGroovyFile() {
        return "dao.groovy";
    }
}
