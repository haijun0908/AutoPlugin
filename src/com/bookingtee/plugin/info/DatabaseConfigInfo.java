package com.bookingtee.plugin.info;

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
