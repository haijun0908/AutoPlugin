package com.plugin.auto.script.java;

import com.plugin.auto.common.WriteFileType;
import com.plugin.auto.info.*;
import com.plugin.auto.utils.PluginUtils;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class ServiceGenerator extends JavaGenerator {
    public static final int SERVICE = 1;
    public static final int SERVICE_IMPL = 2;
    private boolean isImpl = false;
    private TableInfo tableInfo;

    private MybatisDaoGenerator daoGenerator;
    private DtoGenerator dtoGenerator;
    private ModelGenerator modelGenerator;
    private DTOConvertGenerator convertGenerator;


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

        daoGenerator = new MybatisDaoGenerator(configInfo, Arrays.asList(tableInfo));
        daoGenerator.resetCurrentTime(MybatisDaoGenerator.CHILD, tableInfo);

        dtoGenerator = new DtoGenerator(configInfo, null);
        dtoGenerator.resetCurrentTime(DtoGenerator.CHILD_TIME, tableInfo);

        modelGenerator = new ModelGenerator(configInfo, null);
        modelGenerator.resetCurrentTime(1, tableInfo);

        convertGenerator = new DTOConvertGenerator(null, null);
    }

    @Override
    protected String getFileName() {
        return PluginUtils.javaName(tableInfo.getTableName(), true) + (isImpl ? "ServiceImpl" : "Service");
    }

    @Override
    protected List<String> getImportList() {
        List<String> importList = new ArrayList<>();

        importList.addAll(PluginUtils.getImportList(tableInfo.getPrimaryColumns()));
        if (tableInfo.getTableIndexList() != null && tableInfo.getTableIndexList().size() > 0) {
            tableInfo.getTableIndexList().stream()
                    .map(tableIndex -> PluginUtils.getImportList(tableIndex.getColumnInfoList()))
                    .forEach(importList::addAll);
        }
        importList.add("java.util.List");
        importList.add("java.util.Map");

        HashSet<String> h = new HashSet<>(importList);
        importList.clear();
        importList.addAll(h);

        importList.add("");


        importList.add(dtoGenerator.getFullName());


        if (isImpl) {
            importList.add(modelGenerator.getFullName());
            importList.add("javax.annotation.Resource");
            importList.add("java.util.HashMap");
            ServiceGenerator serviceGenerator = new ServiceGenerator(configInfo, null);
            serviceGenerator.resetCurrentTime(SERVICE, tableInfo);
            importList.add(serviceGenerator.getFullName());

            MybatisDaoGenerator daoGenerator = new MybatisDaoGenerator(configInfo, Collections.singletonList(tableInfo));
            daoGenerator.resetCurrentTime(MybatisDaoGenerator.CHILD, tableInfo);
            importList.add(daoGenerator.getFullName());


            importList.add(configInfo.getPackagePath() + "." + convertGenerator.getSubPackage() + "." + convertGenerator.getJavaName());

            importList.add("org.springframework.stereotype.Service");
            importList.add("org.springframework.beans.factory.annotation.Autowired");

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
        return isImpl ? JavaFile.FileType.CLASS : JavaFile.FileType.INTERFACE;
    }

    @Override
    protected String getParentClass() {
        return null;
    }

    @Override
    protected List<String> getImplClassList() {
        List<String> list = new ArrayList<>();
        if (isImpl) {
            list.add(PluginUtils.javaName(tableInfo.getTableName(), true) + "Service");
        } else {

        }
        return list;
    }

    @Override
    protected List<JavaFileField> getFieldList() {
        List<JavaFileField> list = new ArrayList<>();
        if (isImpl) {

            list.add(new JavaFileField().access(JavaAccess.PRIVATE)
                    .type(daoGenerator.getFileName())
                    .field(PluginUtils.javaName(daoGenerator.getFileName(), false))
                    .anno("Autowired")
            );
        }
        return list;
    }

    @Override
    protected List<JavaFileMethod> getMethodList() {
        String javaName = PluginUtils.javaName(tableInfo.getTableName(), true);


        String dtoName = dtoGenerator.getFileName();

        String primaryKey = "PrimaryKey";
        String primaryParams = "";
        String primaryObjs = "";
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
                primaryObjs = PluginUtils.lowerFirst(primaryKey);
            } else {
                for (ColumnInfo columnInfo : primaryList) {
                    primaryParams += PluginUtils.reg(columnInfo).type + " " + PluginUtils.javaName(columnInfo.getField(), false);
                    primaryParams += ", ";
                    primaryObjs += PluginUtils.javaName(columnInfo.getField(), false);
                    primaryObjs += ", ";
                }
                primaryParams = primaryParams.substring(0, primaryParams.length() - 2);
                primaryObjs = primaryObjs.substring(0, primaryObjs.length() - 2);
            }

        }


        List<JavaFileMethod> methodList = new ArrayList<>();
        //save
        JavaFileMethod save = new JavaFileMethod();
        String saveReturnType = "boolean";
        if (tableInfo.getPrimaryColumns().size() == 1 && tableInfo.getPrimaryColumns().get(0).isAutoIncrement()) {
            //只有1个主键 并且自增
            saveReturnType = PluginUtils.reg(tableInfo.getPrimaryColumns().get(0)).type;
        }
        save.returnType(saveReturnType).method("save" + javaName).params(dtoName + " " + PluginUtils.lowerFirst(dtoName));
        if (isImpl) {
            String body = modelGenerator.getFileName() + " model = " + dto2Model() + ";\n";
            body += "int rows = " + PluginUtils.lowerFirst(daoGenerator.getFileName()) + "." + daoGenerator.saveName() + "(model);\n";
            if (tableInfo.getAutoIncrementColumn() != null) {
                body += "if (rows > 0) {\n";
                body += "    " + PluginUtils.lowerFirst(dtoName) + ".set" + PluginUtils.javaName(tableInfo.getAutoIncrementColumn().getField(), true) + "(model.get" + PluginUtils.javaName(tableInfo.getAutoIncrementColumn().getField(), true) + "());\n";
                body += "}\n";
            }
            if (saveReturnType.equals("boolean")) {
                body += "return rows > 0;";
            } else {
                body += "return model.get" + PluginUtils.javaName(tableInfo.getAutoIncrementColumn().getField(), true) + "();";
            }
            save.body(body)
                    .anno("Override");
        }


        //update
        JavaFileMethod update = new JavaFileMethod();
        update.returnType("boolean").method("update" + javaName).params(dtoName + " " + PluginUtils.lowerFirst(dtoName));
        if (isImpl) {
            update.body("return " + PluginUtils.lowerFirst(daoGenerator.getFileName()) + "." + daoGenerator.updateName() + "(" + dto2Model() + ");").anno("Override");
        }


        //delete
        JavaFileMethod delete = new JavaFileMethod();
        delete.returnType("boolean").method("deleteBy" + primaryKey).params(primaryParams);
        if (isImpl) {
            delete.body("return " + PluginUtils.lowerFirst(daoGenerator.getFileName()) + "." + daoGenerator.deleteName() + "(" + primaryObjs + ");").anno("Override");
        }

        //get
        JavaFileMethod get = new JavaFileMethod();
        get.returnType(dtoName).method("get" + javaName + "By" + primaryKey).params(primaryParams);
        if (isImpl) {
            get.body("return " + model2dto(PluginUtils.lowerFirst(daoGenerator.getFileName()) + "." + daoGenerator.getName() + "(" + primaryObjs + ")") + ";").anno("Override");
        }

        //getAll
        JavaFileMethod getAll = new JavaFileMethod();
        getAll.returnType("List<" + dtoName + ">").method("getAll");
        if (isImpl) {
            getAll.body("return " + convertGenerator.getJavaName() + ".convert2List(" + PluginUtils.lowerFirst(daoGenerator.getFileName()) + "." + daoGenerator.getAllName() + "(), " + dtoGenerator.getFileName() + ".class);")
                    .anno("Override");
        }

        methodList.add(save);
        methodList.add(update);
        methodList.add(delete);
        methodList.add(get);
        methodList.add(getAll);

        if (onePrimaryKey) {

            //list
            JavaFileMethod getList = new JavaFileMethod();
            getList.returnType("List<" + dtoName + ">").method("get" + javaName + "List").params("List<" + primaryReg.packageName + "> " + PluginUtils.javaName(primaryList.get(0).getField(),false) + "List");
            if (isImpl) {
                getList.body("return " + convertGenerator.getJavaName() + ".convert2List(" + PluginUtils.lowerFirst(daoGenerator.getFileName()) + "." + daoGenerator.getListName() + "(" + PluginUtils.javaName(primaryList.get(0).getField(),false) + "List), " + dtoGenerator.getFileName() + ".class);")
                        .anno("Override");
            }

            //map
            JavaFileMethod map = new JavaFileMethod();
            map.returnType("Map<" + primaryReg.packageName + ", " + dtoName + ">").method("get" + javaName + "MapBy" + primaryKey)
                    .params("List<" + primaryReg.packageName + "> " + PluginUtils.javaName(primaryList.get(0).getField(),false) + "List");
            if (isImpl) {
                String body = "List<" + dtoName + "> list = get" + javaName + "List(" + PluginUtils.javaName(primaryList.get(0).getField(),false) + "List);" + "\n" +
                        "    if (list != null && list.size() > 0) {" + "\n" +
                        "        Map<" + primaryReg.packageName + ", " + dtoName + "> map = new HashMap<>();" + "\n" +
                        "        for (" + dtoName + " dto : list) {" + "\n" +
                        "            map.put(dto." + "get" + PluginUtils.javaName(primaryList.get(0).getField(), true) + "(), dto);" + "\n" +
                        "        }" + "\n" +
                        "        return map;" + "\n" +
                        "    }" + "\n" +
                        "    return null;" + "\n";
                map.body(body).anno("Override");
            }

            methodList.add(getList);
            methodList.add(map);
        }

        if (tableInfo.getTableIndexList() != null && tableInfo.getTableIndexList().size() > 0) {
            tableInfo.getTableIndexList().stream().filter(tableIndex -> !(tableIndex.isUnique() && tableIndex.getColumnInfoList().size() == 1))
                    .forEach(tableIndex -> {
                        JavaFileMethod method = new JavaFileMethod().returnType(tableIndex.getReturnType(dtoName))
                                .method(tableIndex.getMethod())
                                .params(tableIndex.getParams(false));
                        if (isImpl) {
                            method.body("return " + convertGenerator.getJavaName() + ".convert" + (tableIndex.isUnique() ? "" : "2List") + "(" + PluginUtils.lowerFirst(daoGenerator.getFileName()) + "." + method.getMethod() + "(" +
                                    tableIndex.getColumnInfoList().stream().map(ColumnInfo::getParams).collect(Collectors.joining(", ")) + "), " + dtoGenerator.getFileName() + ".class);")
                                    .anno("Override");
                        }
                        methodList.add(method);
                    });
        }


        return methodList;
    }

    private String dto2Model() {
        return convertGenerator.getJavaName() + ".convert(" + PluginUtils.lowerFirst(dtoGenerator.getFileName()) + ", " + modelGenerator.getFileName() + ".class)";
    }

    private String model2dto(String model) {
        return convertGenerator.getJavaName() + ".convert(" + model + ", " + dtoGenerator.getFileName() + ".class)";
    }

    @Override
    protected String getPackagePath() {
        return (isImpl ? configInfo.getServiceImplPackage() : configInfo.getServicePackage()) + (isImpl ? ".impl" : "") + tableInfo.getFirstSubPackage();
    }

    @Override
    protected String getFilePath() {
        return isImpl ? configInfo.getServiceImplFilePath() : configInfo.getServiceFilePath();
    }

    @Override
    protected String getAnno() {
        return isImpl ? "Service\n@com.alibaba.dubbo.config.annotation.Service" : null;
    }

    @Override
    public void aroundFile(Around around, JavaFile javaFile, StringBuilder sb) {
        super.aroundFile(around, javaFile, sb);
        if (around == Around.after && isImpl) {
            //copy
            new DTOConvertGenerator(configInfo.getWriteFilePath(), configInfo.getPackagePath()).generator();
        }
    }

    @Override
    protected WriteFileType getWriteFileType() {
        return WriteFileType.BOTH_NEW;
    }
}
