package com.plugin.auto.config;

import com.intellij.openapi.fileChooser.FileChooser;
import com.intellij.openapi.fileChooser.FileChooserDescriptorFactory;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectManager;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.util.ui.JBUI;
import com.plugin.auto.common.DialogConfirmCallback;
import com.plugin.auto.db.TableUtils;
import com.plugin.auto.info.DatabaseConfigInfo;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.File;

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
    private JButton resourceBtn;
    private JLabel resourceFolder;
    private JButton modelOpen;
    private JTextField modelPackage;
    private JLabel modelFolder;
    private JButton dtoOpen;
    private JTextField dtoPackage;
    private JLabel dtoFolder;
    private JButton daoOpen;
    private JTextField daoPackage;
    private JLabel daoFolder;
    private JLabel serviceFolder;
    private JButton serviceOpen;
    private JTextField servicePackage;
    private JTabbedPane tabbedPane1;
    private JButton serviceImplOpen;
    private JLabel serviceImplFolder;
    private JTextField serviceImplPackage;

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
            resourceFolder.setText(configInfo.getResourcePath());
            dtoFolder.setText(configInfo.getDtoFilePath());
            dtoPackage.setText(configInfo.getDtoPackage());
            modelFolder.setText(configInfo.getModelFilePath());
            modelPackage.setText(configInfo.getModelPackage());
            serviceFolder.setText(configInfo.getServiceFilePath());
            servicePackage.setText(configInfo.getServicePackage());
            daoFolder.setText(configInfo.getDaoFilePath());
            daoPackage.setText(configInfo.getDaoPackage());
            serviceImplFolder.setText(configInfo.getServiceImplFilePath());
            serviceImplPackage.setText(configInfo.getServiceImplPackage());
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

        resourceFolder.setSize(100, 0);
        folder.setSize(100, 0);
        buttonOK.addActionListener(e -> onOK());

        buttonCancel.addActionListener(e -> onCancel());


        openButton.addActionListener(new OpenBtnListener(openButton  , folder));
        resourceBtn.addActionListener(new OpenBtnListener(resourceBtn  , resourceFolder));

        dtoOpen.addActionListener(new OpenBtnListener(dtoOpen  , dtoFolder));
        daoOpen.addActionListener(new OpenBtnListener(daoOpen  , daoFolder));
        modelOpen.addActionListener(new OpenBtnListener(modelOpen  , modelFolder));
        serviceOpen.addActionListener(new OpenBtnListener(serviceOpen  , serviceFolder));
        serviceImplOpen.addActionListener(new OpenBtnListener(serviceImplOpen , serviceImplFolder));

        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(e -> onCancel(), KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);

        test.addActionListener(e -> testConnect());
    }





    private void onOK() {
        if (StringUtils.isAnyBlank(name.getText(), host.getText(), port.getText(), user.getText(), password.getText(), db.getText(),
                folder.getText(), packageName.getText(), resourceFolder.getText())) {
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
        configInfo.setResourcePath(resourceFolder.getText());

        if(StringUtils.isBlank(dtoFolder.getText())){
            configInfo.setDtoFilePath(folder.getText());
        }else{
            configInfo.setDtoFilePath(dtoFolder.getText());
        }
        if(StringUtils.isBlank(dtoPackage.getText())){
            configInfo.setDtoPackage(packageName.getText() + ".dto");
        }else{
            configInfo.setDtoPackage(dtoPackage.getText());
        }

        if(StringUtils.isBlank(daoFolder.getText())){
            configInfo.setDaoFilePath(folder.getText());
        }else{
            configInfo.setDaoFilePath(daoFolder.getText());
        }
        if(StringUtils.isBlank(daoPackage.getText())){
            configInfo.setDaoPackage(packageName.getText() + ".dao");
        }else{
            configInfo.setDaoPackage(daoPackage.getText());
        }

        if(StringUtils.isBlank(modelFolder.getText())){
            configInfo.setModelFilePath(folder.getText());
        }else{
            configInfo.setModelFilePath(modelFolder.getText());
        }
        if(StringUtils.isBlank(modelPackage.getText())){
            configInfo.setModelPackage(packageName.getText() + ".model");
        }else{
            configInfo.setModelPackage(modelPackage.getText());
        }

        if(StringUtils.isBlank(serviceFolder.getText())){
            configInfo.setServiceFilePath(folder.getText());
        }else{
            configInfo.setServiceFilePath(serviceFolder.getText());
        }
        if(StringUtils.isBlank(servicePackage.getText())){
            configInfo.setServicePackage(packageName.getText() + ".service");
        }else{
            configInfo.setServicePackage(servicePackage.getText());
        }

        if(StringUtils.isBlank(serviceImplFolder.getText())){
            configInfo.setServiceImplFilePath(folder.getText());
        }else{
            configInfo.setServiceImplFilePath(serviceImplFolder.getText());
        }
        if(StringUtils.isBlank(serviceImplPackage.getText())){
            configInfo.setServiceImplPackage(packageName.getText() + ".service");
        }else{
            configInfo.setServiceImplPackage(serviceImplPackage.getText());
        }


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
        if (canConnect) {
            Messages.showMessageDialog("Connect successful", "OK", Messages.getInformationIcon());
        } else {
            Messages.showMessageDialog("Connect Error!!", "Error", Messages.getErrorIcon());
        }
    }

    @Nullable
    @Override
    protected JComponent createCenterPanel() {
        return contentPane;
    }

    class OpenBtnListener implements ActionListener {
        JButton btn;
        VirtualFile virtualFile;
        JLabel folder;

        public OpenBtnListener(JButton btn, JLabel folder) {
            this.btn = btn;
            this.folder = folder;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            virtualFile = FileChooser.chooseFile(FileChooserDescriptorFactory.createSingleFolderDescriptor(), ProjectManager.getInstance().getDefaultProject(), virtualFile);
            if (virtualFile != null) {
                folder.setText(virtualFile.getPath());
            }
        }
    }
}
