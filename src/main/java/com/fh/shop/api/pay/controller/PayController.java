package com.fh.shop.api.pay.controller;

import com.fh.shop.api.annotation.check;
import com.fh.shop.api.common.ServerResponse;
import com.fh.shop.api.common.SystemConstant;
import com.fh.shop.api.member.vo.MemberVo;
import com.fh.shop.api.pay.biz.IPayService;
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
@RequestMapping("/api/pay")
@Api(tags = "支付接口")
public class PayController {
    @Resource(name = "payService")
    private IPayService payService;

    @GetMapping("/createNative")
    @check
    @ApiOperation("统1下单")
    @ApiImplicitParams({
            @ApiImplicitParam(name="x-auth",value = "头部信息" ,type = "string" ,required = true,paramType = "header")
    })
    public ServerResponse createNative(HttpServletRequest request){
        MemberVo member = (MemberVo) request.getAttribute(SystemConstant.CURR_MEMBER);
        Long memberId = member.getId();

        return  payService.createNative(memberId);
    }
    @GetMapping("/queryStatus")
    @check
    @ApiOperation("查询订单状态")
    @ApiImplicitParams({
            @ApiImplicitParam(name="x-auth",value = "头部信息" ,type = "string" ,required = true,paramType = "header")
    })
    public ServerResponse queryStatus(HttpServletRequest request){
        MemberVo member = (MemberVo) request.getAttribute(SystemConstant.CURR_MEMBER);
        Long memberId = member.getId();
        return  payService.queryStatus(memberId);
    }

}
