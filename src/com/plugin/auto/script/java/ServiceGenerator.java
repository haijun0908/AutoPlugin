package com.plugin.auto.script.java;

import com.plugin.auto.info.*;
import com.plugin.auto.utils.PluginUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ServiceGenerator extends JavaGenerator {
    public static final int SERVICE = 1;
    public static final int SERVICE_IMPL = 2;
    private boolean isImpl = false;
    private TableInfo tableInfo;


    public ServiceGenerator(DatabaseConfigInfo configInfo, List<TableInfo> tableInfoList) {
        super(configInfo, tableInfoList);
    }

    @Override
    protected int writeTimes() {
        return 2;
    }


    @Override
    protected void resetCurrentTime(int currentTime, TableInfo tableInfo) {
        isImpl = currentTime == SERVICE_IMPL;
        this.tableInfo = tableInfo;


    }

    @Override
    protected String getFileName() {
        return PluginUtils.javaName(tableInfo.getTableName(), true) + (isImpl ? "ServiceImpl" : "Service");
    }

    @Override
    protected List<String> getImportList() {
        List<String> importList = new ArrayList<>();

        importList.addAll(PluginUtils.getImportList(tableInfo.getPrimaryColumns()));
        importList.add("java.util.List");
        importList.add("java.util.Map");

        importList.add("");

        DtoGenerator dtoGenerator = new DtoGenerator(configInfo, null);
        dtoGenerator.resetCurrentTime(2, tableInfo);
        ModelGenerator modelGenerator = new ModelGenerator(configInfo, null);
        modelGenerator.resetCurrentTime(1, tableInfo);
        importList.add(dtoGenerator.getFullName());
        importList.add(modelGenerator.getFullName());

        if (isImpl) {
            ServiceGenerator serviceGenerator = new ServiceGenerator(configInfo, null);
            serviceGenerator.resetCurrentTime(SERVICE, tableInfo);
            importList.add(serviceGenerator.getFullName());
        }



        return importList;
    }

    @Override
    protected boolean isAbstract() {
        return false;
    }

    @Override
    protected String getFileAnno() {
        return null;
    }

    @Override
    protected JavaFile.FileType getFileType() {
        return isImpl ? JavaFile.FileType.CLASS : JavaFile.FileType.INTERFACE;
    }

    @Override
    protected String getParentClass() {
        return null;
    }

    @Override
    protected List<String> getImplClassList() {
        if (isImpl) {
            return Arrays.asList(PluginUtils.javaName(tableInfo.getTableName(), true) + "Service");
        }
        return null;
    }

    @Override
    protected List<JavaFileField> getFieldList() {
        return null;
    }

    @Override
    protected List<JavaFileMethod> getMethodList() {
        String javaName = PluginUtils.javaName(tableInfo.getTableName(), true);

        DtoGenerator dtoGenerator = new DtoGenerator(configInfo, null);
        dtoGenerator.resetCurrentTime(DtoGenerator.CHILD_TIME, tableInfo);
        String dtoName = dtoGenerator.getFileName();

        String primaryKey = "PrimaryKey";
        String primaryParams = "";
        List<ColumnInfo> primaryList = tableInfo.getPrimaryColumns();

        boolean onePrimaryKey = false;
        PluginUtils.Reg primaryReg = null;

        if (primaryList != null && primaryList.size() > 0) {
            if (primaryList.size() == 1) {
                primaryReg = PluginUtils.reg(primaryList.get(0));
                onePrimaryKey = true;

                String field = primaryList.get(0).getField();
                primaryKey = PluginUtils.javaName(field, true);
                primaryParams = primaryReg.type + " " + PluginUtils.lowerFirst(primaryKey);
            } else {
                for (ColumnInfo columnInfo : primaryList) {
                    primaryParams += PluginUtils.reg(columnInfo).type + " " + PluginUtils.javaName(columnInfo.getField(), false);
                    primaryParams += " , ";
                }
                primaryParams = primaryParams.substring(0, primaryParams.length() - 2);
            }

        }


        List<JavaFileMethod> methodList = new ArrayList<>();
        //save
        JavaFileMethod save = new JavaFileMethod();
        save.returnType("int").method("save" + javaName).params(dtoName + " " + PluginUtils.lowerFirst(dtoName));
        if (isImpl) {
            //todo
            save.body("return 0;");
        }


        //update
        JavaFileMethod update = new JavaFileMethod();
        update.returnType("boolean").method("update" + javaName).params(dtoName + " " + PluginUtils.lowerFirst(dtoName));
        if (isImpl) {
            //todo
            update.body("return false;");
        }


        //delete
        JavaFileMethod delete = new JavaFileMethod();
        delete.returnType("boolean").method("deleteBy" + primaryKey).params(primaryParams);
        if(isImpl){
            //todo
            delete.body("return false;");
        }

        //get
        JavaFileMethod get = new JavaFileMethod();
        get.returnType(dtoName).method("get" + javaName + "By" + primaryKey).params(primaryParams);
        if(isImpl){
            //todo
            get.body("return null;");
        }

        //getAll
        JavaFileMethod getAll = new JavaFileMethod();
        getAll.returnType("List<"+dtoName+">").method("getAll");
        if(isImpl){
            //todo
            getAll.body("return null;");
        }

        methodList.add(save);
        methodList.add(update);
        methodList.add(delete);
        methodList.add(get);
        methodList.add(getAll);

        if(onePrimaryKey){

            //list
            JavaFileMethod getList = new JavaFileMethod();
            getList.returnType("List<"+dtoName+">").method("get" + javaName + "List").params("List<"+primaryReg.packageName+"> " + primaryList.get(0).getField() + "List");
            if(isImpl){
                getList.body("return null;");
            }

            //map
            JavaFileMethod map = new JavaFileMethod();
            map.returnType("Map<"+primaryReg.packageName+", "+dtoName+">").method("get" + javaName + "MapBy" + primaryKey)
                    .params("List<"+primaryReg.packageName+"> " + primaryList.get(0).getField() + "List");
            if(isImpl){
                map.body("return null;");
            }

            methodList.add(getList);
            methodList.add(map);
        }




        return methodList;
    }

    @Override
    protected String getSubPackage() {
        return "service" + (isImpl ? ".impl" : "");
    }
}
