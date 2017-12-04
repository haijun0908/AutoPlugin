package com.plugin.auto.out.dto.base;

import java.io.Serializable;
import java.util.Date;

public abstract class CcCircle22DTOBase implements Serializable {

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
