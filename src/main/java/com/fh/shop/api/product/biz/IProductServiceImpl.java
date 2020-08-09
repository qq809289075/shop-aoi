package com.fh.shop.api.product.biz;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fh.shop.api.common.ServerResponse;
import com.fh.shop.api.product.mapper.IProductMapper;
import com.fh.shop.api.product.po.Product;
import com.fh.shop.api.product.vo.ProductVo;
import com.fh.shop.api.utils.RedisUitl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

@Service("productService")
public class IProductServiceImpl implements IProductService {
    @Autowired
    private IProductMapper productMapper;

    @Override
    public ServerResponse findHotList() {

        String hotProductList = RedisUitl.get("hotProductList");
        if(!StringUtils.isEmpty(hotProductList)){
            List <ProductVo> productList = JSONObject.parseArray(hotProductList, ProductVo.class);
            return ServerResponse.success(productList);
        }

        QueryWrapper <Product> productQueryWrapper = new QueryWrapper <>();
        productQueryWrapper.select("id","name","price","filePath");
        productQueryWrapper.eq("isHot",1);
        productQueryWrapper.eq("isup",1);
        List <Product> productList = productMapper.selectList(productQueryWrapper);
        List<ProductVo> productVoList = new ArrayList <>();
        for (Product product : productList) {
            ProductVo productVo =new ProductVo();
            productVo.setId(product.getId());
            productVo.setName(product.getName());
            productVo.setPrice(product.getPrice().toString());
            productVo.setFilePath(product.getFilePath());

            productVoList.add(productVo);
        }
        String hotProductListJson = JSONObject.toJSONString(productVoList);
         RedisUitl.set("hotProductList",hotProductListJson);

        return ServerResponse.success(productVoList);
    }

    @Override
    public List <Product> findSrockLess() {
        QueryWrapper <Product> productQueryWrapper = new QueryWrapper <>();
        productQueryWrapper.lt("stock", 10);
        List <Product> products = productMapper.selectList(productQueryWrapper);

        return products;
    }
}
