package com.fh.shop.api.product.controller;


import com.fh.shop.api.common.ServerResponse;
import com.fh.shop.api.product.biz.IProductService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/api/products")
@Api(value = "商品信息")
public class ProductController {

    @Resource(name = "productService")
    private IProductService productService;


    @GetMapping
    @ApiOperation(value = "查询热销商品信息",notes = "查询商品信息")
    public ServerResponse findHotList(){

        return productService.findHotList();
    }


}
