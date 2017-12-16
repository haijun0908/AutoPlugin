package com.plugin.auto.utils;

import com.google.gson.*;
import com.plugin.auto.info.xml.mapper.MapperBase;
import com.plugin.auto.info.xml.mapper.MapperXml;

import java.io.File;
import java.util.Iterator;
import java.util.Map;

public class MapperOutUtils extends FileOut<MapperXml> {
    private MapperXml mapperXml;

    @Override
    boolean canOverwrite() {
        return mapperXml.isCanOverwrite();
    }

    @Override
    File getWriteFile() {
        return new File(mapperXml.getFilePath() + File.separator + mapperXml.getFileName() + ".xml");
    }


    public MapperOutUtils(MapperXml mapperXml) {
        super(mapperXml);
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

    @Override
    StringBuilder getFileContent() {
        append("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\n" +
                "<!DOCTYPE "+mapperXml.getRoot()+"\n" +
                "        PUBLIC "+mapperXml.getDtd()+">");
        appendLine();
        append("<"+mapperXml.getRoot()+" namespace=\"" + mapperXml.getNamespace() + "\">");

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
                    append(s, 2);
                });
            }

            append(mapperBase.getValue(), 2);
            append("</" + mapperBase.element() + ">", 1);
        }

        appendLine();
        append("</"+mapperXml.getRoot()+">");

        return sb;
    }

}
