package com.fh.shop.api.book.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.util.Date;

@Data
@TableName("t_boo")
public class Book {

    @TableId(value = "id",type = IdType.AUTO)
    private  Integer id;

    private  String name;

    private BigDecimal price ;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date   timeDate;

    private  Integer  tid;
     @TableField(exist = false)
    private  String tname;
}
