package com.plugin.auto.script.java;

import com.intellij.javaee.module.view.common.editor.FacetAsVirtualFile;
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
            if(reg == null){
                throw new RuntimeException("can't parse the columnType '"+info.getType()+"' which table is '"+tableInfo.getOriginTableName()+"'");
            }
            //field
            fieldList.add(new JavaFileField()
                    .comment(info.getComment())
                    .field(PluginUtils.javaName(info.getField(), false))
                    .type(reg.type)
                    .access(JavaAccess.PRIVATE)
                    .defaultVal(getDefaultValue(info,reg, false))
            );
            //setMethod
            methodList.add(new JavaFileMethod().access(JavaAccess.PUBLIC).returnType("void").method("set" + PluginUtils.javaName(info.getField(), true))
                    .params(reg.type + " " + PluginUtils.javaName(info.getField(), false)).body("this." + PluginUtils.javaName(info.getField(), false) + " = " + PluginUtils.javaName(info.getField(), false) + ";")
            );
            //getMethod
            methodList.add(new JavaFileMethod().access(JavaAccess.PUBLIC).returnType(reg.type).method("get" + PluginUtils.javaName(info.getField(), true))
                    .body("return this." + PluginUtils.javaName(info.getCustomField(), false) + ";")
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
        return tableInfo.getComment();
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
    protected String getFilePath() {
        return configInfo.getModelFilePath();
    }

    @Override
    protected String getPackagePath() {
        return configInfo.getModelPackage() + tableInfo.getFirstSubPackage();
    }

    @Override
    protected String getAnno() {
        return null;
    }




}
