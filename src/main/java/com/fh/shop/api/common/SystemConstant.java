package com.fh.shop.api.common;

public class SystemConstant {


    public static final  String CURR_MEMBER ="member";

    public static final  int CART_MEMBER = 0;

    public interface  OrderStatus  {
        int WAIT_PA =10;
        int PA_SUEEESS =20;
        int SEND_GOODS =30;
    }
    public interface  payStatus  {
        int PA_WAIT =10;
        int PA_SUEEESS =20;

    }
}
