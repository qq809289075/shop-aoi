package com.fh.shop.api.product.po;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
@TableName("fh_product")
public class Product implements Serializable {

    private Long id;

    private String name;

    private BigDecimal price;

    private Long brandId;
    @TableField(exist = false)
    private String brandName;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date createDate;

    private Date showTime;

    private Date updateTime;

    private String filePath;
    @TableField(exist = false)
    private String oldFilePath;

    private Integer isHot;

    private Integer isup;

    private  Integer stock;


}
