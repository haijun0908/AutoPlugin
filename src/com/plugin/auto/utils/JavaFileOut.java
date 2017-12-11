package com.plugin.auto.utils;

import com.intellij.openapi.util.io.FileUtil;
import com.plugin.auto.common.WriteJavaFileListener;
import com.plugin.auto.info.JavaFile;
import com.plugin.auto.info.JavaFileField;
import com.plugin.auto.info.JavaFileMethod;
import org.apache.commons.lang3.StringUtils;

import java.io.File;

public class JavaFileOut {

    private static StringBuilder sb;
    private static String SPACE = " ";

    public static void writeFile(JavaFile javaFile , WriteJavaFileListener listener) {
        sb = new StringBuilder();

        File basePath = new File(javaFile.getWriteFilePath() + File.separator + javaFile.getPackagePath().replaceAll("[.]", File.separator));
        basePath.mkdirs();

        File file = new File(basePath, javaFile.getFileName() + ".java");
        try {
            if(!javaFile.isCanOverwrite() && file.exists() && file.isFile()){
                //不允许重写。备份之前的文件
                FileUtil.copy(file , new File(basePath , javaFile.getFileName() + ".java" + ".old"));
            }


            if(listener != null) listener.aroundFile(WriteJavaFileListener.Around.before , javaFile , sb);

            if(listener != null) listener.aroundPackage(WriteJavaFileListener.Around.before , javaFile , sb);
            //包名
            append("package " + javaFile.getPackagePath() + ";");
            if(listener != null) listener.aroundPackage(WriteJavaFileListener.Around.after , javaFile , sb);

            appendLine();

            //依赖包
            if(listener != null) listener.aroundImport(WriteJavaFileListener.Around.before , javaFile , sb);
            if (javaFile.getImportList() != null && javaFile.getImportList().size() > 0) {
                for (String importVo : javaFile.getImportList()) {
                    if(StringUtils.isBlank(importVo)){
                        appendLine();
                    }else {
                        append("import " + importVo + ";");
                    }
                }
            }
            if(listener != null) listener.aroundImport(WriteJavaFileListener.Around.after , javaFile , sb);
            appendLine();

            if(listener != null) listener.aroundJavaName(WriteJavaFileListener.Around.before , javaFile , sb);
            //文件名
            if (StringUtils.isNotBlank(javaFile.getFileComment())) {
                append("/**");
                append(" * " + javaFile.getFileComment());
                append(" */");
            }
            if(StringUtils.isNotBlank(javaFile.getAnno())){
                append("@" + javaFile.getAnno());
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
            if(listener != null) listener.aroundJavaName(WriteJavaFileListener.Around.after , javaFile , sb);

            appendLine();

            if(listener != null) listener.aroundJavaField(WriteJavaFileListener.Around.before , javaFile , sb);
            //字段
            if (javaFile.getFieldList() != null && javaFile.getFieldList().size() > 0) {
                for (JavaFileField field : javaFile.getFieldList()) {
                    append(field.getCode(), 1);
                    appendLine();
                }
            }
            if(listener != null) listener.aroundJavaField(WriteJavaFileListener.Around.after , javaFile , sb);


            if(listener != null) listener.aroundJavaMethod(WriteJavaFileListener.Around.before , javaFile , sb);
            //方法
            if (javaFile.getMethodList() != null && javaFile.getMethodList().size() > 0) {
                for (JavaFileMethod method : javaFile.getMethodList()) {
                    append(method.getCode(), 1);
                    appendLine();
                }
            }
            if(listener != null) listener.aroundJavaMethod(WriteJavaFileListener.Around.after , javaFile , sb);

            append("}");
            FileUtil.writeToFile(file, sb.toString());
            if(listener != null) listener.aroundFile(WriteJavaFileListener.Around.after , javaFile , sb);

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
