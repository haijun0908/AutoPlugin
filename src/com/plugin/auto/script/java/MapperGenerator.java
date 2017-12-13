package com.plugin.auto.script.java;

import com.intellij.openapi.util.io.FileUtil;
import com.intellij.xml.util.CheckTagEmptyBodyInspection;
import com.plugin.auto.info.ColumnInfo;
import com.plugin.auto.info.DatabaseConfigInfo;
import com.plugin.auto.info.TableInfo;
import com.plugin.auto.info.mapper.*;
import com.plugin.auto.utils.MapperOutUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.function.*;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class MapperGenerator {
    private DatabaseConfigInfo configInfo;
    private TableInfo tableInfo;

    private MybatisDaoGenerator daoGenerator;


    MapperGenerator(DatabaseConfigInfo configInfo, TableInfo tableInfo) {
        this.configInfo = configInfo;
        this.tableInfo = tableInfo;
        daoGenerator = new MybatisDaoGenerator(configInfo, Arrays.asList(tableInfo));
        daoGenerator.resetCurrentTime(DaoGenerator.CHILD, tableInfo);
    }

    public void generator() {
        MapperXml mapper = new MapperXml();
        mapper.namespace(getDaoName());
        List<MapperBase> elementList = new ArrayList<>();

        elementList.add(new MapperSql().id("table").value(tableInfo.getOriginTableName()));
        elementList.add(new MapperSql().id("fields").value(getAllFieldStr(false, false)));

        MapperResultMap resultMap = new MapperResultMap();
        resultMap.type(getModelName())
                .id("model");
        List<Node> nodeList = new ArrayList<>();
        if (tableInfo.getAutoIncrementColumn() != null) {
            nodeList.add(new ResultMapNode("id").column(tableInfo.getAutoIncrementColumn().getField()).property(tableInfo.getAutoIncrementColumn().getCustomField()));
        }
        nodeList.addAll(tableInfo.getColumnInfoList().stream()
                .filter(columnInfo -> !columnInfo.isAutoIncrement())
                .map(columnInfo -> new ResultMapNode("result").column(columnInfo.getField()).property(columnInfo.getCustomField()))
                .collect(Collectors.toList()));
        resultMap.nodeList(nodeList);
        elementList.add(resultMap);

        //save
        MapperInsert save = new MapperInsert();
        save.useGeneratedKeys(true)
                .parameterType(getModelName());
        if (tableInfo.getAutoIncrementColumn() != null) {
            save.keyProperty(tableInfo.getAutoIncrementColumn().getCustomField())
                    .keyColumn(tableInfo.getAutoIncrementColumn().getField());
        }
        save.id(daoGenerator.saveName())
                .value("INSERT INTO " + getTable() + " (" + getAllFieldStr(true, false) + ") " +
                        "VALUES (" + getAllFieldStr(true, false, "#{", "}", ",") + ")")
        ;
        elementList.add(save);


        //update
        MapperUpdate update = new MapperUpdate();
        update.id(daoGenerator.updateName())
                .value("UPDATE " + getTable() + " set " +
                        tableInfo.getColumnInfoList().stream().filter(columnInfo -> !columnInfo.isPrimaryKey())
                                .map(columnInfo -> columnInfo.getField() + "=#{" + columnInfo.getCustomField() + "}")
                                .collect(Collectors.joining(","))
                        + " where "
                        + tableInfo.getPrimaryColumns().stream().map(columnInfo -> columnInfo.getField() + "=#{" + columnInfo.getCustomField() + "}")
                        .collect(Collectors.joining(","))
                );
        elementList.add(update);

        //delete
        MapperDelete delete = new MapperDelete();
        delete.id(daoGenerator.deleteName())
                .value("DELETE FROM " + getTable() + " WHERE "
                        + tableInfo.getPrimaryColumns().stream().map(columnInfo -> columnInfo.getField() + "=#{" + columnInfo.getCustomField() + "}")
                        .collect(Collectors.joining(","))
                );
        elementList.add(delete);


        //get
        MapperSelect get = new MapperSelect();
        get.resultMap("model")
                .id(daoGenerator.getName())
                .value("SELECT " + getFields() + " FROM " + getTable() + " WHERE "
                        + tableInfo.getPrimaryColumns().stream().map(columnInfo -> columnInfo.getField() + "=#{" + columnInfo.getCustomField() + "}")
                        .collect(Collectors.joining(","))
                );
        elementList.add(get);

        //getAll
        MapperSelect getAll = new MapperSelect();
        getAll.resultMap("model")
                .id(daoGenerator.getAllName())
                .value("SELECT " + getFields() + " FROM " + getTable());
        elementList.add(getAll);

        if (tableInfo.getPrimaryColumns() != null && tableInfo.getPrimaryColumns().size() == 1) {
            //getList
            String value = "SELECT " + getFields() + " FROM " + getTable()
                    + " WHERE " + tableInfo.getPrimaryColumns().get(0).getField() + " IN \n";
            value += "<foreach item=\"item\" collection=\"list\" open=\"(\" separator=\",\" close=\")\"> \n";
            value += "    #{item}\n";
            value += "</foreach>";

            MapperSelect getList = new MapperSelect();
            getList.resultMap("model")
                    .id(daoGenerator.getListName())
                    .value(value);
            elementList.add(getList);

        }

        if (tableInfo.getTableIndexList() != null && tableInfo.getTableIndexList().size() > 0) {

            tableInfo.getTableIndexList().stream().filter(tableIndex -> !(tableIndex.isUnique() && tableIndex.getColumnInfoList().size() == 1))
                    .forEach(tableIndex -> {
                        String value = "SELECT " + getFields() + " FROM " + getTable();
                        value += " WHERE " + tableIndex.getColumnInfoList().stream()
                                .map(columnInfo -> columnInfo.getField() + "=#{" + columnInfo.lowerField() + "}").collect(Collectors.joining(" AND "));
                        elementList.add(new MapperSelect().resultMap("model")
                                .id(tableIndex.getMethod())
                                .value(value)
                        );
                    });
        }


        mapper.elementList(elementList);
        mapper.fileName(daoGenerator.getFileName());
        mapper.filePath(configInfo.getWriteFilePath());
        new MapperOutUtils(mapper).write();
    }

    private String getAllFieldStr(boolean skipAutoIncrementKey, boolean skipPrimaryKey, String before, String after, String delimiter) {
        return tableInfo.getColumnInfoList().stream()
                .filter(columnInfo -> !(skipAutoIncrementKey && columnInfo.isAutoIncrement()) && !(skipPrimaryKey && columnInfo.isPrimaryKey()))
                .map(columnInfo -> before + columnInfo.getCustomField() + after).collect(Collectors.joining(delimiter));
    }

    private String getAllFieldStr(boolean skipAutoIncrementKey, boolean skipPrimaryKey) {
        return getAllFieldStr(skipAutoIncrementKey, skipPrimaryKey, "", "", ",");
    }

    private String getModelName() {
        ModelGenerator modelGenerator = new ModelGenerator(configInfo, Arrays.asList(tableInfo));
        modelGenerator.resetCurrentTime(1, tableInfo);
        return modelGenerator.getFullName();
    }

    private String getTable() {
        return " <include refid=\"table\"/> ";
//        return tableInfo.getOriginTableName();
    }

    private String getFields() {
//        return "*";
        return "<include refid=\"fields\"/>";
    }

    private String getDaoName() {
        return daoGenerator.getFullName();
    }


}
