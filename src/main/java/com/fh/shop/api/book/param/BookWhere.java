package com.fh.shop.api.book.param;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class BookWhere extends Page {

    private String name;

    private BigDecimal minPrice;

    private BigDecimal maxPrice;

    private Long tid;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date minTimeDate;


    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date maxTimeDate;


}
