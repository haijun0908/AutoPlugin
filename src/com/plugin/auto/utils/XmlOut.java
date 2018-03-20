package com.plugin.auto.utils;

import com.plugin.auto.info.xml.XmlInfo;
import com.plugin.auto.info.xml.mapper.Node;

import java.io.File;

public class XmlOut extends FileOut<XmlInfo> {
    private XmlInfo xmlInfo;

    public XmlOut(XmlInfo xmlInfo) {
        super(xmlInfo);
        this.xmlInfo = xmlInfo;
    }

    @Override
    boolean canOverwrite() {
        return false;
    }

    @Override
    public File getWriteFile() {
        return new File(xmlInfo.getFilePath(), "mybatis-config.xml");
    }

    @Override
    StringBuilder getFileContent() {
        append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        append("<!DOCTYPE " + xmlInfo.getRoot() + " PUBLIC " + xmlInfo.getDtd() + ">");
        appendLine();
        append("<" + xmlInfo.getRoot() + ">");

        xmlInfo.getNodeList().forEach(node -> {
            node2XML(node, 1);
            appendLine();
        });

        append("</" + xmlInfo.getRoot() + ">");
        return sb;
    }

    public void node2XML(Node node, int tabCount) {

        append("<" + node.getNode() + node.getAttributeValues() + ((node.getSubNodeList() != null && node.getSubNodeList().size() > 0) ? "" : "/") + ">", tabCount);
        if (node.getSubNodeList() != null && node.getSubNodeList().size() > 0) {
            for (int i = 0; i < node.getSubNodeList().size(); i++) {
                node2XML(node.getSubNodeList().get(i), tabCount + 1);
            }
            append("</" + node.getNode() + ">", tabCount);
        }
    }
}
