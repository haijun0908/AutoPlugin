package com.bookingtee.plugin.info;

import org.apache.commons.lang3.StringUtils;

public class JavaFileMethod {
    private String returnType;
    private String method;
    private String params;
    private String body;
    private JavaAccess access = JavaAccess.PUBLIC;
    private String anno;

    public JavaFileMethod returnType(String returnType) {
        this.returnType = returnType;
        return this;
    }

    public JavaFileMethod method(String method) {
        this.method = method;
        return this;
    }

    public JavaFileMethod params(String params) {
        this.params = params;
        return this;
    }

    public JavaFileMethod body(String body) {
        this.body = body;
        return this;
    }

    public JavaFileMethod access(JavaAccess access) {
        this.access = access;
        return this;
    }


    public JavaFileMethod anno(String anno) {
        this.anno = anno;
        return this;
    }

    public String getCode() {
        String code = "";

        if (StringUtils.isNotBlank(this.anno)) {
            code += "/**\n";
            code += " * " + this.anno + "\n";
            code += " */";
        }
        code += this.access.getAccess() + " " + this.returnType + " " + this.method;
        if (StringUtils.isNotBlank(params)) {
            code += "(" + this.params + ")";
        } else {
            code += "()";
        }
        if (StringUtils.isNotBlank(this.body)) {
            code += " {\n";
            code += "    " + this.body + "\n";
            code += "}";
        } else {
            code += ";";
        }

        return code;
    }

}
