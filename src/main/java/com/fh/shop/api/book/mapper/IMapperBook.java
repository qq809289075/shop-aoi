package com.fh.shop.api.book.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fh.shop.api.book.param.BookWhere;
import com.fh.shop.api.book.po.Book;

import com.fh.shop.api.book.po.Type;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface IMapperBook extends BaseMapper<Book> {

    Long findCount(BookWhere bookWhere);

    List<Book> findList(BookWhere bookWhere);
    @Select("select * from t_type")
    List<Type> getTypeList();

}
