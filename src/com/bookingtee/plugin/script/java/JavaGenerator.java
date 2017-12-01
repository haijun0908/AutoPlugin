package com.bookingtee.plugin.script.java;

import com.bookingtee.plugin.info.*;
import com.bookingtee.plugin.script.BaseGenerator;
import com.bookingtee.plugin.utils.JavaFileOut;
import com.bookingtee.plugin.utils.PluginUtils;

import java.util.List;

public abstract class JavaGenerator extends BaseGenerator {
    public JavaGenerator(DatabaseConfigInfo configInfo, List<TableInfo> tableInfoList) {
        super(configInfo, tableInfoList);
    }

    /**
     * 需要输出几次文件
     *
     * @return
     */
    protected abstract int writeTimes();

    /**
     * 当前是第几次文件
     *
     * @param currentTime
     * @return
     */
    protected abstract void resetCurrentTime(int currentTime, TableInfo tableInfo);


    protected abstract String getFileName();
    protected abstract List<String> getImportList();
    protected abstract boolean isAbstract();
    protected abstract String getFileAnno();
    protected abstract JavaFile.FileType getFileType();
    protected abstract String getParentClass();
    protected abstract List<String> getImplClassList();
    protected abstract List<JavaFileField> getFieldList();
    protected abstract List<JavaFileMethod> getMethodList();
    protected abstract String getSubPackage();

    @Override
    protected void generator() {
        int times = writeTimes();
        for (int i = 0; i < times; i++) {
            resetCurrentTime(i + 1, currentTable);
            JavaFile file = new JavaFile();
            file.setWriteFilePath("/Users/Jun/Documents/idea_workspace/AutoPlugin/OutFile/src/");
            file.setFileName(getFileName());
            file.setPackagePath(configInfo.getPackagePath() + "." + getSubPackage());
            file.setImportList(getImportList());
            file.setAbstract(isAbstract());
            file.setFileAnno(getFileAnno());
            file.setFileType(getFileType());
            file.setParentClass(getParentClass());
            file.setImplClassList(getImplClassList());
            file.setFieldList(getFieldList());
            file.setMethodList(getMethodList());

            JavaFileOut.writeFile(file);

        }
    }

    public String getFullName(){
        return this.configInfo.getPackagePath() + "." + this.getSubPackage() + "." + this.getFileName();
    }
}
