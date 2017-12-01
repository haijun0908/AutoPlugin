package com.bookingtee.plugin.info;

import org.apache.commons.lang3.StringUtils;

public class JavaFileField {
    private String type;
    private String field;
    private String anno;
    private JavaAccess access;
    private String defaultVal;
    private String modifier;

    public JavaFileField type(String type) {
        this.type = type;
        return this;
    }

    public JavaFileField field(String field) {
        this.field = field;
        return this;
    }

    public JavaFileField anno(String anno) {
        this.anno = anno;
        return this;
    }


    public JavaFileField access(JavaAccess access) {
        this.access = access;
        return this;
    }


    public JavaFileField defaultVal(String defaultVal) {
        this.defaultVal = defaultVal;
        return this;
    }

    public JavaFileField modifier(String modifier) {
        this.modifier = modifier;
        return this;
    }

    public String getCode(){
        String code = "";
        if(StringUtils.isNotBlank(this.anno)){
            code += "/**\n";
            code += " * " + this.anno + "\n";
            code += " */\n";
        }
        code += this.access.getAccess() + " " ;
        if(StringUtils.isNotBlank(this.modifier)){
            code += this.modifier + " ";
        }
        code += this.type + " " + this.field;

        if(StringUtils.isNotBlank(this.defaultVal)){
            code += " = " + this.defaultVal;
        }
        code += ";";
        return code;
    }

}
