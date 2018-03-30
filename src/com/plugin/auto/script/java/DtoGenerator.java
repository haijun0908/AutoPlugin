package com.plugin.auto.script.java;

import com.plugin.auto.info.*;
import com.plugin.auto.utils.PluginUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DtoGenerator extends JavaGenerator {
    public static final int PARENT_TIME = 1;
    public static final int CHILD_TIME = 2;
    private boolean isBase = true;
    private TableInfo tableInfo;

    private List<JavaFileField> fieldList = new ArrayList<>();
    private List<JavaFileMethod> methodList = new ArrayList<>();

    public DtoGenerator(DatabaseConfigInfo configInfo, List<TableInfo> tableInfoList) {
        super(configInfo, tableInfoList);
    }

    @Override
    protected int writeTimes() {
        return 2;
    }

    @Override
    protected void resetCurrentTime(int currentTime, TableInfo tableInfo) {
        isBase = currentTime == PARENT_TIME;
        this.tableInfo = tableInfo;
        this.fieldList = new ArrayList<>();
        this.methodList = new ArrayList<>();

        //初始化一些数据
        if (isBase) {
            fieldList.add(new JavaFileField().access(JavaAccess.PRIVATE).type("long").field("serialVersionUID").defaultVal("-1L").modifier("static final"));


//            sb.append("id=").append(id);
//            sb.append(", name='").append(name).append('\'');
//            sb.append(", createTime=").append(createTime);
//            sb.append(", updateTime=").append(updateTime);
//            sb.append(", memberId=").append(memberId);
//            sb.append('}');
//            return sb.toString();
            String toStringBody = "StringBuffer sb = new StringBuffer(\"" + getFileName() + "{\");\n";
            boolean isFirst = true;
            for (ColumnInfo info : tableInfo.getColumnInfoList()) {
                PluginUtils.Reg reg = PluginUtils.reg(info);
                //field
                fieldList.add(new JavaFileField().comment(info.getComment()).field(PluginUtils.javaName(info.getCustomField(), false)).type(reg.type).access(JavaAccess.PRIVATE));
                //setMethod
                methodList.add(new JavaFileMethod().access(JavaAccess.PUBLIC).returnType("void").method("set" + PluginUtils.javaName(info.getCustomField(), true))
                        .params(reg.type + " " + info.getField()).body("this." + PluginUtils.javaName(info.getCustomField(), false) + " = " + PluginUtils.javaName(info.getCustomField(), false)+ ";")
                );
                //getMethod
                methodList.add(new JavaFileMethod().access(JavaAccess.PUBLIC).returnType(reg.type).method("get" + PluginUtils.javaName(info.getCustomField(), true))
                        .body("return this." + PluginUtils.javaName(info.getCustomField(), false) + ";")
                );
                toStringBody += "sb.append(\"" + (isFirst ? "" : ",") + " " + PluginUtils.javaName(info.getCustomField(), false) + "=\").append(" + PluginUtils.javaName(info.getCustomField(), false) + ");\n";
                isFirst = false;
            }
            toStringBody += "sb.append('}');\n";
            toStringBody += "return sb.toString();";
            methodList.add(new JavaFileMethod().access(JavaAccess.PUBLIC).returnType("String").method("toString").anno("Override").body(toStringBody));
        }
    }

    @Override
    protected String getFileName() {
        return PluginUtils.javaName(tableInfo.getTableName(), true) + (isBase ? "DTOBase" : "DTO");
    }

    @Override
    protected List<String> getImportList() {
        List<String> importList = new ArrayList<>();

        if (isBase) {
            importList.add("java.io.Serializable");
            importList.addAll(PluginUtils.getImportList(tableInfo.getColumnInfoList()));
        } else {
            DtoGenerator modelGenerator = new DtoGenerator(configInfo, Arrays.asList(tableInfo));
            modelGenerator.resetCurrentTime(1, tableInfo);
            importList.add(modelGenerator.getFullName());
        }

        return importList;
    }

    @Override
    protected boolean isAbstract() {
        return isBase;
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
        return isBase ? null : (PluginUtils.javaName(tableInfo.getTableName(), true) + "DTOBase");
    }

    @Override
    protected List<String> getImplClassList() {
        return isBase ? Arrays.asList("Serializable") : null;
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
        return configInfo.getDtoFilePath();
    }

    @Override
    protected String getPackagePath() {
        return configInfo.getDtoPackage() + (isBase ? ".base" : "") + tableInfo.getFirstSubPackage();
    }

    @Override
    protected String getAnno() {
        return null;
    }

    @Override
    protected boolean getCanOverwrite() {
        return isBase ? true : false;
    }
}
