package com.fh.shop.api.brand.biz;

import com.fh.shop.api.common.ServerResponse;
import com.fh.shop.api.brand.po.Brand;

public interface IBrandService {

    ServerResponse addBrand(Brand brand);

    ServerResponse deleteBrand(Long id);

    ServerResponse getBrandList();

    ServerResponse updateBrand(Brand brand);

    ServerResponse deleteBatch(String ids);
}
