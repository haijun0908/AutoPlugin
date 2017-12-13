package com.plugin.auto.script.java;

import com.plugin.auto.common.WriteJavaFileListener;
import com.plugin.auto.info.*;
import com.plugin.auto.utils.PluginUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class MybatisDaoGenerator extends JavaGenerator {
    public static final int PARENT = 1;
    public static final int CHILD = 2;

    private boolean isBase = false;
    private TableInfo tableInfo;

    private String javaName;
    private String primaryKey;
    private boolean onePrimaryKey = false;
    private String primaryParams = "";
    private PluginUtils.Reg primaryReg;
    private List<ColumnInfo> primaryList;

    public MybatisDaoGenerator(DatabaseConfigInfo configInfo, List<TableInfo> tableInfoList) {
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

        init();
    }

    private void init() {
        javaName = PluginUtils.javaName(tableInfo.getTableName(), true);

        primaryKey = "PrimaryKey";

        primaryList = tableInfo.getPrimaryColumns();

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
        importList.add("java.util.List");
        importList.add("java.util.Map");

        if (!isBase) {
            DaoGenerator dao = new DaoGenerator(configInfo, null);
            dao.resetCurrentTime(PARENT, tableInfo);
            importList.add(dao.getFullName());
        }


        return importList;
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
        return JavaFile.FileType.INTERFACE;
    }

    @Override
    protected String getParentClass() {
        return isBase ? null : PluginUtils.javaName(tableInfo.getTableName(), true) + "DaoBase";
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
        if (!isBase) {
            return null;
        }


        ModelGenerator modelGenerator = new ModelGenerator(configInfo, null);
        modelGenerator.resetCurrentTime(1, tableInfo);
        String modelName = modelGenerator.getFileName();


        List<JavaFileMethod> methodList = new ArrayList<>();
        //save
        JavaFileMethod save = new JavaFileMethod();
        save.returnType("int").method(saveName()).params(modelName + " " + PluginUtils.lowerFirst(modelName));


        //update
        JavaFileMethod update = new JavaFileMethod();
        update.returnType("boolean").method(updateName()).params(modelName + " " + PluginUtils.lowerFirst(modelName));


        //delete
        JavaFileMethod delete = new JavaFileMethod();
        delete.returnType("boolean").method(deleteName()).params(primaryParams);

        //get
        JavaFileMethod get = new JavaFileMethod();
        get.returnType(modelName).method(getName()).params(primaryParams);

        //getAll
        JavaFileMethod getAll = new JavaFileMethod();
        getAll.returnType("List<" + modelName + ">").method(getAllName());

        methodList.add(save);
        methodList.add(update);
        methodList.add(delete);
        methodList.add(get);
        methodList.add(getAll);

        if (onePrimaryKey) {

            //list
            JavaFileMethod getList = new JavaFileMethod();
            getList.returnType("List<" + modelName + ">").method(getListName()).params("List<" + primaryReg.packageName + "> " + primaryList.get(0).getField() + "List");

            methodList.add(getList);

            if (tableInfo.getTableIndexList() != null && tableInfo.getTableIndexList().size() > 0) {
                tableInfo.getTableIndexList().stream().filter(tableIndex -> !(tableIndex.isUnique() && tableIndex.getColumnInfoList().size() == 1))
                        .forEach(tableIndex -> methodList.add(new JavaFileMethod().returnType(tableIndex.getReturnType(modelName))
                                .method(tableIndex.getMethod())
                                .params(tableIndex.getParams())
                        ));
            }
        }

        return methodList;
    }


    @Override
    protected String getSubPackage() {
        return "dao" + (isBase ? ".base" : "");
    }

    @Override
    protected String getAnno() {
        return null;
    }


    public String saveName() {
        return "save" + javaName;
    }

    public String updateName() {
        return "update" + javaName;
    }

    public String getMapName() {
        return "get" + javaName + "MapBy" + primaryKey;
    }

    public String getListName() {
        return "get" + javaName + "List";
    }

    public String getAllName() {
        return "getAll";
    }

    public String getName() {
        return "get" + javaName + "By" + primaryKey;
    }

    public String deleteName() {
        return "deleteBy" + primaryKey;
    }

    @Override
    public void aroundFile(Around around, JavaFile javaFile, StringBuilder sb) {
        super.aroundFile(around, javaFile, sb);
        if (around == Around.after && isBase) {
            new MapperGenerator(configInfo, tableInfo).generator();
        }
    }
}
