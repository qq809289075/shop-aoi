package com.fh.shop.api.type.controller;


import com.fh.shop.api.common.ServerResponse;
import com.fh.shop.api.type.biz.ITypeService;
import com.fh.shop.api.type.po.Type;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/api/type")
public class TypeController {

    @Resource(name="typeService")
    private ITypeService typeService;

    //跳转展示
    @GetMapping("toIndex")
    public String toIndex(){
        return "type/index";
    }

    //查询
    @GetMapping("findType")
    public ServerResponse findType(){
        List<Type> typeList = typeService.findTypeList();
        return ServerResponse.success(typeList);
    }
    //添加
    @PostMapping("addType")
    public ServerResponse addType(Type type){
        typeService.addType(type);
        return ServerResponse.success(type.getId());
    }
    //删除
    @DeleteMapping("deleteType")

    public ServerResponse deleteType(@RequestParam("ids[]") List<Long> ids){
        typeService.deleteType(ids);
        return ServerResponse.success();
    }
    //根据id来查询要修改的数据
    @GetMapping("findTypeById")
    public ServerResponse findTypeById(Long id){
       Type type =  typeService.findTypeById(id);
        return ServerResponse.success(type);
    }
    //修改
    @PutMapping("editType")
    public ServerResponse editType(Type type){
        typeService.editType(type);
        return ServerResponse.success();
    }
    @GetMapping("findTypeByPid")
    public ServerResponse findTypeByPid(Long pid){
        List<Type> typeList = typeService.findTypeByPid(pid);
        return ServerResponse.success(typeList);
    }
}
