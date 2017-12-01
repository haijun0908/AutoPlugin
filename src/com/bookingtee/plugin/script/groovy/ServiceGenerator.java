package com.bookingtee.plugin.script.groovy;

import com.bookingtee.plugin.info.DatabaseConfigInfo;
import com.bookingtee.plugin.info.TableInfo;

import java.util.List;

public class ServiceGenerator extends GroovyGenerator {
    public ServiceGenerator(DatabaseConfigInfo configInfo, List<TableInfo> tableInfoList) {
        super(configInfo, tableInfoList);
    }

    @Override
    String getGroovyFile() {
        return "service.groovy";
    }
}
