package com.fh.shop.api.cart.controller;

import com.fh.shop.api.annotation.check;
import com.fh.shop.api.cart.biz.ICartService;
import com.fh.shop.api.common.ServerResponse;
import com.fh.shop.api.common.SystemConstant;
import com.fh.shop.api.member.vo.MemberVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/cart")
@Api(tags = "购物车接口")
public class CartController {

    @Resource(name = "cartService")
    private ICartService cartService;


    @PostMapping("/addItem")
    @check
    @ApiOperation("添加商品到购物车")
    @ApiImplicitParams({
            @ApiImplicitParam(name="x-auth",value = "头部信息" ,type = "string" ,required = true,paramType = "header"),
            @ApiImplicitParam(name="goodsId",value = "商品Id" ,type = "long" ,required = true,paramType = "query"),
            @ApiImplicitParam(name="num",value = "商品数量" ,type = "int" ,required = true,paramType = "query")

    }
    )
    public ServerResponse addItem(HttpServletRequest request, Long goodsId , int num){
        MemberVo member = (MemberVo) request.getAttribute(SystemConstant.CURR_MEMBER);
        Long memberId = member.getId();

        return cartService.addItem(memberId ,goodsId ,num);
    }

    @GetMapping("/findItemList")
    @check
    @ApiOperation("获取指定会员的购物车")

    @ApiImplicitParams({
            @ApiImplicitParam(name="x-auth",value = "头部信息" ,type = "string" ,required = true,paramType = "header")
    })

    public ServerResponse findItemList(HttpServletRequest request){
        MemberVo member = (MemberVo) request.getAttribute(SystemConstant.CURR_MEMBER);

        Long memberId = member.getId();
        return cartService.findItemList(memberId);
    }

    @GetMapping("/findItemNum")
    @check
    @ApiOperation("获取指定会员的购物车的商品个数")

    @ApiImplicitParams({
            @ApiImplicitParam(name="x-auth",value = "头部信息" ,type = "string" ,required = true,paramType = "header")
    })

    public ServerResponse findItemNum(HttpServletRequest request){
        MemberVo member = (MemberVo) request.getAttribute(SystemConstant.CURR_MEMBER);
        Long memberId = member.getId();
        return cartService.findItemNum(memberId);
    }

    @DeleteMapping("/deleteCartItem/{id}")
    @check
    @ApiOperation("获取删除的商品Id")

    @ApiImplicitParams({
            @ApiImplicitParam(name="x-auth",value = "头部信息" ,type = "string" ,required = true,paramType = "header"),
            @ApiImplicitParam(name="id",value = "商品Id" ,type = "long" ,required = true,paramType = "path")

    })
    public ServerResponse deleteCartItem(HttpServletRequest request,@PathVariable("id") Long goodsId) {
        MemberVo member = (MemberVo) request.getAttribute(SystemConstant.CURR_MEMBER);
        Long memberId = member.getId();

          return cartService.deleteCartItem(memberId,goodsId);
    }
    @DeleteMapping("/deleteBatchItems")
    @check
    @ApiOperation("获取删除的商品Ids")

    @ApiImplicitParams({
            @ApiImplicitParam(name="x-auth",value = "头部信息" ,type = "string" ,required = true,paramType = "header"),
            @ApiImplicitParam(name="ids",value = "选中的id如1，2,3" ,type = "String" ,required = true,paramType = "query")

    })
    public ServerResponse deleteBatchItems(HttpServletRequest request,String ids){
        MemberVo member = (MemberVo) request.getAttribute(SystemConstant.CURR_MEMBER);
        Long memberId = member.getId();
        return cartService.deleteBatchItems(memberId,ids);
    }
}
