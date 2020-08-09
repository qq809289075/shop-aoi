package com.fh.shop.api.interceptor;

import com.alibaba.fastjson.JSONObject;
import com.fh.shop.api.common.ResponseEnum;
import com.fh.shop.api.common.SystemConstant;
import com.fh.shop.api.annotation.check;
import com.fh.shop.api.exception.GlobalException;
import com.fh.shop.api.member.vo.MemberVo;
import com.fh.shop.api.utils.KeyUtil;
import com.fh.shop.api.utils.MD5Util;
import com.fh.shop.api.utils.RedisUitl;
import org.springframework.http.HttpHeaders;
import org.springframework.util.StringUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.Base64;

public class LoginInterceptor extends HandlerInterceptorAdapter {

    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        //跨域处理
        response.setHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN,"*");
          //处理自定请求头信息
        response.setHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_HEADERS,"x-auth,content-type");
       //处理客户端发送的各种请求类型
        response.setHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_METHODS,"GET,DELETE,PUT,POST");

        String requestMethod= request.getMethod();
        if(requestMethod.equalsIgnoreCase("options")){
          //阻止后续访问
           return false;
        }

        //通过自定义注解  来决定那个方法被拦截
        HandlerMethod handlerMethod  =  (HandlerMethod) handler;

        Method method = handlerMethod.getMethod();
        if(!method.isAnnotationPresent(check.class)){
            return true;
        }
        String header = request.getHeader("x-auth");
        if(StringUtils.isEmpty(header)){
              throw new GlobalException(ResponseEnum.LOGIN_HEADER_IS_NULL);
        }

        String[] split = header.split("\\.");
        if(split.length != 2){
            throw new GlobalException(ResponseEnum.LOGIN_HEADER_IS_MISS);
        }

        String memberJsonBase64 =split[0];
        String signBase64 =split[1];
        String newSign = MD5Util.sign(memberJsonBase64, MD5Util.SECRET);
        String newSignBase64= Base64.getEncoder().encodeToString(newSign.getBytes("utf-8"));
        if(!newSignBase64.equals(signBase64)){
            throw new GlobalException(ResponseEnum.LOGIN_HEADER_CHANGE);
        }
        String memberJson = new String(Base64.getDecoder().decode(memberJsonBase64),"utf-8");
        MemberVo memberVo = JSONObject.parseObject(memberJson, MemberVo.class);
        Long id = memberVo.getId();
        String uuid = memberVo.getUuid();
        boolean exist = RedisUitl.exist(KeyUtil.buildMemberKey(uuid, id));
        if(!exist){
            throw new GlobalException(ResponseEnum.LOGIN_HEADER_TIME_OUT);
        }
        RedisUitl.expire(KeyUtil.buildMemberKey(uuid,id), KeyUtil.MEMBER_KEY_EXPIRE);

        request.setAttribute(SystemConstant.CURR_MEMBER,memberVo);
        return true;

    }
}
