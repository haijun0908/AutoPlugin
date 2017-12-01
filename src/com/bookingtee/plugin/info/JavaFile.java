package com.bookingtee.plugin.info;

import java.util.List;

public class JavaFile {
    private String writeFilePath;
    private String fileName;
    private String packagePath;
    private List<String> importList;
    private boolean isAbstract = false;
    private String fileAnno;
    private FileType fileType;

    private String parentClass;
    private List<String> implClassList;
    private List<JavaFileField> fieldList;
    private List<JavaFileMethod> methodList;

    public String getWriteFilePath() {
        return writeFilePath;
    }

    public void setWriteFilePath(String writeFilePath) {
        this.writeFilePath = writeFilePath;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getPackagePath() {
        return packagePath;
    }

    public void setPackagePath(String packagePath) {
        this.packagePath = packagePath;
    }

    public List<String> getImportList() {
        return importList;
    }

    public void setImportList(List<String> importList) {
        this.importList = importList;
    }

    public boolean isAbstract() {
        return isAbstract;
    }

    public void setAbstract(boolean anAbstract) {
        isAbstract = anAbstract;
    }

    public String getFileAnno() {
        return fileAnno;
    }

    public void setFileAnno(String fileAnno) {
        this.fileAnno = fileAnno;
    }

    public FileType getFileType() {
        return fileType;
    }

    public void setFileType(FileType fileType) {
        this.fileType = fileType;
    }

    public String getParentClass() {
        return parentClass;
    }

    public void setParentClass(String parentClass) {
        this.parentClass = parentClass;
    }

    public List<String> getImplClassList() {
        return implClassList;
    }

    public void setImplClassList(List<String> implClassList) {
        this.implClassList = implClassList;
    }

    public List<JavaFileField> getFieldList() {
        return fieldList;
    }

    public void setFieldList(List<JavaFileField> fieldList) {
        this.fieldList = fieldList;
    }

    public List<JavaFileMethod> getMethodList() {
        return methodList;
    }

    public void setMethodList(List<JavaFileMethod> methodList) {
        this.methodList = methodList;
    }

    public static enum FileType {
        CLASS("class"),
        INTERFACE("interface");

        private String fileType;
        FileType(String fileType){
            this.fileType = fileType;
        }

        public String getFileType() {
            return fileType;
        }
    }

}
