package com.plugin.auto.out.model;

import java.io.Serializable;
import java.util.Date;

public class CcCircle22Model implements Serializable {

    private static final long serialVersionUID = -1L;

    /**
     * zhujianid
     */
    private int id;

    /**
     * zhujianid
     */
    private Date createTime;

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return this.id;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getCreateTime() {
        return this.createTime;
    }

}