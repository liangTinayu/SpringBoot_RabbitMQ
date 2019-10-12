package com.lty.common.bean;

import java.io.Serializable;

public class Order implements Serializable {
    private static final long serialVersionUID = -4524367558713982150L;
    private Integer id;
    private String name;

    public Order() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
