package com.fh.shop.api.brand.biz;

import com.fh.shop.api.common.ServerResponse;
import com.fh.shop.api.brand.mapper.IBrandMapper;
import com.fh.shop.api.brand.po.Brand;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class IBrandServiceImpl implements IBrandService {
    @Autowired
    private IBrandMapper brandMapper;


    @Override
    public ServerResponse addBrand(Brand brand) {
               brandMapper.addBrand(brand);
        return  ServerResponse.success();
    }

    @Override
    public ServerResponse deleteBrand(Long id) {
           brandMapper.deleteBrand(id);
        return ServerResponse.success();
    }

    @Override

    public ServerResponse getBrandList() {
        List <Brand> brandList = brandMapper.getBrandList();
        return ServerResponse.success(brandList);

    }

    @Override
    public ServerResponse updateBrand(Brand brand) {
        brandMapper.updateBrand(brand);
        return ServerResponse.success();
    }

    @Override
    public ServerResponse deleteBatch(String ids) {
             if(StringUtils.isNotEmpty(ids)){
              String[] idsArr= ids.split(",");
              List<Long> idsList = Arrays.stream(idsArr).map(x ->Long.parseLong(x)).collect(Collectors.toList());
                 brandMapper.deleteBatch(idsList);
             }
        return ServerResponse.success();
    }


}
