package com.example.bookstore.dao;

import java.util.List;

import com.example.bookstore.dto.BookQueryParams;
import com.example.bookstore.dto.BookRequest;
import com.example.bookstore.model.Book;

public interface BookDao {

    // 查詢書籍資訊 by bookId
    Book getBookInfoById(Integer bookId);

    // 新增書籍
    Integer addBook(BookRequest bookRequest);

    // 更新書籍資訊
    void updateBook(Integer bookId, BookRequest bookRequest);

    // 更新書籍庫存
    void updateStock(Integer bookId, Integer Stock);

    // 刪除書籍資訊
    void deleteBook(Integer bookId);

    // 查詢所有書籍
    List<Book> getBooks(BookQueryParams bookQueryParams);

    // 查詢書籍數量
    Integer countBooks(BookQueryParams bookQueryParams);
}
