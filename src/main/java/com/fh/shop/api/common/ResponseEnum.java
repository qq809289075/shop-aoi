package com.fh.shop.api.common;

public enum ResponseEnum {


    PA_ZHIFU_ERROR(5000,"支付失败！！！"),

    ORDER_ERROR(4002,"下订单出现错误！！！"),
    ORDER_SUCCESS_PD(4001,"正在排队处理中！！！"),
    ORDER_STOCK_LESS(4000,"订单库存不足！！！"),


    ITEM_ADD_IDS_NULL(3003,"批量删除的Id不能为空！！"),
    CART_ADD_ERER_NO(3002,"添加商品不合法！！"),
    CART_IS_ISHOT(3001,"商品已下架！！"),
    CART_IS_NOT_NULL(3000,"商品不存在！！"),


    LOGIN_HEADER_TIME_OUT(2006,"头信息超时！！"),
    LOGIN_HEADER_CHANGE(2005,"头信息被篡改！！"),
    LOGIN_HEADER_IS_MISS(2004,"头信息不完整！！"),
    LOGIN_HEADER_IS_NULL(2003,"头信息丢失！！"),
    LOGIN_POSSWROD_NEXT(2002,"密码错误！！"),
    LOGIN_MEMBERNAME_NEXT(2001,"用户不存在！！"),
    LOGIN_MEMBER_POSSWROD_NULL(2000,"用户名密码为空！！"),


    MEMER_MEMBER_ECZ(10001,"会员已存在！！！"),
    MEMER_MAIL_ECZ(10002,"邮箱已存在！！！"),
    MEMER_PHONE_ECZ(10003,"手机号已存在！！！"),
   MEMER_IS_NULL(10000,"注册不能为空！！！")
   ;

    private int code;
    private String msg;

    private ResponseEnum(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
