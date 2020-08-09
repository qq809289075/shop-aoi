package com.fh.shop.api.brand.mapper;

import com.fh.shop.api.brand.po.Brand;
import org.apache.ibatis.annotations.*;

import java.util.List;

public interface IBrandMapper {

     @Insert("insert into t_brand (brandname) values (#{brandName})")
     void addBrand(Brand brand);

     @Delete("delete from t_brand where id = #{v}" )
     Long deleteBrand(Long id);

     @Select("select * from t_brand")
     List<Brand> getBrandList();


    @Update("update t_brand set brandname=#{brandName} where id = #{id}")
     Brand updateBrand(Brand brand);


     @Delete(" <script> delete from t_brand where id in\n" +
             "    <foreach collection=\"list\" item=\"id\" open=\"(\" close=\")\" separator=\",\">\n" +
             "        #{id}\n" +
             "    </foreach></script>")
    void deleteBatch(List<Long> idsList);
}
