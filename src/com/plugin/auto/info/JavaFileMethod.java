package com.plugin.auto.info;

import org.apache.commons.lang3.StringUtils;

public class JavaFileMethod {
    private String returnType;
    private String method;
    private String params;
    private String body;
    private JavaAccess access = JavaAccess.PUBLIC;
    private String comment;
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


    public JavaFileMethod comment(String comment) {
        this.comment = comment;
        return this;
    }

    public String getCode() {
        String code = "";

        if (StringUtils.isNotBlank(this.comment)) {
            code += "/**\n";
            code += " * " + this.comment + "\n";
            code += " */";
        }
        if(StringUtils.isNotBlank(this.anno)){
            code += "@" + anno + "\n";
        }
        code += this.access.getAccess() + " " + this.returnType + " " + this.method;
        if (StringUtils.isNotBlank(params)) {
            code += "(" + this.params + ")";
        } else {
            code += "()";
        }
        if (StringUtils.isNotBlank(this.body)) {
            code += " {\n";
//            code += "    " + this.body + "\n";
            code = append(code , this.body , 1);
            code += "}";
        } else {
            code += ";";
        }

        return code;
    }

    private String append(String code , String content, int tabCount) {
        String[] lines = content.split("\n");
        String tab = "";
        for (int i = 0; i < tabCount; i++) {
            tab += "    ";
        }
        for (String line : lines) {
//            sb.append(tab).append(line).append("\n");
            code += tab + line + "\n";
        }
        return code;
    }

    public JavaFileMethod anno(String anno) {
        this.anno = anno;
        return this;
    }

    public String getReturnType() {
        return returnType;
    }

    public String getMethod() {
        return method;
    }

    public String getParams() {
        return params;
    }

    public String getBody() {
        return body;
    }

    public JavaAccess getAccess() {
        return access;
    }

    public String getComment() {
        return comment;
    }

    public String getAnno() {
        return anno;
    }
}
