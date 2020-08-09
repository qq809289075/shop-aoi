package com.fh.shop.api.type.biz;


import com.fh.shop.api.type.po.Type;

import java.util.List;

;

public interface ITypeService {

    void addType(Type type);

    void deleteType(List<Long> ids);

    Type findTypeById(Long id);

    void editType(Type type);

    List<Type> findTypeList();

    List<Type> findTypeByPid(Long pid);
}
