package com.bookingtee.plugin.menu;


import com.bookingtee.plugin.info.DatabaseConfigInfo;
import com.bookingtee.plugin.script.BaseGenerator;
import com.bookingtee.plugin.common.RightMenuItem;
import com.bookingtee.plugin.info.TableInfo;
import com.intellij.openapi.ui.JBMenuItem;
import com.intellij.openapi.ui.JBPopupMenu;
import com.intellij.openapi.util.IconLoader;

import java.awt.*;
import java.lang.reflect.Constructor;
import java.util.List;

/**
 * 表名右键菜单
 */
public class TableRightMenu {
    private Component component;
    private int x;
    private int y;
    private List<TableInfo> tableInfoList;
    private DatabaseConfigInfo configInfo;

    public TableRightMenu(DatabaseConfigInfo configInfo, List<TableInfo> tableInfoList, Component component, int x, int y) {
        this.configInfo = configInfo;
        this.tableInfoList = tableInfoList;
        this.x = x;
        this.y = y;
        this.component = component;
        showMenu();
    }

    private void showMenu() {
        JBPopupMenu menu = new JBPopupMenu();
        menu.add(getItem(RightMenuItem.ALL));
        menu.add(getItem(RightMenuItem.NULL));
        menu.add(getItem(RightMenuItem.MODEL));
        menu.add(getItem(RightMenuItem.DAO));
        menu.add(getItem(RightMenuItem.SERVICE));
        menu.add(getItem(RightMenuItem.DTO));
        menu.show(component, x, y);
    }

    private JBMenuItem getItem(RightMenuItem menuItem) {
        JBMenuItem item = new JBMenuItem(menuItem.getItem());
        item.setIcon(IconLoader.findIcon(menuItem.getIcon()));
        item.addActionListener(e -> {
            clickItem(menuItem);
        });
        return item;
    }

    private void clickItem(RightMenuItem menuItem) {
        if (menuItem.getGenerator() != null || menuItem == RightMenuItem.ALL) {
            try {
                if (menuItem != RightMenuItem.ALL) {
                    Class clazz = menuItem.getGenerator();
                    Constructor c = clazz.getConstructor(DatabaseConfigInfo.class, List.class);
                    BaseGenerator p = (BaseGenerator) c.newInstance(configInfo, tableInfoList);
                    p.startGeneratorList();
                } else {
                    for (RightMenuItem item : RightMenuItem.values()) {
                        if (item.getGenerator() != null) {
                            clickItem(item);
                        }
                    }
                }
            } catch (Exception e1) {
                e1.printStackTrace();
                try {
                    throw e1;
                } catch (Exception e2) {
                    e2.printStackTrace();
                }
            }
        }
    }


}

