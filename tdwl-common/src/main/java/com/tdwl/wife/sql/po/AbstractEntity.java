package com.tdwl.wife.sql.po;

import java.io.Serializable;

public abstract class AbstractEntity extends AbstractPo implements Serializable {

    private static final long serialVersionUID = -2003582091127969079L;

    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        AbstractEntity that = (AbstractEntity) o;
        if (id != null ? !id.equals(that.id) : that.id != null) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

}
