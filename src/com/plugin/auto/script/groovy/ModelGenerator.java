package com.plugin.auto.script.groovy;

import com.plugin.auto.info.ColumnInfo;
import com.plugin.auto.info.DatabaseConfigInfo;
import com.plugin.auto.info.TableInfo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ModelGenerator extends GroovyGenerator {
    public ModelGenerator(DatabaseConfigInfo configInfo, List<TableInfo> tableInfoList) {
        super(configInfo, tableInfoList);
    }

    @Override
    String getGroovyFile() {
        return "test.groovy";
    }


    public static void main(String[] args){
        TableInfo tableInfo = new TableInfo();
        tableInfo.setTableName("cc_circle22");
        List<ColumnInfo> columnInfoList = new ArrayList<>();
        ColumnInfo ci = new ColumnInfo();
        ci.setType("int");
        ci.setPrimaryKey(true);
        ci.setField("id");
        ci.setComment("zhujianid");
        ci.setDefaultVal(22);
        columnInfoList.add(ci);
        ColumnInfo ci3 = new ColumnInfo();
        ci3.setType("date");
        ci3.setPrimaryKey(false);
        ci3.setField("d");
        ci3.setComment("zhujianid");
        ci3.setDefaultVal(22);
        columnInfoList.add(ci3);
        tableInfo.setColumnInfoList(columnInfoList);

        DatabaseConfigInfo configInfo = new DatabaseConfigInfo();
        configInfo.setPackagePath("com.plugin.auto.out");
        new ServiceGenerator(configInfo , Arrays.asList(tableInfo)).startGeneratorList();
        new ModelGenerator(configInfo , Arrays.asList(tableInfo)).startGeneratorList();
        new DtoGenerator(configInfo , Arrays.asList(tableInfo)).startGeneratorList();
    }
}
