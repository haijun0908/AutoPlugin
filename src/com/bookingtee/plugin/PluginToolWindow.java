package com.bookingtee.plugin;

import com.bookingtee.plugin.db.TableUtils;
import com.bookingtee.plugin.info.DatabaseConfigInfo;
import com.bookingtee.plugin.info.TableInfo;
import com.bookingtee.plugin.menu.TableRightMenu;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.JBMenuItem;
import com.intellij.openapi.ui.JBPopupMenu;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowFactory;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.ContentFactory;

import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.table.DefaultTableModel;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public class PluginToolWindow implements ToolWindowFactory {

    private JPanel myToolWindowContent;
    private JComboBox selectList;
    private JButton configBtn;
    private JButton connectBtn;
    private JButton disConnectBtn;
    private JLabel loading;
    private JTable table1;
    private ToolWindow myToolWindow;

    private TableUtils tableUtils;
    private List<DatabaseConfigInfo> configInfoList;
    private DefaultTableModel defaultTableModel;
    private DatabaseConfigInfo selectConfigInfo;

    public PluginToolWindow() {
        configInfoList = DatabaseConfigInfo.getExistList();
        configInfoList.add(0, null);
        configBtn.addActionListener(e -> {
        });
        connectBtn.addActionListener(e -> {
            connectJDBC();
            showTables();
        });
        disConnectBtn.addActionListener(e -> disConnectJDBC());
        selectList.addItemListener(e -> {

        });
        selectList.addActionListener(e -> notifyBtnStatus());
        if (configInfoList != null && configInfoList.size() > 0) {
            for (int i = 0; i < configInfoList.size(); i++) {
                if (configInfoList.get(i) != null) {
                    selectList.addItem(configInfoList.get(i).getName());
                } else {
                    selectList.addItem(null);
                }
            }
        }
    }

    /**
     * 显示表明列表
     */
    private void showTables() {
        String[][] tables = new String[tableUtils.getTables().size()][2];
        if (tableUtils.getTables() != null && tableUtils.getTables().size() > 0) {
            for (int i = 0; i < tableUtils.getTables().size(); i++) {
                tables[i][0] = tableUtils.getTables().get(i).getTableName();
                tables[i][1] = tableUtils.getTables().get(i).getTableName();
            }
        } else {
            tables = new String[0][2];
        }
        defaultTableModel.setDataVector(tables, new Object[]{"table", "属性名(双击编辑)"});
    }

    private void notifyBtnStatus() {
        if (getSelectConfig() != null) {
            connectBtn.setEnabled(true);
        } else {
            connectBtn.setEnabled(false);
        }
    }

    private DatabaseConfigInfo getSelectConfig() {
        if (selectList.getSelectedItem() != null) {
            return configInfoList.get(selectList.getSelectedIndex());
        } else {
            return null;
        }
    }

    /**
     * 断开连接
     */
    private void disConnectJDBC() {
        tableUtils = null;
    }

    /**
     * 建立连接
     */
    private void connectJDBC() {
        loading.setVisible(true);
        DatabaseConfigInfo configInfo = getSelectConfig();
        tableUtils = new TableUtils(configInfo);
        this.selectConfigInfo = configInfo;
        loading.setVisible(false);
    }

    @Override
    public void createToolWindowContent(Project project, ToolWindow toolWindow) {
        myToolWindow = toolWindow;
        ContentFactory contentFactory = ContentFactory.SERVICE.getInstance();
        Content content = contentFactory.createContent(myToolWindowContent, "", false);
        toolWindow.getContentManager().addContent(content);
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("PluginToolWindow");
        frame.setContentPane(new PluginToolWindow().myToolWindowContent);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

    private void createUIComponents() {
        defaultTableModel = new DefaultTableModel();
        table1 = new JTable(defaultTableModel) {
            public void tableChanged(TableModelEvent e) {
                super.tableChanged(e);
                repaint();
            }

            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 1;
            }
        };
        table1.setRowHeight(30);
        table1.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                if (SwingUtilities.isRightMouseButton(e)) {
                    //右键
                    int rightRow = table1.rowAtPoint(e.getPoint());
                    showRightMenu(rightRow, e);
                }
            }
        });
    }

    private void showRightMenu(int rightRow, MouseEvent e) {
        if (rightRow <= -1) {
            return;
        }
        int[] selectRows = table1.getSelectedRows();
        if (selectRows == null || selectRows.length <= 0) {
            return;
        }
        List<TableInfo> selectTable = new ArrayList<>();
        for (int selectRow : selectRows) {
            String name = (String) ((Vector) defaultTableModel.getDataVector().get(selectRow)).get(1);
            tableUtils.getTables().get(selectRow).setTableName(name);
            selectTable.add(tableUtils.getTables().get(selectRow));
        }
        new TableRightMenu(selectConfigInfo, selectTable, table1, e.getX(), e.getY());
    }
}
