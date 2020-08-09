package com.fh.shop.api.utils;

import org.apache.commons.lang3.StringUtils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {

    public static  final  String  Y_M_D="yyyy-MM-dd";
    public static  final  String  FULL_TIME="yyyy-MM-dd HH:mm:ss";

    public static  String date2str(Date  date,String str){
        if(date==null){
           return  "";
        }
        SimpleDateFormat  sim=new SimpleDateFormat(str);
        String result = sim.format(date);
        return result;
    }

    public static Date str2date(String date,String str){
        if(StringUtils.isEmpty(date)){
            throw  new RuntimeException("日期格式的字符串为空");
        }
        SimpleDateFormat  sim =new SimpleDateFormat(str);
        Date date2=null;
        try{
            date2 = sim.parse(date);
        }catch (Exception e){
            e.printStackTrace();
        }
        return date2;
    }

}
