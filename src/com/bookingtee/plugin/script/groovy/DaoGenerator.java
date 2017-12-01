package com.bookingtee.plugin.script.groovy;

import com.bookingtee.plugin.info.DatabaseConfigInfo;
import com.bookingtee.plugin.info.TableInfo;

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
