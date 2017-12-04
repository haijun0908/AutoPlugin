package com.plugin.auto.common;

import com.intellij.openapi.ui.DialogWrapper;

public interface DialogConfirmCallback<T> {

    void confirm(DialogWrapper dialog , T item);

}
