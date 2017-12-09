package com.plugin.auto.utils;

import com.google.gson.*;
import com.intellij.openapi.util.io.FileUtil;
import com.plugin.auto.info.mapper.MapperBase;
import com.plugin.auto.info.mapper.MapperXml;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map;

public class MapperOutUtils {
    private MapperXml mapperXml;
    private StringBuilder sb = new StringBuilder();

    public MapperOutUtils(MapperXml mapperXml) {
        this.mapperXml = mapperXml;
    }

    private static Gson gson;

    static {
        gson = new GsonBuilder().addSerializationExclusionStrategy(new ExclusionStrategy() {
            @Override
            public boolean shouldSkipField(FieldAttributes fieldAttributes) {
                return fieldAttributes.getName().equals("id") || fieldAttributes.getName().equals("value")
                        || fieldAttributes.getName().equals("nodeList");
            }

            @Override
            public boolean shouldSkipClass(Class<?> aClass) {
                return false;
            }
        }).create();
    }

    public void write() {
        try {
            append("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\n" +
                    "<!DOCTYPE mapper\n" +
                    "        PUBLIC \"-//mybatis.org//DTD Mapper 3.0//EN\"\n" +
                    "        \"http://mybatis.org/dtd/mybatis-3-mapper.dtd\">");
            appendLine();
            append("<mapper namespace=\"" + mapperXml.getNamespace() + "\">");

            for (MapperBase mapperBase : mapperXml.getElementList()) {
                JsonObject json = gson.toJsonTree(mapperBase).getAsJsonObject();
                appendLine();
                String element = "<" + mapperBase.element() + " id=\"" + mapperBase.getId() + "\"";
                Iterator keys = json.entrySet().iterator();
                while (keys.hasNext()) {
                    Map.Entry<String, JsonElement> entry = (Map.Entry<String, JsonElement>) keys.next();
                    element += " " + entry.getKey() + "=\"" + entry.getValue().getAsString() + "\"";
                }


                append(element + ">", 1);

                //node
                if (mapperBase.getNodeList() != null && mapperBase.getNodeList().size() > 0) {
                    mapperBase.getNodeList().forEach(node -> {
                        String s = "<" + node.getNode();
                        for (int i = 0; i < node.getKeys().size(); i++) {
                            s += " " + node.getKeys().get(i) + "=" + "\"" + node.getValues().get(i) + "\"";
                        }
                        s += "/>";
                        append(s , 2);
                    });
                }

                append(mapperBase.getValue(), 2);
                append("</" + mapperBase.element() + ">", 1);
            }

            appendLine();


            append("</mapper>");

            FileUtil.writeToFile(new File(mapperXml.getFilePath() + File.separator + mapperXml.getFileName() + ".xml"), sb.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void append(String content) {
        append(content, 0);
    }

    private void appendLine() {
        append("");
    }


    private void append(String content, int tabCount) {
        if(content == null)
            return;
        String[] lines = content.split("\n");
        String tab = "";
        for (int i = 0; i < tabCount; i++) {
            tab += "    ";
        }
        for (String line : lines) {
            sb.append(tab).append(line).append("\n");
        }
    }
}
