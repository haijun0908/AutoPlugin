package com.plugin.auto.script.groovy;

import com.plugin.auto.info.DatabaseConfigInfo;
import com.plugin.auto.info.TableInfo;

import java.util.List;

public class DtoGenerator extends GroovyGenerator {
    public DtoGenerator(DatabaseConfigInfo configInfo, List<TableInfo> tableInfoList) {
        super(configInfo, tableInfoList);
    }

    @Override
    String getGroovyFile() {
        return "dto.groovy";
    }
}
