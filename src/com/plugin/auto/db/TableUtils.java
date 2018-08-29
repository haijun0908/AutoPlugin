package com.plugin.auto.db;

import com.plugin.auto.info.ColumnInfo;
import com.plugin.auto.info.DatabaseConfigInfo;
import com.plugin.auto.info.TableIndex;
import com.plugin.auto.info.TableInfo;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class TableUtils {

    private DBHelper dbHelper;
    private List<TableInfo> tableInfoList;
    private String dbName;

    public TableUtils(DatabaseConfigInfo configInfo) {
        dbHelper = new DBHelper(configInfo);
        this.dbName = configInfo.getDb();
    }

    public void initTables() {
        String sql = "show table STATUS";
        tableInfoList = new ArrayList<>();
        try {
            PreparedStatement ps = dbHelper.getPreparedStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                String tableName = rs.getString("Name");
                TableInfo tableInfo = new TableInfo();
                tableInfo.setTableName(tableName);
                tableInfo.setOriginTableName(tableName);
                tableInfo.setComment(rs.getString("Comment"));
                tableInfoList.add(tableInfo);
            }
            descTable();

            dbHelper.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean canConnect() {
        String sql = "show tables;";
        try {
            PreparedStatement ps = dbHelper.getPreparedStatement(sql);
            if (ps != null)
                return true;
        } catch (Exception e) {
//            e.printStackTrace();
        } finally {
            try {
                dbHelper.close();
            } catch (Exception e) {
//                e.printStackTrace();
            }
        }
        return false;
    }

    private void descTable() {
        try {
            if (tableInfoList != null && tableInfoList.size() > 0) {


                String sql = "select TABLE_NAME,COLUMN_COMMENT,COLUMN_NAME,COLUMN_KEY,DATA_TYPE,COLUMN_DEFAULT,EXTRA from information_schema.columns where table_schema = '" + this.dbName + "'";
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
                    info.setAutoIncrement("auto_increment".equals(rs.getString("EXTRA")));
                    info.setCustomField(info.getField());
                    columnInfoList.add(info);

                    columnMap.put(tableName, columnInfoList);
                }

                for (TableInfo tableInfo : tableInfoList) {
                    tableInfo.setColumnInfoList(columnMap.get(tableInfo.getTableName()));

                    fillTableIndex(tableInfo);
                }

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<TableInfo> getTables() {
        return tableInfoList;
    }

    public void fillTableIndex(TableInfo tableInfo) {
        String sql = "show INDEX from " + tableInfo.getTableName();
        try {
            Map<String, ColumnInfo> columnInfoMap = new HashMap<>();
            for (ColumnInfo columnInfo : tableInfo.getColumnInfoList()) {
                columnInfoMap.put(columnInfo.getField(), columnInfo);
            }
            PreparedStatement ps = dbHelper.getPreparedStatement(sql);
            ResultSet rs = ps.executeQuery();
            Map<String, TableIndex> map = new LinkedHashMap<>();
            while (rs.next()) {
                String keyName = rs.getString("Key_name");
                TableIndex tableIndex = null;
                if (map.containsKey(keyName)) {
                    tableIndex = map.get(keyName);
                } else {
                    tableIndex = new TableIndex();
                }
                boolean isUnique = rs.getInt("Non_unique") == 0;
                ColumnInfo columnInfo = columnInfoMap.get(rs.getString("Column_name"));
                tableIndex.setUnique(isUnique);
                List<ColumnInfo> columnInfoList = tableIndex.getColumnInfoList();
                if (columnInfoList == null) {
                    columnInfoList = new ArrayList<>();
                }
                columnInfoList.add(columnInfo);
                tableIndex.setColumnInfoList(columnInfoList);
                map.put(keyName, tableIndex);
            }
            Iterator keys = map.keySet().iterator();
            List<TableIndex> tableIndexList = new ArrayList<>();
            while (keys.hasNext()) {
                tableIndexList.add(map.get((String) keys.next()));
            }
            tableInfo.setTableIndexList(tableIndexList);


        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
