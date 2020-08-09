package com.fh.shop.api.book.po;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("t_type")
public class Type {

    private Integer tid;

    private  String tname;
}
