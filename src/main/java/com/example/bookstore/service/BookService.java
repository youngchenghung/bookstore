package com.example.bookstore.service;

import com.example.bookstore.dto.BookQueryParams;
import com.example.bookstore.dto.BookRequest;
import com.example.bookstore.model.Book;

import java.util.List;

import jakarta.validation.Valid;

public interface BookService {

    // 查詢書籍資訊 by bookId
    Book getBookInfoById(Integer bookId);

    // 新增書籍
    Integer addBook(@Valid BookRequest bookRequest);

    // 更新書籍資訊
    void updateBook(Integer bookId, @Valid BookRequest bookRequest);

    // 刪除書籍資訊
    void deleteBook(Integer bookId);

    // 查詢所有書籍
    List<Book> getBooks(BookQueryParams bookQueryParams);

    // 查詢書籍數量
    Integer countBooks(BookQueryParams bookQueryParams);
}
