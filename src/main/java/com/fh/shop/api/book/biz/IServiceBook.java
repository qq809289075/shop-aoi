package com.fh.shop.api.book.biz;

import com.fh.shop.api.common.ServerResponse;
import com.fh.shop.api.book.param.BookWhere;
import com.fh.shop.api.book.param.DataTableResult;
import com.fh.shop.api.book.po.Book;


public interface IServiceBook {

    ServerResponse getTypeList();

    ServerResponse addBook(Book book);

    ServerResponse deleteBook(Integer id);

    DataTableResult findList(BookWhere bookWhere);

    ServerResponse updateBook(Book book);

    Book findById(Long id);
}
