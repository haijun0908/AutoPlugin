package com.plugin.auto.config;

import com.intellij.openapi.fileChooser.FileChooser;
import com.intellij.openapi.fileChooser.FileChooserDescriptorFactory;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectManager;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.vfs.VirtualFile;
import com.plugin.auto.common.DialogConfirmCallback;
import com.plugin.auto.db.TableUtils;
import com.plugin.auto.info.DatabaseConfigInfo;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.event.KeyEvent;

public class DatabaseConfigDialog extends DialogWrapper {
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
    private JButton test;

    private DatabaseConfigInfo configInfo;
    private DialogConfirmCallback<DatabaseConfigInfo> callback;
    private VirtualFile virtualFile;
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
            virtualFile = FileChooser.chooseFile(FileChooserDescriptorFactory.createSingleFolderDescriptor(), ProjectManager.getInstance().getDefaultProject(), virtualFile);
            if (virtualFile != null) {
                folder.setText(virtualFile.getPath());
            }
        });

        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(e -> onCancel(), KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);

        test.addActionListener(e -> testConnect());
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

    private void testConnect() {
        DatabaseConfigInfo info = new DatabaseConfigInfo();
        info.setHost(host.getText());
        info.setPort(port.getText());
        info.setDb(db.getText());
        info.setUser(user.getText());
        info.setPwd(password.getText());
        TableUtils tableUtils = new TableUtils(info);
        boolean canConnect = tableUtils.canConnect();
        if(canConnect){
            Messages.showMessageDialog("Connect successful", "OK", Messages.getInformationIcon());
        }else{
            Messages.showMessageDialog("Connect Error!!", "Error", Messages.getErrorIcon());
        }
    }

    @Nullable
    @Override
    protected JComponent createCenterPanel() {
        return contentPane;
    }
}
