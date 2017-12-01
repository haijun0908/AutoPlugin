package com.bookingtee.plugin.script.groovy;

import com.bookingtee.plugin.info.DatabaseConfigInfo;
import com.bookingtee.plugin.info.TableInfo;
import com.bookingtee.plugin.script.BaseGenerator;
import groovy.lang.*;

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
            String path = "/Users/Jun/Documents/idea_workspace/AutoPlugin/OutFile/src/com/bookingtee/com/plugin/out/";

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
