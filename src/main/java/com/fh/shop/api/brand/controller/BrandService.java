package com.fh.shop.api.brand.controller;

import com.fh.shop.api.brand.biz.IBrandService;
import com.fh.shop.api.common.ServerResponse;
import com.fh.shop.api.brand.po.Brand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/brand")
public class BrandService {

    @Autowired
    private IBrandService brandService;

    @PostMapping
    public ServerResponse add(Brand brand) {

        return brandService.addBrand(brand);
    }
    @DeleteMapping("/{id}")
    public ServerResponse deleteBrand(@PathVariable Long id) {

        return brandService.deleteBrand(id);
    }

    @GetMapping
    public ServerResponse getBrandList() {

        return  brandService.getBrandList();
    }
    @PutMapping
    public ServerResponse updateBrand(@RequestBody Brand brand) {

        return brandService.updateBrand(brand);
    }
    @DeleteMapping()
    public ServerResponse deleteBatch(String  ids) {

        return brandService.deleteBatch(ids);
    }

}
