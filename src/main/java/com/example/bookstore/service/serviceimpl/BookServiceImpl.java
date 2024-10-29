package com.example.bookstore.service.serviceimpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.example.bookstore.dao.BookDao;
import com.example.bookstore.dto.BookQueryParams;
import com.example.bookstore.dto.BookRequest;
import com.example.bookstore.model.Book;
import com.example.bookstore.service.BookService;

@Component
public class BookServiceImpl implements BookService{

    @Autowired BookDao bookDao;

    // 查詢書籍資訊 by bookId 回傳 BookDao.getBookInfoById(bookId)
    @Override
    public Book getBookInfoById(Integer bookId) {
        return bookDao.getBookInfoById(bookId);
    }

    // 新增書籍 回傳 BookDao.addBook(bookRequest)
    @Override
    public Integer addBook(BookRequest bookRequest) {
        return bookDao.addBook(bookRequest);
    }

    // 更新書籍資訊 回傳 BookDao.updateBook(bookId, bookRequest)
    @Override
    public void updateBook(Integer bookId, BookRequest bookRequest) {
        bookDao.updateBook(bookId, bookRequest);
    }

    // 刪除書籍資訊 回傳 BookDao.deleteBook(bookId)
    @Override
    public void deleteBook(Integer bookId) {
        bookDao.deleteBook(bookId);
    }

    // 查詢所有書籍 回傳 BookDao.getBooks(bookQueryParams)
    @Override
    public List<Book> getBooks(BookQueryParams bookQueryParams) {
        return bookDao.getBooks(bookQueryParams);
    }

    // 查詢書籍數量 回傳 BookDao.countBooks(bookQueryParams)
    @Override
    public Integer countBooks(BookQueryParams bookQueryParams) {
        return bookDao.countBooks(bookQueryParams);
    }
}
