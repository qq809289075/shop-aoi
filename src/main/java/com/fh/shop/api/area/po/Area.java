package com.fh.shop.api.area.po;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
@Data
@TableName("t_area")
public class Area implements Serializable {

    private Long id;

    private String name;

    private Long pid;

}
