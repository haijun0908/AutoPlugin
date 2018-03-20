package com.plugin.auto.utils;

import com.intellij.openapi.util.io.FileUtil;
import com.intellij.xml.actions.xmlbeans.FileUtils;
import com.plugin.auto.common.WriteFileListener;
import org.sqlite.util.StringUtils;
import sun.applet.Main;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public abstract class FileOut<T> {

    protected StringBuilder sb;
    protected String SPACE = " ";

    abstract boolean canOverwrite();

    abstract File getWriteFile();

    abstract StringBuilder getFileContent();

    private WriteFileListener<T> writeFileListener;
    private T t;

    public FileOut(T t) {
        this.t = t;
        sb = new StringBuilder();
    }

    public void writeFile() {
        try {
            File file = getWriteFile();
            if (writeFileListener != null) {
                writeFileListener.aroundFile(WriteFileListener.Around.before, t, sb);
            }
            StringBuilder content = getFileContent();

            //先判断路径是否存在
            file.getParentFile().mkdirs();

            if (file.exists() && file.isFile() && !canOverwrite()) {
                //不允许覆盖
                //同时比较2者文件是否一致
                if (!(content.toString().equals(com.plugin.auto.utils.FileUtils.readToString(file)))) {
                    FileUtil.copy(file, new File(file.getParentFile() + File.separator + file.getName() + "." + getTime()));
                }
            }
            FileUtil.writeToFile(file, content.toString());
            if (writeFileListener != null) {
                writeFileListener.aroundFile(WriteFileListener.Around.after, t, content);
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String getTime() {
        Date d = new Date();
        return new SimpleDateFormat("HH_mm_ss").format(d);
    }

    public FileOut setWriteFileListener(WriteFileListener<T> writeFileListener) {
        this.writeFileListener = writeFileListener;
        return this;
    }

    protected void appendLine() {
        append("");
    }

    protected void append(String content) {
        append(content, 0);
    }

    protected void append(String content, int tabCount) {
        if (content == null) {
            return;
        }
        String[] lines = content.split("\n");
        String tab = "";
        for (int i = 0; i < tabCount; i++) {
            tab += "    ";
        }
        for (String line : lines) {
            sb.append(tab).append(line).append("\n");
        }
    }




    public static void main(String[] args){
        System.out.println(File.separator);
        System.out.println(org.apache.commons.lang.StringUtils.replace("D:.安装环境的软件.scrt安装包", ".", File.separator));
    }
}
