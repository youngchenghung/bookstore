package com.example.bookstore.controller;

import java.io.IOException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.bookstore.cloudinary.CloudinaryService;
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

    @Autowired
    CloudinaryService cloudinaryService;

    // 查詢書籍資訊 by bookId
    @GetMapping("/book/{bookId}")
    @PreAuthorize("isAuthenticated()") // 需要驗證
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

    // 新增書籍 回傳新增後的書籍資訊
    @PostMapping(value = "/book/add", consumes = "multipart/form-data")
    public ResponseEntity<Book> addBook(@ModelAttribute @Valid BookRequest bookRequest) {
        logger.info("新增書籍: {}", bookRequest);

        // 檢查圖片是否存在
        MultipartFile image = bookRequest.getImage();
        if (image == null || image.isEmpty()) {
            logger.error("圖片檔案未提供");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }

        try {
            // 上傳圖片到 Cloudinary
            String imageUrl = cloudinaryService.uploadImage(image);
            logger.info("圖片上傳成功，URL: {}", imageUrl);

            // 將圖片 URL 帶入到 bookRequest
            bookRequest.setImageUrl(imageUrl);

            // 新增書籍
            Integer bookId = BookService.addBook(bookRequest);

            // 返回新增書籍資訊
            Book bookInfo = BookService.getBookInfoById(bookId);
            return ResponseEntity.status(HttpStatus.CREATED).body(bookInfo);

        } catch (IOException e) {
            logger.error("圖片上傳失敗", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        } catch (Exception e) {
            logger.error("處理請求時發生錯誤", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    
    // 更新書籍資訊 回傳更新後的書籍資訊
    @PutMapping(value = "/book/update/{bookId}", consumes = "multipart/form-data")
    public ResponseEntity<Book> updateBook(@PathVariable Integer bookId, @ModelAttribute @Valid BookRequest bookRequest) {

        logger.info("更新書籍ID: {}", bookId);

        // 若查無此書籍，回傳 404 Not Found
        Book bookInfo = BookService.getBookInfoById(bookId);
        if (bookInfo == null) {
            logger.warn("書籍ID {} 查詢失敗", bookId);

            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        // 若查詢成功，則更新書籍資訊
        try {
            MultipartFile image = bookRequest.getImage();
            if (image != null && !image.isEmpty()) {
                String imageUrl = cloudinaryService.uploadImage(image);
                // 若上傳新圖片，則更新圖片
                bookRequest.setImageUrl(imageUrl);
                logger.info("圖片上傳成功，URL: {}", imageUrl);
            }
            else {
                // 若未上傳新圖片，則保留原圖片
                bookRequest.setImageUrl(bookInfo.getImageUrl());
            }
        

            // 更新書籍資訊
            BookService.updateBook(bookId, bookRequest);

            // 取得更新後的書籍資訊
            bookInfo = BookService.getBookInfoById(bookId);
            logger.info("書籍ID {} 更新成功", bookId);
                
            return ResponseEntity.status(HttpStatus.OK).body(bookInfo);
        }

        // 若更新失敗，回傳 500 Internal Server Error
        catch (IOException e) {
            logger.error("圖片上傳失敗", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
        catch (Exception e) {
            logger.error("處理請求時發生錯誤", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // 刪除書籍資訊
    @DeleteMapping("/book/delete/{bookId}")
    public ResponseEntity<Book> deleteBook(@PathVariable Integer bookId) {
        Book bookInfo = BookService.getBookInfoById(bookId);
    
        logger.info("刪除書籍ID: {}", bookId);
        try {
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

        } catch (Exception e) {
            logger.error("處理請求時發生錯誤", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // 查詢所有書籍
    @GetMapping("/book")
    public ResponseEntity<Page<Book>> getBookList(
        // 查詢條件
        @RequestParam(required = false) BookCategory category,
        @RequestParam(required = false) String search,
        // 分頁條件
        @RequestParam(defaultValue = "created_date") String orderBy,
        @RequestParam(defaultValue = "desc") String sort,
        // 分頁條件
        @RequestParam(defaultValue = "10") @Max(100) @Min(0) Integer limit, // 每頁顯示筆數 (最多 100 筆)
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
