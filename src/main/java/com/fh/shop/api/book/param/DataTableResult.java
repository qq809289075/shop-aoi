package com.fh.shop.api.book.param;

import java.io.Serializable;
import java.util.List;

public class DataTableResult<T> extends Page implements Serializable {
    private Long draw;

    private Long recordsFiltered;

    private Long recordsTotal;

    private List<T> data;

    public DataTableResult(Long draw, Long recordsFiltered, Long recordsTotal, List<T> data) {
        this.draw = draw;
        this.recordsFiltered = recordsFiltered;
        this.recordsTotal = recordsTotal;
        this.data = data;
    }

    public DataTableResult(){}

    public Long getDraw() {
        return draw;
    }

    public void setDraw(Long draw) {
        this.draw = draw;
    }

    public Long getRecordsFiltered() {
        return recordsFiltered;
    }

    public void setRecordsFiltered(Long recordsFiltered) {
        this.recordsFiltered = recordsFiltered;
    }

    public Long getRecordsTotal() {
        return recordsTotal;
    }

    public void setRecordsTotal(Long recordsTotal) {
        this.recordsTotal = recordsTotal;
    }

    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }
}
