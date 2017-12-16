package com.plugin.auto.info.xml;

import com.plugin.auto.info.FileInfo;
import com.plugin.auto.info.xml.mapper.Node;

import java.util.List;

public class XmlInfo extends FileInfo {
    private String root;
    private String dtd;
    private List<Node> nodeList;

    public String getRoot() {
        return root;
    }

    public void setRoot(String root) {
        this.root = root;
    }

    public String getDtd() {
        return dtd;
    }

    public void setDtd(String dtd) {
        this.dtd = dtd;
    }

    public List<Node> getNodeList() {
        return nodeList;
    }

    public XmlInfo nodeList(List<Node> nodeList) {
        this.nodeList = nodeList;
        return this;
    }
}
