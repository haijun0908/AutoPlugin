package com.plugin.auto.utils;

import com.plugin.auto.info.ColumnInfo;
import org.apache.commons.lang3.StringUtils;
import org.ini4j.Reg;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class PluginUtils {



    private static List<Reg> regList = new ArrayList<>();

    static {
        regList.add(new Reg("int", "int", "", "Integer"));
        regList.add(new Reg("float|double|decimal|real", "double", "", "Double"));
        regList.add(new Reg("datetime|timestamp", "Date", "java.util.Date", "Date"));
        regList.add(new Reg("date", "java.sql.Date", "", "java.sql.Date"));
        regList.add(new Reg("time", "java.sql.Time", "", "java.sql.Time"));
        regList.add(new Reg("", "String", "", "String"));
    }

    public static String javaName(String str, boolean capitalize) {
        String result = "";
        for (String s : str.split("_")) {
            result += s.substring(0, 1).toUpperCase() + s.substring(1);
        }
        return capitalize ? result : (result.substring(0, 1).toLowerCase() + result.substring(1));
    }

    public static Reg reg(ColumnInfo c) {
        for (Reg reg : regList) {
            Pattern p = Pattern.compile(reg.reg, Pattern.DOTALL);
            if (p.matcher(c.getType()).find()) {
                return reg;
            }
        }
        return null;
    }

    public static String lowerFirst(String s){
        return s.substring(0,1).toLowerCase() + s.substring(1);
    }

    public static List<String> getImportList(List<ColumnInfo> columnInfos){
        List<String> list = new ArrayList<>();
        for (ColumnInfo columnInfo : columnInfos) {
            PluginUtils.Reg reg = PluginUtils.reg(columnInfo);
            if (reg != null && StringUtils.isNotBlank(reg.importClass) && list.indexOf(reg.importClass) == -1) {
                list.add(reg.importClass);
            }
        }
        return list;
    }

    public static class Reg {
        public String reg;
        public String type;
        public String importClass;
        public String packageName;

        public Reg(String reg, String type, String importClass, String packageName) {
            this.reg = reg;
            this.type = type;
            this.importClass = importClass;
            this.packageName = packageName;
        }
    }

}
