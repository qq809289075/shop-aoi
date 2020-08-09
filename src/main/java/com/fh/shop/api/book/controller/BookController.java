package com.fh.shop.api.book.controller;

import com.fh.shop.api.book.biz.IServiceBook;

import com.fh.shop.api.book.param.BookWhere;
import com.fh.shop.api.book.param.DataTableResult;
import com.fh.shop.api.book.po.Book;
import com.fh.shop.api.common.ServerResponse;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/api/book")
public class BookController {

   @Resource(name = "serviceBook")
     private IServiceBook iServiceBook;


    @PostMapping
    public ServerResponse add(Book book) {

        return iServiceBook.addBook(book);
    }
    @DeleteMapping
    public ServerResponse deleteBook( Integer id) {

        return iServiceBook.deleteBook(id);
    }

    @GetMapping
    private DataTableResult findList(BookWhere bookWhere){
        DataTableResult bookList = iServiceBook.findList(bookWhere);
        return bookList;
    }
    @RequestMapping("/getTypeList")
    public ServerResponse getTypeList () {
        ServerResponse typeList = iServiceBook.getTypeList();
        return  typeList;
    }

    @PutMapping
    public ServerResponse updateBook( Book  book) {

        return iServiceBook.updateBook(book);
    }
    @GetMapping("/findById")
    public ServerResponse findById(Long id){
        Book book = iServiceBook.findById(id);

        return  ServerResponse.success(book);
    }
}
