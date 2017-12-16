package com.plugin.auto.common;

import com.plugin.auto.info.xml.MybatisConfigXml;
import com.plugin.auto.script.BaseGenerator;
import com.plugin.auto.script.java.*;

public enum RightMenuItem {

    ALL("ALL", "", null),
    NULL("-----------", "", null),
    MODEL("Model", "", ModelGenerator.class),
    DAO("Dao", "", MybatisDaoGenerator.class , MybatisConfigGenerator.class),
    SERVICE("Service", "", ServiceGenerator.class),
    DTO("Dto", "", DtoGenerator.class);

    private String item;
    private String icon;
    private Class<? extends BaseGenerator>[] generator;

    RightMenuItem(String item, String icon, Class<? extends BaseGenerator>... generator) {
        this.item = item;
        this.icon = icon;
        this.generator = generator;
    }

    public Class<? extends BaseGenerator>[] getGenerator() {
        return generator;
    }

    public void setGenerator(Class<? extends BaseGenerator>[] generator) {
        this.generator = generator;
    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }
}
