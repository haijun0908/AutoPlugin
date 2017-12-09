package com.plugin.auto.script.java;

import com.intellij.openapi.util.io.FileUtil;

import java.io.File;
import java.io.InputStream;

public class DTOConvertGenerator {

    private String filePath;
    private String packageName;

    public DTOConvertGenerator(String filePath, String packageName) {
        this.filePath = filePath;
        this.packageName = packageName;
    }

    public void generator() {
        try {
            InputStream is = getClass().getResourceAsStream("/DtoConvert.txt");
            File file = new File(filePath + File.separator + packageName.replaceAll("[.]", File.separator) + File.separator + "utils");
            file.mkdirs();
            byte[] b = new byte[is.available()];
            is.read(b);
            String s = new String(b);
            String f = "package " + packageName + "." + getSubPackage() + ";\n" + s;
            FileUtil.writeToFile(new File(file.getPath() + File.separator + getJavaName() + ".java"), f);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getSubPackage() {
        return "utils";
    }

    public String getJavaName() {
        return "DtoConvert";
    }

}
