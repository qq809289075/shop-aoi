package com.fh.shop.api.type.mapper;





import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fh.shop.api.type.po.Type;

import java.util.List;

public interface ITypeMapper extends BaseMapper<Type> {
    List<Type> findTypeList();

    void editType(Type type);

    Type findTypeById(Long id);

    void deleteType(List<Long> ids);

    void addType(Type type);

    List<Type> findTypeByPid(Long pid);
}
