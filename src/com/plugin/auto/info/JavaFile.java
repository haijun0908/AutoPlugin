package com.plugin.auto.info;

import java.util.List;

public class JavaFile {
    private String writeFilePath;
    private String fileName;
    private String packagePath;
    private List<String> importList;
    private boolean isAbstract = false;
    private String fileComment;
    private FileType fileType;
    private String anno;
    private boolean canOverwrite = true;

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

    public String getFileComment() {
        return fileComment;
    }

    public void setFileComment(String fileComment) {
        this.fileComment = fileComment;
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

    public String getAnno() {
        return anno;
    }

    public void setAnno(String anno) {
        this.anno = anno;
    }

    public boolean isCanOverwrite() {
        return canOverwrite;
    }

    public void setCanOverwrite(boolean canOverwrite) {
        this.canOverwrite = canOverwrite;
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
