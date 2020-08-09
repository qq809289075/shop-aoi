package com.fh.shop.api.book.biz;

import com.fh.shop.api.common.ServerResponse;
import com.fh.shop.api.book.mapper.IMapperBook;
import com.fh.shop.api.book.param.BookWhere;
import com.fh.shop.api.book.param.DataTableResult;
import com.fh.shop.api.book.po.Book;
import com.fh.shop.api.book.po.Type;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service("serviceBook")
public class IServiceBookImpl implements IServiceBook {
    @Autowired
    private IMapperBook iMapperBook;

    @Override
    public ServerResponse getTypeList() {

        List<Type> typeList = iMapperBook.getTypeList();

        return ServerResponse.success(typeList);
    }

    @Override
    public ServerResponse addBook(Book book) {

             iMapperBook.insert(book);
        return  ServerResponse.success();
    }

    @Override
    public com.fh.shop.api.common.ServerResponse deleteBook(Integer id) {
        iMapperBook.deleteById(id);
        return ServerResponse.success();
    }


    @Override
    public DataTableResult findList(BookWhere bookWhere) {

        Long totalCount = iMapperBook.findCount(bookWhere);

        List <Book> bookList = iMapperBook.findList(bookWhere);

        return new DataTableResult(bookWhere.getDraw(), totalCount, totalCount, bookList);
    }


    @Override
    public ServerResponse updateBook(Book book) {
        iMapperBook.updateById(book);
        return ServerResponse.success();
    }

    @Override
    public Book findById(Long id) {

        return iMapperBook.selectById(id);
    }


}
