package com.fh.shop.api.utils;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class RadisPool {

    private  RadisPool(){

    }

    private  static JedisPool jedisPool;

      private static void initPool(){

          JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();

             jedisPoolConfig.setMaxTotal(1000);
             jedisPoolConfig.setMinIdle(500);
             jedisPoolConfig.setMaxIdle(500);

          jedisPool = new JedisPool(jedisPoolConfig, "172.16.31.139", 7021);
      }

      static {
          initPool();
      }

      public static Jedis getResource(){
          return jedisPool.getResource();
      }

}
