package com.example.bookstore.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.bookstore.constant.BookCategory;
import com.example.bookstore.dto.BookQueryParams;
import com.example.bookstore.dto.BookRequest;
import com.example.bookstore.model.Book;
import com.example.bookstore.service.BookService;
import com.example.bookstore.util.Page;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

@Validated
@RestController
public class BookController {

    // 日誌事件
    private static final Logger logger = LoggerFactory.getLogger(BookController.class);

    @Autowired
    BookService BookService;

    // 查詢書籍資訊 by bookId
    @GetMapping("/book/{bookId}")
    public ResponseEntity<Book> getBookInfoById (@PathVariable Integer bookId) {

        logger.info("查詢書籍ID: {}", bookId);

        Book bookInfo = BookService.getBookInfoById(bookId);

        // 若查無此書籍，回傳 404 Not Found
        if (bookInfo == null) {
            logger.warn("書籍ID {} 查詢失敗", bookId);

            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        // 若查詢成功，回傳 200 OK
        else {
            logger.info("書籍ID {} 以查詢", bookId);

            return ResponseEntity.status(HttpStatus.OK).body(bookInfo);
        }
    }

    // 新增書籍 回傳新增的書籍資訊
    @PostMapping("/book/add")
    public ResponseEntity<Book> addBook(@RequestBody @Valid BookRequest bookRequest) {

        logger.info("新增書籍: {}", bookRequest);

        // 新增書籍
        Integer bookId = BookService.addBook(bookRequest);

        // 取得新增的書籍資訊 s
        Book bookInfo = BookService.getBookInfoById(bookId);
        logger.info("新增書籍ID: {}", bookId);

        return ResponseEntity.status(HttpStatus.CREATED).body(bookInfo);
    }

    // 更新書籍資訊 回傳更新後的書籍資訊
    @PutMapping("/book/update/{bookId}")
    public ResponseEntity<Book> updateBook(@PathVariable Integer bookId, @RequestBody @Valid BookRequest bookRequest) {
        Book bookInfo = BookService.getBookInfoById(bookId);

        logger.info("更新書籍ID: {}", bookId);

        // 若查無此書籍，回傳 404 Not Found
        if (bookInfo == null) {
            logger.warn("書籍ID {} 查詢失敗", bookId);

            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        // 若更新成功，回傳 200 OK
        else {
            // 更新書籍資訊
            BookService.updateBook(bookId, bookRequest);

            // 取得更新後的書籍資訊
            bookInfo = BookService.getBookInfoById(bookId);
            logger.info("書籍ID {} 更新成功", bookId);
            
            return ResponseEntity.status(HttpStatus.OK).body(bookInfo);
        }
    }

    // 刪除書籍資訊
    @DeleteMapping("/book/delete/{bookId}")
    public ResponseEntity<Book> deleteBook(@PathVariable Integer bookId) {
        Book bookInfo = BookService.getBookInfoById(bookId);
    
        logger.info("刪除書籍ID: {}", bookId);

        // 若查無此書籍，回傳 404 Not Found
        if (bookInfo == null) {
            logger.warn("書籍ID {} 查詢失敗", bookId);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        else {
            BookService.deleteBook(bookId);
            logger.info("書籍ID {} 刪除成功", bookId);

            return ResponseEntity.status(HttpStatus.OK).build();
        }
    }

    // 查詢所有書籍
    @GetMapping("/books")
    public ResponseEntity<Page<Book>> getBookList(
        // 查詢條件
        @RequestParam(required = false) BookCategory category,
        @RequestParam(required = false) String search,
        // 分頁條件
        @RequestParam(defaultValue = "created_date") String orderBy,
        @RequestParam(defaultValue = "desc") String sort,
        // 分頁條件
        @RequestParam(defaultValue = "2") @Max(100) @Min(0) Integer limit, // 每頁顯示筆數 (最多 100 筆)
        @RequestParam(defaultValue = "0") @Min(0) Integer offset // 頁碼 (從 0 開始)
        ) {

        logger.info("查詢書籍列表");

        // 查詢書籍 by 查詢條件 & 分頁條件 & 排序條件 
        BookQueryParams bookQueryParams = new BookQueryParams();
        bookQueryParams.setBookCategory(category);
        bookQueryParams.setSearch(search);
        bookQueryParams.setOrderBy(orderBy);
        bookQueryParams.setSort(sort);
        bookQueryParams.setLimit(limit);
        bookQueryParams.setOffset(offset);

        logger.info("查詢條件: category={}, search={}, orderBy={}, sort={}, limit={}, offset={}", category, search, orderBy, sort, limit, offset);

        // 取得書籍列表 
        List<Book> bookList = BookService.getBooks(bookQueryParams);

        logger.info("書籍列表共 {} 筆", bookList.size());

        // 取得書籍總數
        Integer total = BookService.countBooks(bookQueryParams);

        logger.info("書籍總數: {}", total);

        // 分頁資訊
        Page<Book> page = new Page<>();
        page.setLimit(limit);
        page.setOffset(offset);
        page.setTotal(total);
        page.setResult(bookList);

        logger.info("書籍列表查詢成功");

        return ResponseEntity.status(HttpStatus.OK).body(page);
    }
    
}
