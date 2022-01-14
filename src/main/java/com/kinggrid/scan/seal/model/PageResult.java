package com.kinggrid.scan.seal.model;

public class PageResult <T> {
    private Integer total;
    private T rows;

    public PageResult() {
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public T getRows() {
        return rows;
    }

    public void setRows(T rows) {
        this.rows = rows;
    }
}
