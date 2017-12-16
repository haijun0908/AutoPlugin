package com.plugin.auto.common;

import com.plugin.auto.info.JavaFile;

public interface WriteJavaFileListener extends WriteFileListener<JavaFile> {

    void aroundPackage(Around around, JavaFile javaFile, StringBuilder sb);

    void aroundImport(Around around, JavaFile javaFile, StringBuilder sb);

    void aroundJavaName(Around around, JavaFile javaFile, StringBuilder sb);

    void aroundJavaField(Around around, JavaFile javaFile, StringBuilder sb);

    void aroundJavaMethod(Around around, JavaFile javaFile, StringBuilder sb);


}
