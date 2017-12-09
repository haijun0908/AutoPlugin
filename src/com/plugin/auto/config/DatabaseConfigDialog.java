package com.plugin.auto.config;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.openapi.ui.Messages;
import com.plugin.auto.common.DialogConfirmCallback;
import com.plugin.auto.info.DatabaseConfigInfo;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileWriter;

public class DatabaseConfigDialog extends DialogWrapper {
    private static final long serialVersionUID = -317495679511983280L;
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTextField name;
    private JTextField host;
    private JTextField port;
    private JTextField user;
    private JTextField password;
    private JTextField db;
    private JButton openButton;
    private JTextField packageName;
    private JLabel folder;

    private DatabaseConfigInfo configInfo;
    private DialogConfirmCallback<DatabaseConfigInfo> callback;

    protected DatabaseConfigDialog(@Nullable Project project) {
        super(project);
    }

    @Override
    public void show() {
        this.init();
        super.show();
    }

    @Override
    protected void init() {
        super.init();
        initDialog();
        if (configInfo != null) {
            name.setText(configInfo.getName());
            host.setText(configInfo.getHost());
            port.setText(configInfo.getPort());
            user.setText(configInfo.getUser());
            password.setText(configInfo.getPwd());
            db.setText(configInfo.getDb());
            name.setEnabled(false);
            folder.setText(configInfo.getWriteFilePath());
            packageName.setText(configInfo.getPackagePath());
        }
    }

    @NotNull
    @Override
    protected Action[] createActions() {
        return new Action[]{};
    }

    public void setConfigInfo(DatabaseConfigInfo configInfo) {
        this.configInfo = configInfo;
    }

    public void initDialog() {
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);

        buttonOK.addActionListener(e -> onOK());

        buttonCancel.addActionListener(e -> onCancel());

        openButton.addActionListener(e -> {
            JFileChooser jf = new JFileChooser();
            jf.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            jf.setAcceptAllFileFilterUsed(false);
            if(StringUtils.isNotBlank(folder.getText())){
                jf.setCurrentDirectory(new File(folder.getText()));
            }
            int status = jf.showDialog(null, "确定");
            if(status == JFileChooser.APPROVE_OPTION){
                File fi = jf.getSelectedFile();
                if (fi != null) {
                    if (fi.isFile()) {
                        folder.setText(fi.getParentFile().getPath());
                    } else {
                        if(!fi.exists()){
                            fi = fi.getParentFile();
                        }
                        folder.setText(fi.getPath());
                    }
                }
            }
        });

        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(e -> onCancel(), KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    private void onOK() {
        if (StringUtils.isAnyBlank(name.getText(), host.getText(), port.getText(), user.getText(), password.getText(), db.getText(), folder.getText(), packageName.getText())) {
            Messages.showMessageDialog("请输入完整的信息", "Error", Messages.getInformationIcon());
            return;
        }

        if (configInfo == null) {
            configInfo = new DatabaseConfigInfo();
        }

        configInfo.setHost(host.getText());
        configInfo.setPort(port.getText());
        configInfo.setDb(db.getText());
        configInfo.setUser(user.getText());
        configInfo.setPwd(password.getText());
        configInfo.setName(name.getText());
        configInfo.setWriteFilePath(folder.getText());
        configInfo.setPackagePath(packageName.getText());

        callback.confirm(this, configInfo);

        dispose();

    }

    private void onCancel() {
        dispose();
    }

    public void setCallback(DialogConfirmCallback<DatabaseConfigInfo> callback) {
        this.callback = callback;
    }

    @Nullable
    @Override
    protected JComponent createCenterPanel() {
        return contentPane;
    }
}
