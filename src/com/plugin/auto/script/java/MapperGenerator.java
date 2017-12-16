package com.plugin.auto.script.java;

import com.plugin.auto.common.WriteFileListener;
import com.plugin.auto.info.ColumnInfo;
import com.plugin.auto.info.DatabaseConfigInfo;
import com.plugin.auto.info.TableInfo;
import com.plugin.auto.info.xml.mapper.*;
import com.plugin.auto.utils.MapperOutUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class MapperGenerator {
    private DatabaseConfigInfo configInfo;
    private TableInfo tableInfo;

    private ModelGenerator modelGenerator;

    public static final int PARENT = 1;
    public static final int CHILD = 2;

    private boolean isBase = true;

    MapperGenerator(DatabaseConfigInfo configInfo, TableInfo tableInfo) {
        this.configInfo = configInfo;
        this.tableInfo = tableInfo;

    }

    public MapperGenerator generator(int currentTime) {
        isBase = currentTime == PARENT;


        modelGenerator = new ModelGenerator(configInfo, Arrays.asList(tableInfo));
        modelGenerator.resetCurrentTime(1, tableInfo);

        MapperXml mapper = new MapperXml();
        mapper.namespace(getDaoName());
        List<MapperBase> elementList = new ArrayList<>();

        if (isBase) {
            elementList.add(new MapperSql().id("table").value(tableInfo.getOriginTableName()));
            elementList.add(new MapperSql().id("fields").value(getAllFieldStr(false, false)));
        } else {
            elementList.add(new MapperSql().id("table").value("<include refid=\"" + getDaoGenerator(true).getFullName() + ".table\"/>"));
            elementList.add(new MapperSql().id("fields").value("<include refid=\"" + getDaoGenerator(true).getFullName() + ".fields\"/>"));

        }

        MapperResultMap resultMap = new MapperResultMap();
        resultMap.type(modelGenerator.getFileName())
                .id("model");
        if (isBase) {
            List<Node> nodeList = new ArrayList<>();
            if (tableInfo.getAutoIncrementColumn() != null) {
                nodeList.add(new ResultMapNode("id").column(tableInfo.getAutoIncrementColumn().getField()).property(tableInfo.getAutoIncrementColumn().getCustomField()));
            }
            nodeList.addAll(tableInfo.getColumnInfoList().stream()
                    .filter(columnInfo -> !columnInfo.isAutoIncrement())
                    .map(columnInfo -> new ResultMapNode("result").column(columnInfo.getField()).property(columnInfo.getCustomField()))
                    .collect(Collectors.toList()));
            resultMap.nodeList(nodeList);
        } else {
            resultMap.type(modelGenerator.getFileName())
                    .extendsStr(getDaoGenerator(true).getFullName() + ".model");
        }
        elementList.add(resultMap);

        if (isBase) {
            //save
            MapperInsert save = new MapperInsert();
            save.useGeneratedKeys(true)
                    .parameterType(modelGenerator.getFileName());
            if (tableInfo.getAutoIncrementColumn() != null) {
                save.keyProperty(tableInfo.getAutoIncrementColumn().getCustomField())
                        .keyColumn(tableInfo.getAutoIncrementColumn().getField());
            }
            save.id(getDaoGenerator(isBase).saveName())
                    .value("INSERT INTO " + getTable() + " (" + getAllFieldStr(true, false) + ") " +
                            "VALUES (" + getAllFieldStr(true, false, "#{", "}", ",") + ")")
            ;
            elementList.add(save);


            //update
            MapperUpdate update = new MapperUpdate();
            update.parameterType(modelGenerator.getFileName())
                    .id(getDaoGenerator(isBase).updateName())
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
            delete.id(getDaoGenerator(isBase).deleteName())
                    .value("DELETE FROM " + getTable() + " WHERE "
                            + tableInfo.getPrimaryColumns().stream().map(columnInfo -> columnInfo.getField() + "=#{" + columnInfo.getCustomField() + "}")
                            .collect(Collectors.joining(","))
                    );
            elementList.add(delete);


            //get
            MapperSelect get = new MapperSelect();
            get.resultMap("model")
                    .id(getDaoGenerator(isBase).getName())
                    .value("SELECT " + getFields() + " FROM " + getTable() + " WHERE "
                            + tableInfo.getPrimaryColumns().stream().map(columnInfo -> columnInfo.getField() + "=#{" + columnInfo.getCustomField() + "}")
                            .collect(Collectors.joining(","))
                    );
            elementList.add(get);

            //getAll
            MapperSelect getAll = new MapperSelect();
            getAll.resultMap("model")
                    .id(getDaoGenerator(isBase).getAllName())
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
                        .id(getDaoGenerator(isBase).getListName())
                        .value(value);
                elementList.add(getList);

            }

            if (tableInfo.getTableIndexList() != null && tableInfo.getTableIndexList().size() > 0) {

                tableInfo.getTableIndexList().stream().filter(tableIndex -> !(tableIndex.isUnique() && tableIndex.getColumnInfoList().size() == 1))
                        .forEach(tableIndex -> {
                            String value = "SELECT " + getFields() + " FROM " + getTable();
                            value += " WHERE " + tableIndex.getColumnInfoList().stream()
                                    .map(ColumnInfo::getSql)
                                    .collect(Collectors.joining(" AND "));
                            elementList.add(new MapperSelect().resultMap("model")
                                    .id(tableIndex.getMethod())
                                    .value(value)
                            );
                        });
            }
        }
        mapper.elementList(elementList);


        mapper.fileName(getDaoGenerator(isBase).getFileName());
        mapper.filePath(configInfo.getResourcePath() + File.separator + "mapper" + (isBase ? (File.separator + "base") : ""));
        mapper.canOverwrite(isBase ? true : false);
        new MapperOutUtils(mapper).writeFile();

        return this;
    }

    private String getAllFieldStr(boolean skipAutoIncrementKey, boolean skipPrimaryKey, String before, String after, String delimiter) {
        return tableInfo.getColumnInfoList().stream()
                .filter(columnInfo -> !(skipAutoIncrementKey && columnInfo.isAutoIncrement()) && !(skipPrimaryKey && columnInfo.isPrimaryKey()))
                .map(columnInfo -> before + columnInfo.getCustomField() + after).collect(Collectors.joining(delimiter));
    }

    private String getAllFieldStr(boolean skipAutoIncrementKey, boolean skipPrimaryKey) {
        return getAllFieldStr(skipAutoIncrementKey, skipPrimaryKey, "", "", ",");
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
        return getDaoGenerator(isBase).getFullName();
    }

    private MybatisDaoGenerator getDaoGenerator(boolean isParent) {
        MybatisDaoGenerator daoGenerator = new MybatisDaoGenerator(configInfo, Collections.singletonList(tableInfo));
        daoGenerator.resetCurrentTime(isParent ? MybatisDaoGenerator.PARENT : MybatisDaoGenerator.CHILD, tableInfo);
        return daoGenerator;
    }

}
