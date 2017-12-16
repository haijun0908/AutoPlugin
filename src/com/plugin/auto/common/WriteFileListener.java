package com.plugin.auto.common;

public interface WriteFileListener<T> {

    void aroundFile(Around around, T t, StringBuilder sb);

    enum Around {
        before,
        after
    }
}
