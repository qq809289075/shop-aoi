package com.fh.shop.api.utils;

public class KeyUtil {

    public static final int MEMBER_KEY_EXPIRE = 5 * 60;

    public static String buildMemberKey(String uuid, Long memberId) {
        return "member:" + uuid + ":" + memberId;
    }


    public static String buildMemberKey(Long memberId) {

        return "cart:" + memberId;
    }

    public static String buildStockLessKey(Long memberId) {

        return "order:stock:less:" + memberId;
    }

    public static String buildOderKey(Long memberId) {
        return "order:" + memberId;

    }

    public static String buildPayLogKey(Long memberId) {
        return "paylog:"+memberId;
    }

    public static String buildOrderErrorKey(Long memberId) {
        return "order:error:" + memberId;
    }
}
