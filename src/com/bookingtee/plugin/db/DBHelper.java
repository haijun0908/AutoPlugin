package com.bookingtee.plugin.db;

import com.bookingtee.plugin.info.DatabaseConfigInfo;

import java.sql.*;

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
