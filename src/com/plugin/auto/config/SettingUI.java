package com.plugin.auto.config;

import com.intellij.openapi.options.Configurable;
import com.intellij.openapi.options.ConfigurationException;
import com.plugin.auto.info.DatabaseConfigInfo;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

public class SettingUI implements Configurable {
    private JPanel panel1;
    private JButton addBtn;
    private JButton editBtn;
    private JTable configList;
    private JButton deleteBtn;

    private DefaultTableModel defaultTableModel;

    private List<DatabaseConfigInfo> configInfoList;

    public SettingUI() {

        addBtn.addActionListener(e -> showConfigDialog(null));
        editBtn.addActionListener(e -> showConfigDialog(getSelectConfigInfo()));
        deleteBtn.addActionListener(e -> {
            if (getSelectConfigInfo() != null) {
                configInfoList.remove(getSelectConfigInfo());
                refreshList();
            }
            notifyBtnStatus();
        });

        configList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                notifyBtnStatus();
            }
        });

    }

    private void notifyBtnStatus() {
        if (getSelectConfigInfo() != null) {
            editBtn.setEnabled(true);
            deleteBtn.setEnabled(true);
        } else {
            editBtn.setEnabled(false);
            deleteBtn.setEnabled(false);
        }
    }

    private DatabaseConfigInfo getSelectConfigInfo() {
        if (configList.getSelectedRow() > -1) {
            return configInfoList.get(configList.getSelectedRow());
        } else {
            return null;
        }
    }

    private void showConfigDialog(final DatabaseConfigInfo oldConfig) {
        DatabaseConfigDialog dialog = new DatabaseConfigDialog(null);
        dialog.setConfigInfo(oldConfig);
        dialog.setCallback((dialog1, item) -> {
            if (oldConfig != null && item != null) {
                //编辑
                int oldIndex = configInfoList.indexOf(oldConfig);
                configInfoList.remove(oldConfig);
                configInfoList.add(oldIndex, item);
            } else if (oldConfig == null && item != null) {
                //新增
                configInfoList.add(item);
            }
            refreshList();
        });
        dialog.pack();
        dialog.show();
    }

    /**
     * 刷新列表
     */
    private void refreshList() {
        setDataInTable(defaultTableModel);
    }


    @Nls
    @Override
    public String getDisplayName() {
        return "AutoPluginSetting";
    }

    @Nullable
    @Override
    public JComponent createComponent() {
        return panel1;
    }

    @Override
    public boolean isModified() {
        return true;
    }

    @Override
    public void apply() throws ConfigurationException {
        DatabaseConfigInfo.saveList(configInfoList);
    }

    private void createUIComponents() {
        configInfoList = DatabaseConfigInfo.getExistList();
        defaultTableModel = new DefaultTableModel();
        setDataInTable(defaultTableModel);
        configList = new JTable(defaultTableModel) {
            public void tableChanged(TableModelEvent e) {
                super.tableChanged(e);
                repaint();
            }
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        configList.getTableHeader().setPreferredSize(new Dimension(configList.getTableHeader().getWidth(), 30));
        configList.setRowHeight(25);
    }

    private void setDataInTable(DefaultTableModel dm) {
        if (configInfoList == null || configInfoList.size() <= 0) {
            dm.setDataVector(null, new Object[]{"Name", "Link"});
            return;
        }
        Object[][] object = new Object[configInfoList.size()][2];
        for (int i = 0; i < configInfoList.size(); i++) {
            for (int j = 0; j < 2; j++) {
                switch (j) {
                    case 0:
                        object[i][j] = configInfoList.get(i).getName();
                        break;
                    case 1:
                        object[i][j] = configInfoList.get(i).getDisplayLink();
                        break;
                    default:
                        break;
                }
            }
        }
        dm.setDataVector(object, new Object[]{"Name", "Link"});
    }
}
