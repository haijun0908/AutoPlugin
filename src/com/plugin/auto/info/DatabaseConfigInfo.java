package com.plugin.auto.info;

import com.google.gson.Gson;
import com.intellij.ide.util.PropertiesComponent;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 数据库信息对象
 */
public class DatabaseConfigInfo implements Serializable {

    private static final long serialVersionUID = -5355876389679434197L;

    private static final String CACHE_KEY = "cache_db_info";

    private String name;
    private String host;
    private String port;
    private String user;
    private String pwd;
    private String db;

    private String packagePath;
    private String writeFilePath;
    private String resourcePath;

    private String dtoPackage;
    private String dtoFilePath;
    private String modelPackage;
    private String modelFilePath;
    private String servicePackage;
    private String serviceFilePath;
    private String daoPackage;
    private String daoFilePath;

    public String getDtoPackage() {
        return dtoPackage;
    }

    public void setDtoPackage(String dtoPackage) {
        this.dtoPackage = dtoPackage;
    }

    public String getDtoFilePath() {
        return dtoFilePath;
    }

    public void setDtoFilePath(String dtoFilePath) {
        this.dtoFilePath = dtoFilePath;
    }

    public String getModelPackage() {
        return modelPackage;
    }

    public void setModelPackage(String modelPackage) {
        this.modelPackage = modelPackage;
    }

    public String getModelFilePath() {
        return modelFilePath;
    }

    public void setModelFilePath(String modelFilePath) {
        this.modelFilePath = modelFilePath;
    }

    public String getServicePackage() {
        return servicePackage;
    }

    public void setServicePackage(String servicePackage) {
        this.servicePackage = servicePackage;
    }

    public String getServiceFilePath() {
        return serviceFilePath;
    }

    public void setServiceFilePath(String serviceFilePath) {
        this.serviceFilePath = serviceFilePath;
    }

    public String getDaoPackage() {
        return daoPackage;
    }

    public void setDaoPackage(String daoPackage) {
        this.daoPackage = daoPackage;
    }

    public String getDaoFilePath() {
        return daoFilePath;
    }

    public void setDaoFilePath(String daoFilePath) {
        this.daoFilePath = daoFilePath;
    }

    public String getResourcePath() {
        return resourcePath;
    }

    public void setResourcePath(String resourcePath) {
        this.resourcePath = resourcePath;
    }

    public String getWriteFilePath() {
        return writeFilePath;
    }

    public void setWriteFilePath(String writeFilePath) {
        this.writeFilePath = writeFilePath;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public String getDb() {
        return db;
    }

    public void setDb(String db) {
        this.db = db;
    }


    public String getPackagePath() {
        return packagePath;
    }

    public void setPackagePath(String packagePath) {
        this.packagePath = packagePath;
    }


    public String getDisplayLink() {
        return "mysql://" + host + ":" + port + "/" + db;
    }


    public static List<DatabaseConfigInfo> getExistList() {
        List<DatabaseConfigInfo> list = new ArrayList<>();
        String s = PropertiesComponent.getInstance().getValue(CACHE_KEY);
        if (StringUtils.isNotBlank(s)) {
            DatabaseConfigInfo[] databaseConfigInfos = new Gson().fromJson(s, DatabaseConfigInfo[].class);
            if (databaseConfigInfos != null || databaseConfigInfos.length > 0) {
                for (DatabaseConfigInfo info : databaseConfigInfos) {
                    list.add(info);
                }
            }
        }
        return list;
    }

    public static void saveList(List<DatabaseConfigInfo> configInfoList) {
        if (configInfoList != null && configInfoList.size() > 0) {
            PropertiesComponent.getInstance().setValue(CACHE_KEY, new Gson().toJson(configInfoList));
        } else {
            PropertiesComponent.getInstance().unsetValue(CACHE_KEY);
        }
    }
}
