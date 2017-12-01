package com.bookingtee.plugin.script.java;

import com.bookingtee.plugin.info.*;
import com.bookingtee.plugin.utils.PluginUtils;

import java.util.ArrayList;
import java.util.List;

public class DaoGenerator extends JavaGenerator {

    public static final int PARENT = 1;
    public static final int CHILD = 2;

    private boolean isBase = false;
    private TableInfo tableInfo;

    public DaoGenerator(DatabaseConfigInfo configInfo, List<TableInfo> tableInfoList) {
        super(configInfo, tableInfoList);
    }

    @Override
    protected int writeTimes() {
        return 2;
    }

    @Override
    protected void resetCurrentTime(int currentTime, TableInfo tableInfo) {
        this.isBase = currentTime == PARENT;
        this.tableInfo = tableInfo;


    }

    @Override
    protected String getFileName() {
        return PluginUtils.javaName(tableInfo.getTableName(), true) + (isBase ? "DaoBase" : "Dao");
    }

    @Override
    protected List<String> getImportList() {
        List<String> importList = new ArrayList<>();

        ModelGenerator model = new ModelGenerator(configInfo, null);
        model.resetCurrentTime(1, tableInfo);

        importList.add(model.getFullName());

        if(!isBase){
            DaoGenerator dao = new DaoGenerator(configInfo , null);
            dao.resetCurrentTime(PARENT , tableInfo);
            importList.add(dao.getFullName());
        }


        return importList;
    }

    @Override
    protected boolean isAbstract() {
        return isBase;
    }

    @Override
    protected String getFileAnno() {
        return null;
    }

    @Override
    protected JavaFile.FileType getFileType() {
        return JavaFile.FileType.CLASS;
    }

    @Override
    protected String getParentClass() {
        return isBase ? null : (PluginUtils.javaName(tableInfo.getTableName(), true) + "DaoBase");
    }

    @Override
    protected List<String> getImplClassList() {
        return null;
    }

    @Override
    protected List<JavaFileField> getFieldList() {
        return null;
    }

    @Override
    protected List<JavaFileMethod> getMethodList() {
        List<JavaFileMethod> methodList = new ArrayList<>();
        return methodList;
    }

    @Override
    protected String getSubPackage() {
        return "dao" + (isBase ? ".base" : "");
    }
}