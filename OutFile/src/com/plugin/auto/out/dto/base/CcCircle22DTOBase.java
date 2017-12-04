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
    private Date d;

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return this.id;
    }

    public void setD(Date d) {
        this.d = d;
    }

    public Date getD() {
        return this.d;
    }

}
