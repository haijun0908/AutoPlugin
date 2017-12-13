package com.plugin.auto.utils;

import com.plugin.auto.PluginToolWindow;

public class ProgressText {
    private static PluginToolWindow toolWindow;

    private ProgressText() {
    }

    public static void regTool(PluginToolWindow toolWindow) {
        ProgressText.toolWindow = toolWindow;
    }

    public static void setProgress(String text) {
        if (toolWindow != null) {
            toolWindow.setProgress(text);
        } else {
            System.out.println(text);
        }
    }


}
