package com.fh.shop.api.utils;


import redis.clients.jedis.Jedis;

public class RedisUitl {


    public static  void expire(String key,int seconds){

        Jedis jedis = null;
        try {
            jedis = RadisPool.getResource();
            jedis.expire(key,seconds);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }finally {
            if(jedis !=null){
                jedis.close();
            }
        }
    }


    public static  boolean exist(String key){

        Jedis jedis = null;
        try {
            jedis = RadisPool.getResource();
           return jedis.exists(key);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }finally {
            if(jedis !=null){
                jedis.close();
            }
        }
    }

    public static void setEx(String key, String value,int souceds){

        Jedis jedis =null;
        try {
            jedis = RadisPool.getResource();
            jedis.setex(key,souceds,value);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }finally {
            if(jedis !=null){
                jedis.close();
            }
        }

    }


    public static void del(String key){
        Jedis jedis =null;
        try {
            jedis = RadisPool.getResource();
            jedis.del(key);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }finally {
            if(jedis !=null){
                jedis.close();
            }
        }
    }

    public static void set(String key, String value){

            Jedis jedis = null;
        try {
            jedis = RadisPool.getResource();
            jedis.set(key, value);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }finally {
            if(jedis !=null){
                jedis.close();
            }
        }

    }

    public static String get(String key){
        Jedis jedis =null;
        try {
            jedis = RadisPool.getResource();
            String res = jedis.get(key);
            return res ;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }finally {
            if(jedis !=null){
                jedis.close();
            }
        }
    }
}
