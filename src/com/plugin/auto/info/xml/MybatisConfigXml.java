package com.plugin.auto.info.xml;

public class MybatisConfigXml extends XmlInfo {

    @Override
    public String getRoot() {
        return "configuration";
    }

    @Override
    public String getDtd() {
        return "\"-//mybatis.org//DTD Config 3.0//EN\" \"http://mybatis.org/dtd/mybatis-3-config.dtd\"";
    }

}
