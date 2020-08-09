package com.fh.shop.api.member.po;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
@TableName("t_member")
public class Member {

    private Long id ;

    private String memberName;

    private String password;

    private String realName;
   @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date birthday;

    private String mail;

    private Integer areaId;

    private  Integer  shengId;

    private  Integer  shiId;

    private  Integer  xianId;

    private String  areaName;

    private  String phone ;
}
