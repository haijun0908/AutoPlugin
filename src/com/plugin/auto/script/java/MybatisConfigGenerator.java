package com.plugin.auto.script.java;

import com.plugin.auto.info.DatabaseConfigInfo;
import com.plugin.auto.info.TableInfo;
import com.plugin.auto.info.xml.MybatisConfigXml;
import com.plugin.auto.info.xml.mapper.Node;
import com.plugin.auto.script.BaseGenerator;
import com.plugin.auto.utils.XmlOut;

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

        new XmlOut(configXml).writeFile();
    }

    private ModelGenerator getModelGenerator(TableInfo tableInfo) {
        ModelGenerator modelGenerator = new ModelGenerator(configInfo, tableInfoList);
        modelGenerator.resetCurrentTime(1, tableInfo);
        return modelGenerator;
    }

}
