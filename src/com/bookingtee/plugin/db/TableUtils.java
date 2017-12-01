package com.bookingtee.plugin.db;

import com.bookingtee.plugin.info.ColumnInfo;
import com.bookingtee.plugin.info.DatabaseConfigInfo;
import com.bookingtee.plugin.info.TableInfo;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TableUtils {

    private DBHelper dbHelper;
    private List<TableInfo> tableInfoList;
    private String dbName;

    public TableUtils(DatabaseConfigInfo configInfo) {
        dbHelper = new DBHelper(configInfo);
        this.dbName = configInfo.getDb();
        initTables();
    }

    private void initTables() {
        String sql = "show tables;";
        tableInfoList = new ArrayList<>();
        try {
            PreparedStatement ps = dbHelper.getPreparedStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                String tableName = rs.getString(1);
                TableInfo tableInfo = new TableInfo();
                tableInfo.setTableName(tableName);
                tableInfoList.add(tableInfo);
            }
            descTable();

            dbHelper.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void descTable() {
        try {
            if (tableInfoList != null && tableInfoList.size() > 0) {
                String sql = "select TABLE_NAME,COLUMN_NAME,COLUMN_DEFAULT,DATA_TYPE,COLUMN_KEY,COLUMN_COMMENT from information_schema.columns where table_schema = '" + this.dbName + "'";
                PreparedStatement ps = dbHelper.getPreparedStatement(sql);
                ResultSet rs = ps.executeQuery();
                Map<String, List<ColumnInfo>> columnMap = new HashMap<>();
                while (rs.next()) {
                    String tableName = rs.getString("TABLE_NAME");
                    List<ColumnInfo> columnInfoList = null;
                    if (columnMap.containsKey(tableName)) {
                        columnInfoList = columnMap.get(tableName);
                    } else {
                        columnInfoList = new ArrayList<>();
                    }
                    ColumnInfo info = new ColumnInfo();
                    info.setComment(rs.getString("COLUMN_COMMENT"));
                    info.setField(rs.getString("COLUMN_NAME"));
                    info.setPrimaryKey("PRI".equals(rs.getString("COLUMN_KEY")));
                    info.setType(rs.getString("DATA_TYPE"));
                    info.setDefaultVal(rs.getObject("COLUMN_DEFAULT"));
                    columnInfoList.add(info);

                    columnMap.put(tableName , columnInfoList);
                }

                for (TableInfo tableInfo : tableInfoList) {
                    tableInfo.setColumnInfoList(columnMap.get(tableInfo.getTableName()));
                }

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<TableInfo> getTables() {
        return tableInfoList;
    }



}
