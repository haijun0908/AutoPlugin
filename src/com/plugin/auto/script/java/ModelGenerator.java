package com.plugin.auto.script.java;

import com.plugin.auto.info.*;
import com.plugin.auto.utils.PluginUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ModelGenerator extends JavaGenerator {

    private TableInfo tableInfo;

    private List<JavaFileField> fieldList = new ArrayList<>();
    private List<JavaFileMethod> methodList = new ArrayList<>();

    public ModelGenerator(DatabaseConfigInfo configInfo, List<TableInfo> tableInfoList) {
        super(configInfo, tableInfoList);
    }


    @Override
    protected int writeTimes() {
        return 1;
    }

    @Override
    protected void resetCurrentTime(int currentTime, TableInfo tableInfo) {
        this.tableInfo = tableInfo;
        //初始化一些数据
        fieldList.add(new JavaFileField().access(JavaAccess.PRIVATE).type("long").field("serialVersionUID").defaultVal("-1L").modifier("static final"));
        for (ColumnInfo info : tableInfo.getColumnInfoList()) {
            PluginUtils.Reg reg = PluginUtils.reg(info);
            //field
            fieldList.add(new JavaFileField().comment(info.getComment()).field(info.getField()).type(reg.type).access(JavaAccess.PRIVATE));
            //setMethod
            methodList.add(new JavaFileMethod().access(JavaAccess.PUBLIC).returnType("void").method("set" + PluginUtils.javaName(info.getField(), true))
                    .params(reg.type + " " + info.getField()).body("this." + info.getField() + " = " + info.getField() + ";")
            );
            //getMethod
            methodList.add(new JavaFileMethod().access(JavaAccess.PUBLIC).returnType(reg.type).method("get" + PluginUtils.javaName(info.getField(), true))
                    .body("return this." + info.getField() + ";")
            );
        }
    }

    @Override
    protected String getFileName() {
        return PluginUtils.javaName(tableInfo.getTableName(), true) + "Model";
    }

    @Override
    protected List<String> getImportList() {
        List<String> list = new ArrayList<>();
        list.add("java.io.Serializable");
        list.addAll(PluginUtils.getImportList(tableInfo.getColumnInfoList()));
        return list;
    }

    @Override
    protected boolean isAbstract() {
        return false;
    }

    @Override
    protected String getFileComment() {
        return null;
    }

    @Override
    protected JavaFile.FileType getFileType() {
        return JavaFile.FileType.CLASS;
    }

    @Override
    protected String getParentClass() {
        return null;
    }

    @Override
    protected List<String> getImplClassList() {
        return Arrays.asList("Serializable");
    }

    @Override
    protected List<JavaFileField> getFieldList() {
        return fieldList;
    }

    @Override
    protected List<JavaFileMethod> getMethodList() {
        return methodList;
    }

    @Override
    protected String getSubPackage() {
        return "model";
    }


    public static void main(String[] args) {
        TableInfo tableInfo = new TableInfo();
        tableInfo.setTableName("cc_circle22");
        tableInfo.setOriginTableName("cc_circle22");
        List<ColumnInfo> columnInfoList = new ArrayList<>();
        ColumnInfo ci = new ColumnInfo();
        ci.setType("int");
        ci.setPrimaryKey(true);
        ci.setField("id");
        ci.setComment("zhujianid");
        ci.setDefaultVal(22);
        ci.setAutoIncrement(true);
        columnInfoList.add(ci);
        ColumnInfo ci3 = new ColumnInfo();
        ci3.setType("date");
        ci3.setPrimaryKey(false);
        ci3.setField("createTime");
        ci3.setComment("zhujianid");
        ci3.setDefaultVal(22);
        columnInfoList.add(ci3);
        tableInfo.setColumnInfoList(columnInfoList);

        DatabaseConfigInfo configInfo = new DatabaseConfigInfo();
        configInfo.setPackagePath("com.plugin.auto.out");
        configInfo.setWriteFilePath("/Users/Jun/Documents/idea_workspace/AutoPlugin/OutFile/src/");

        new DtoGenerator(configInfo, Arrays.asList(tableInfo)).startGeneratorList();
        new ModelGenerator(configInfo, Arrays.asList(tableInfo)).startGeneratorList();
        new ServiceGenerator(configInfo , Arrays.asList(tableInfo)).startGeneratorList();
        new MybatisDaoGenerator(configInfo , Arrays.asList(tableInfo)).startGeneratorList();
    }
}
