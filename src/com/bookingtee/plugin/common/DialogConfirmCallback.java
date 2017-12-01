package com.bookingtee.plugin.common;

import com.intellij.openapi.ui.DialogWrapper;

import javax.swing.*;

public interface DialogConfirmCallback<T> {

    void confirm(DialogWrapper dialog , T item);

}
