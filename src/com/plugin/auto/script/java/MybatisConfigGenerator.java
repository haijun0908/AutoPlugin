package com.plugin.auto.script.java;

import com.plugin.auto.info.DatabaseConfigInfo;
import com.plugin.auto.info.TableInfo;
import com.plugin.auto.info.xml.MybatisConfigXml;
import com.plugin.auto.info.xml.mapper.Node;
import com.plugin.auto.script.BaseGenerator;
import com.plugin.auto.utils.FileUtils;
import com.plugin.auto.utils.XmlOut;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class MybatisConfigGenerator extends BaseGenerator {

    public MybatisConfigGenerator(DatabaseConfigInfo configInfo, List<TableInfo> tableInfoList) {
        super(configInfo, tableInfoList);
    }


    public void generator() {
        MybatisConfigXml configXml = new MybatisConfigXml();
        configXml.setFilePath(configInfo.getResourcePath());

        XmlOut xmlOut = new XmlOut(configXml);
        //首先判断是否有这文件
        File configFile = xmlOut.getWriteFile();
        String content = FileUtils.readToString(configFile);
        if (configFile.exists() && StringUtils.isNotBlank(content) && content.contains("</typeAliases>")) {
            //有 更新该文件
            //找到节点
            int index = content.indexOf("</typeAliases>");
            String addContent = tableInfoList.stream().map(tableInfo -> new Node("typeAlias")
                    .put("type", getModelGenerator(tableInfo).getFullName())
                    .put("alias", getModelGenerator(tableInfo).getFileName()).toString())
                    .findFirst().get();
            if (!content.contains(addContent)) {
                content = content.substring(0, index) + "    " +
                        addContent
                        + "\n    " + content.substring(index, content.length());
                FileUtils.writeString(configFile, content);
            }
        } else {
            //没有  新建
            List<Node> nodeList = new ArrayList<>();
            nodeList.add(new Node("settings").subNodeList(Arrays.asList(new Node("setting").put("name", "logImpl").put("value", "STDOUT_LOGGING"))));

            Node typeAliases = new Node("typeAliases");
            typeAliases.subNodeList(tableInfoList.stream()
                    .map(tableInfo -> new Node("typeAlias")
                            .put("type", getModelGenerator(tableInfo).getFullName())
                            .put("alias", getModelGenerator(tableInfo).getFileName()))
                    .collect(Collectors.toList()));
            nodeList.add(typeAliases);

            configXml.nodeList(nodeList);

            xmlOut.writeFile();
        }
    }

    private ModelGenerator getModelGenerator(TableInfo tableInfo) {
        ModelGenerator modelGenerator = new ModelGenerator(configInfo, tableInfoList);
        modelGenerator.resetCurrentTime(1, tableInfo);
        return modelGenerator;
    }

    public static void main(String[] args) {

    }

}
