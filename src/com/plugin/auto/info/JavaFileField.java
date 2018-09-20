package com.plugin.auto.info;

import org.apache.commons.lang3.StringUtils;

public class JavaFileField {
    private String type;
    private String field;
    private String comment;
    private JavaAccess access;
    private String defaultVal;
    private String modifier;
    private String anno;


    public JavaFileField type(String type) {
        this.type = type;
        return this;
    }

    public JavaFileField field(String field) {
        this.field = field;
        return this;
    }

    public JavaFileField comment(String comment) {
        this.comment = comment;
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

    public String getCode() {
        String code = "";
        if (StringUtils.isNotBlank(this.comment)) {
            code += "/**\n";
            code += " * " + this.comment + "\n";
            code += " */\n";
        }
        if(StringUtils.isNotBlank(anno)){
            code += "@" + anno + "\n";
        }
        code += this.access.getAccess() + " ";
        if (StringUtils.isNotBlank(this.modifier)) {
            code += this.modifier + " ";
        }
        code += this.type + " " + this.field;

        if (defaultVal != null) {
            code += " = " + this.defaultVal;
        }
        code += ";";
        return code;
    }

    public JavaFileField anno(String anno) {
        this.anno = anno;
        return this;
    }
}
