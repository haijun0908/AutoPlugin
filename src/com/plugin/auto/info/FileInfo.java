package com.plugin.auto.info;

import com.plugin.auto.common.WriteFileType;

public class FileInfo {
    private WriteFileType writeFileType;
    private String filePath;
    private String fileName;


    public WriteFileType getWriteFileType() {
        return writeFileType;
    }

    public void setWriteFileType(WriteFileType writeFileType) {
        this.writeFileType = writeFileType;
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

    public FileInfo writeFileType(WriteFileType writeFileType) {
        this.writeFileType = writeFileType;
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
