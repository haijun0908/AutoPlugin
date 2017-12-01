package com.bookingtee.plugin.info;

public enum JavaAccess {
    PUBLIC("public"),
    PRIVATE("private");

    private String access;
    JavaAccess(String access) {
        this.access = access;
    }

    public String getAccess() {
        return access;
    }
}
