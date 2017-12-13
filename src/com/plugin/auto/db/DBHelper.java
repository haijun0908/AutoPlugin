package com.plugin.auto.db;

import com.plugin.auto.info.DatabaseConfigInfo;
import com.plugin.auto.utils.ProgressText;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DBHelper {
    private DatabaseConfigInfo configInfo;

    public Connection conn;

    public DBHelper(DatabaseConfigInfo configInfo) {
        this.configInfo = configInfo;
    }

    private void connect(){
        try {
            if(conn == null || conn.isClosed()){
                ProgressText.setProgress("prepare connect " + configInfo.getHost());
                Class.forName("com.mysql.jdbc.Driver");
                conn = DriverManager.getConnection(getUrl(),configInfo.getUser() , configInfo.getPwd());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String getUrl() {
        return "jdbc:mysql://" + configInfo.getHost() + ":" + configInfo.getPort() + "/" + configInfo.getDb();
    }

    public PreparedStatement getPreparedStatement(String sql) throws SQLException {
        connect();
        ProgressText.setProgress(sql);
        return conn.prepareStatement(sql);
    }

    public void close(){
        if(conn != null){
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        conn = null;
        ProgressText.setProgress("");
    }

}
