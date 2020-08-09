package com.fh.shop.api.order.controller;

import com.fh.shop.api.annotation.check;
import com.fh.shop.api.common.ServerResponse;
import com.fh.shop.api.common.SystemConstant;
import com.fh.shop.api.member.vo.MemberVo;
import com.fh.shop.api.order.biz.IOrderService;
import com.fh.shop.api.order.param.OrderParam;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/order")
@Api(tags = "订单接口")
public class OrderController {

     @Resource(name = "orderService")
     private IOrderService orderService ;


    @GetMapping("/genOrderConfirm")
    @check
    @ApiOperation("生成订单确认模块")
    @ApiImplicitParams({
            @ApiImplicitParam(name="x-auth",value = "头部信息" ,type = "string" ,required = true,paramType = "header")
    })
    public ServerResponse genOrderConfirm(HttpServletRequest request){
        MemberVo member = (MemberVo) request.getAttribute(SystemConstant.CURR_MEMBER);
        Long memberId = member.getId();

      return  orderService.genOrderConfirm(memberId);
    }
    @PostMapping("/genOrder")
    @check
    @ApiOperation("生成订单")
    @ApiImplicitParams({
            @ApiImplicitParam(name="x-auth",value = "头部信息" ,type = "string" ,required = true, paramType = "header"),
            @ApiImplicitParam(name="payType",value = "1微信2支付宝" ,type = "int" ,required = true, paramType = "query"),
            @ApiImplicitParam(name="recipientId",value = "收件人id‘" ,type = "long" ,required = true, paramType = "query")
    })
    public ServerResponse genOrder(HttpServletRequest request , OrderParam orderParam){
        MemberVo member = (MemberVo) request.getAttribute(SystemConstant.CURR_MEMBER);
        Long memberId = member.getId();
        orderParam.setMemberId(memberId);
        return orderService.genOrder(orderParam);

    }
    @GetMapping("/getResult")
    @check
    @ApiOperation("获取订单结果")
    @ApiImplicitParams({
            @ApiImplicitParam(name="x-auth",value = "头部信息" ,type = "string" ,required = true, paramType = "header"),
            @ApiImplicitParam(name="payType",value = "1微信2支付宝" ,type = "int" ,required = true, paramType = "query"),
            @ApiImplicitParam(name="recipientId",value = "收件人id‘" ,type = "long" ,required = true, paramType = "query")
    })
    public ServerResponse getResult(HttpServletRequest request) {

        MemberVo member = (MemberVo) request.getAttribute(SystemConstant.CURR_MEMBER);
        Long memberId = member.getId();
        return orderService.getResult(memberId);
    }
}
