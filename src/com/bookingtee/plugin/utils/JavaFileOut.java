package com.bookingtee.plugin.utils;

import com.bookingtee.plugin.info.JavaFile;
import com.bookingtee.plugin.info.JavaFileField;
import com.bookingtee.plugin.info.JavaFileMethod;
import com.intellij.openapi.util.io.FileUtil;
import org.apache.commons.lang3.StringUtils;

import java.io.File;

public class JavaFileOut {

    private static StringBuilder sb;
    private static String SPACE = " ";

    public static void writeFile(JavaFile javaFile) {
        sb = new StringBuilder();

        File basePath = new File(javaFile.getWriteFilePath() + File.separator + javaFile.getPackagePath().replaceAll("[.]", File.separator));
        basePath.mkdirs();

        File file = new File(basePath, javaFile.getFileName() + ".java");
        try {
            //包名
            append("package " + javaFile.getPackagePath() + ";");

            appendLine();

            //依赖包
            if (javaFile.getImportList() != null && javaFile.getImportList().size() > 0) {
                for (String importVo : javaFile.getImportList()) {
                    if(StringUtils.isBlank(importVo)){
                        appendLine();
                    }else {
                        append("import " + importVo + ";");
                    }
                }
            }
            appendLine();

            //文件名
            if (StringUtils.isNotBlank(javaFile.getFileAnno())) {
                append("/**");
                append(" * " + javaFile.getFileAnno());
                append(" */");
            }
            String fileName = "public" + (javaFile.isAbstract() ? (SPACE + "abstract") : "") + SPACE + javaFile.getFileType().getFileType() + SPACE;
            fileName += javaFile.getFileName();
            if (StringUtils.isNotBlank(javaFile.getParentClass())) {
                fileName += SPACE + "extends" + SPACE + javaFile.getParentClass();
            }
            if (javaFile.getImplClassList() != null && javaFile.getImplClassList().size() > 0) {
                fileName += SPACE + "implements" + SPACE;
                for (String s : javaFile.getImplClassList()) {
                    fileName += s + ",";
                }
                fileName = fileName.substring(0, fileName.length() - 1);
            }
            fileName += SPACE + "{";
            append(fileName);

            appendLine();

            //字段
            if (javaFile.getFieldList() != null && javaFile.getFieldList().size() > 0) {
                for (JavaFileField field : javaFile.getFieldList()) {
                    append(field.getCode(), 1);
                    appendLine();
                }
            }

            //方法
            if (javaFile.getMethodList() != null && javaFile.getMethodList().size() > 0) {
                for (JavaFileMethod method : javaFile.getMethodList()) {
                    append(method.getCode(), 1);
                    appendLine();
                }
            }

            append("}");
            FileUtil.writeToFile(file, sb.toString());

        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    private static void appendLine() {
        append("");
    }

    private static void append(String content) {
        append(content, 0);
    }

    private static void append(String content, int tabCount) {
        String[] lines = content.split("\n");
        String tab = "";
        for (int i = 0; i < tabCount; i++) {
            tab += "    ";
        }
        for (String line : lines) {
            sb.append(tab).append(line).append("\n");
        }
    }

}
