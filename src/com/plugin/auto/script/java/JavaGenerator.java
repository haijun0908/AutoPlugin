package com.plugin.auto.script.java;

import com.plugin.auto.common.WriteFileType;
import com.plugin.auto.common.WriteJavaFileListener;
import com.plugin.auto.info.*;
import com.plugin.auto.script.BaseGenerator;
import com.plugin.auto.utils.JavaFileOut;
import com.sun.org.apache.bcel.internal.generic.NEW;

import java.util.List;

public abstract class JavaGenerator extends BaseGenerator implements WriteJavaFileListener {
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

    protected abstract String getFileComment();

    protected abstract JavaFile.FileType getFileType();

    protected abstract String getParentClass();

    protected abstract List<String> getImplClassList();

    protected abstract List<JavaFileField> getFieldList();

    protected abstract List<JavaFileMethod> getMethodList();

    protected abstract String getPackagePath();

    protected abstract String getFilePath();

    protected abstract String getAnno();

    @Override
    protected void generator() {
        int times = writeTimes();
        for (int i = 0; i < times; i++) {
            resetCurrentTime(i + 1, currentTable);
            JavaFile file = new JavaFile();
            file.setFilePath(getFilePath());
            file.setFileName(getFileName());
            file.setPackagePath(getPackagePath());
            file.setImportList(getImportList());
            file.setAbstract(isAbstract());
            file.setFileComment(getFileComment());
            file.setFileType(getFileType());
            file.setParentClass(getParentClass());
            file.setImplClassList(getImplClassList());
            file.setFieldList(getFieldList());
            file.setMethodList(getMethodList());
            file.setAnno(getAnno());
            file.setWriteFileType(getWriteFileType());

            new JavaFileOut(file).setListener(this).writeFile();

        }
    }

    protected WriteFileType getWriteFileType() {
        return WriteFileType.NEW;
    }


    public String getFullName() {
        return getPackagePath() + "." + this.getFileName();
    }

    @Override
    public void aroundFile(Around around, JavaFile javaFile, StringBuilder sb) {

    }

    @Override
    public void aroundPackage(Around around, JavaFile javaFile, StringBuilder sb) {

    }

    @Override
    public void aroundImport(Around around, JavaFile javaFile, StringBuilder sb) {

    }

    @Override
    public void aroundJavaName(Around around, JavaFile javaFile, StringBuilder sb) {

    }

    @Override
    public void aroundJavaField(Around around, JavaFile javaFile, StringBuilder sb) {

    }

    @Override
    public void aroundJavaMethod(Around around, JavaFile javaFile, StringBuilder sb) {

    }
}
