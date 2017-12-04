package com.plugin.auto.script.groovy;

import com.plugin.auto.info.DatabaseConfigInfo;
import com.plugin.auto.info.TableInfo;
import com.plugin.auto.script.BaseGenerator;
import groovy.lang.Binding;
import groovy.lang.GroovyShell;

import java.io.File;
import java.util.List;

public abstract class GroovyGenerator extends BaseGenerator {

    public GroovyGenerator(DatabaseConfigInfo configInfo, List<TableInfo> tableInfoList) {
        super(configInfo, tableInfoList);
    }

    abstract String getGroovyFile();

    @Override
    protected void generator() {
        try {

            File file = new File(GroovyGenerator.class.getResource("/script/" + getGroovyFile()).getFile());
            String path = "/Users/Jun/Documents/idea_workspace/AutoPlugin/OutFile/src/com/plugin/auto/out/";

            Binding binding = new Binding();
            GroovyShell shell = new GroovyShell(binding);
            binding.setVariable("table" , currentTable);
            binding.setVariable("dir" , path);
            binding.setVariable("configInfo" , configInfo);
            shell.evaluate(file);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
