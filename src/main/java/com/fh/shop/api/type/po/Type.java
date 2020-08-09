package com.fh.shop.api.type.po;

import lombok.Data;

import java.io.Serializable;
@Data
public class Type implements Serializable {

    private Long id;

    private String typeName;

    private Long pid;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public Long getPid() {
        return pid;
    }

    public void setPid(Long pid) {
        this.pid = pid;
    }
}
