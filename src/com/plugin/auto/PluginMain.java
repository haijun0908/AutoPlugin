package com.plugin.auto;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;

public class PluginMain extends AnAction {
    @Override
    public void actionPerformed(AnActionEvent anActionEvent) {
        System.out.println("plugin  main");
    }
}
