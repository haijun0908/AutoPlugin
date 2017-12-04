package com.plugin.auto.db;

import com.plugin.auto.info.DatabaseConfigInfo;

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
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection(getUrl(),configInfo.getUser() , configInfo.getPwd());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String getUrl() {
        return "jdbc:mysql://" + configInfo.getHost() + ":" + configInfo.getPort() + "/" + configInfo.getDb();
    }

    public PreparedStatement getPreparedStatement(String sql) throws SQLException {
        connect();
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
    }

}
