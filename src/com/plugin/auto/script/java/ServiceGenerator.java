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
        importList.add("java.util.List");
        importList.add("java.util.Map");

        importList.add("");


        importList.add(dtoGenerator.getFullName());
        importList.add(modelGenerator.getFullName());

        if (isImpl) {
            importList.add("javax.annotation.Resource");
            importList.add("java.util.HashMap");
            ServiceGenerator serviceGenerator = new ServiceGenerator(configInfo, null);
            serviceGenerator.resetCurrentTime(SERVICE, tableInfo);
            importList.add(serviceGenerator.getFullName());

            MybatisDaoGenerator daoGenerator = new MybatisDaoGenerator(configInfo, Arrays.asList(tableInfo));
            daoGenerator.resetCurrentTime(MybatisDaoGenerator.CHILD, tableInfo);
            importList.add(daoGenerator.getFullName());


            importList.add(configInfo.getPackagePath() + "." + convertGenerator.getSubPackage() + "." + convertGenerator.getJavaName());

        } else {

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
                    .anno("Resource")
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
        save.returnType("boolean").method("save" + javaName).params(dtoName + " " + PluginUtils.lowerFirst(dtoName));
        if (isImpl) {
            save.body("return " + PluginUtils.lowerFirst(daoGenerator.getFileName()) + "." + daoGenerator.saveName() + "(" + dto2Model() + ") > 0;");
        }


        //update
        JavaFileMethod update = new JavaFileMethod();
        update.returnType("boolean").method("update" + javaName).params(dtoName + " " + PluginUtils.lowerFirst(dtoName));
        if (isImpl) {
            update.body("return " + PluginUtils.lowerFirst(daoGenerator.getFileName()) + "." + daoGenerator.updateName() + "(" + dto2Model() + ");");
        }


        //delete
        JavaFileMethod delete = new JavaFileMethod();
        delete.returnType("boolean").method("deleteBy" + primaryKey).params(primaryParams);
        if (isImpl) {
            delete.body("return " + PluginUtils.lowerFirst(daoGenerator.getFileName()) + "." + daoGenerator.deleteName() + "(" + primaryObjs + ");");
        }

        //get
        JavaFileMethod get = new JavaFileMethod();
        get.returnType(dtoName).method("get" + javaName + "By" + primaryKey).params(primaryParams);
        if (isImpl) {
            get.body("return " + model2dto(PluginUtils.lowerFirst(daoGenerator.getFileName()) + "." + daoGenerator.getName() + "(" + primaryObjs + ")") + ";");
        }

        //getAll
        JavaFileMethod getAll = new JavaFileMethod();
        getAll.returnType("List<" + dtoName + ">").method("getAll");
        if (isImpl) {
            getAll.body("return " + convertGenerator.getJavaName() + ".convert2List(" + PluginUtils.lowerFirst(daoGenerator.getFileName()) + "." + daoGenerator.getAllName() + "(), " + dtoGenerator.getFileName() + ".class);");
        }

        methodList.add(save);
        methodList.add(update);
        methodList.add(delete);
        methodList.add(get);
        methodList.add(getAll);

        if (onePrimaryKey) {

            //list
            JavaFileMethod getList = new JavaFileMethod();
            getList.returnType("List<" + dtoName + ">").method("get" + javaName + "List").params("List<" + primaryReg.packageName + "> " + primaryList.get(0).getField() + "List");
            if (isImpl) {
                getList.body("return " + convertGenerator.getJavaName() + ".convert2List(" + PluginUtils.lowerFirst(daoGenerator.getFileName()) + "." + daoGenerator.getListName() + "(" + primaryList.get(0).getField() + "List), " + dtoGenerator.getFileName() + ".class);");
            }

            //map
            JavaFileMethod map = new JavaFileMethod();
            map.returnType("Map<" + primaryReg.packageName + ", " + dtoName + ">").method("get" + javaName + "MapBy" + primaryKey)
                    .params("List<" + primaryReg.packageName + "> " + primaryList.get(0).getField() + "List");
            if (isImpl) {
                String body = "List<" + dtoName + "> list = get" + javaName + "List(" + primaryList.get(0).getField() + "List);" + "\n" +
                        "    if (list != null && list.size() > 0) {" + "\n" +
                        "        Map<" + primaryReg.packageName + ", " + dtoName + "> map = new HashMap<>();" + "\n" +
                        "        for (" + dtoName + " dto : list) {" + "\n" +
                        "            map.put(dto." + "get" + PluginUtils.javaName(primaryList.get(0).getField(), true) + "(), dto);" + "\n" +
                        "        }" + "\n" +
                        "        return map;" + "\n" +
                        "    }" + "\n" +
                        "    return null;" + "\n";
                map.body(body);
            }

            methodList.add(getList);
            methodList.add(map);
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
    protected String getSubPackage() {
        return "service" + (isImpl ? ".impl" : "");
    }

    @Override
    public void aroundFile(Around around, JavaFile javaFile, StringBuilder sb) {
        super.aroundFile(around, javaFile, sb);
        if (around == Around.after && isImpl) {
            //copy
            new DTOConvertGenerator(configInfo.getWriteFilePath(), configInfo.getPackagePath()).generator();
        }
    }
}
