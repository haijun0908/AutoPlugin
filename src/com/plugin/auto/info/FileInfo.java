package com.plugin.auto.info;

public class FileInfo {
    private boolean canOverwrite;
    private String filePath;
    private String fileName;

    public boolean isCanOverwrite() {
        return canOverwrite;
    }

    public void setCanOverwrite(boolean canOverwrite) {
        this.canOverwrite = canOverwrite;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public FileInfo canOverwrite(boolean canOverwrite) {
        this.canOverwrite = canOverwrite;
        return this;
    }

    public FileInfo filePath(String filePath) {
        this.filePath = filePath;
        return this;
    }

    public FileInfo fileName(String fileName) {
        this.fileName = fileName;
        return this;
    }
}
