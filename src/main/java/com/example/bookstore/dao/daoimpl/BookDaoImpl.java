package com.example.bookstore.dao.daoimpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Component;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.support.KeyHolder;


import com.example.bookstore.model.Book;
import com.example.bookstore.rowmapper.BookRowMapper;
import com.mysql.cj.x.protobuf.MysqlxCrud.Limit;
import com.example.bookstore.dao.BookDao;
import com.example.bookstore.dto.BookQueryParams;
import com.example.bookstore.dto.BookRequest;

import java.util.List;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


@Component
public class BookDaoImpl implements BookDao {

    @Autowired 
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    // 查詢書籍資訊 by bookId 
    @Override
    public Book getBookInfoById(Integer bookId) {
        String sql = "SELECT book_id, category, book_name, author, price, stock, created_date, last_modified_date FROM book WHERE book_id = :bookId";

        Map<String, Object> map = new HashMap<>();
        map.put("bookId", bookId);

        // bookInfo 資料型態為 List<Book>，取出第一筆資料
        List<Book> bookInfo = namedParameterJdbcTemplate.query(sql, map, new BookRowMapper());
        if (bookInfo.isEmpty()) {
            return null;
        }
        return bookInfo.get(0);
    }

    // 新增書籍 回傳 bookId
    @Override
    public Integer addBook(BookRequest bookRequest) {
        String sql = "INSERT INTO book (book_name, category, author, price, stock, created_date, last_modified_date) VALUES (:bookName, :category, :author, :price, :stock, :createdDate, :lastModifiedDate)";

        Map<String, Object> map = new HashMap<>();
        map.put("bookName", bookRequest.getBookName());
        map.put("author", bookRequest.getAuthor());
        map.put("category", bookRequest.getBookCategory().name());
        map.put("price", bookRequest.getPrice());
        map.put("stock", bookRequest.getStock());

        Date now = new Date();
        map.put("createdDate", now);
        map.put("lastModifiedDate", now);

        KeyHolder keyholder = new GeneratedKeyHolder();

        namedParameterJdbcTemplate.update(sql, new MapSqlParameterSource(map), keyholder);

        int bookId = keyholder.getKey().intValue();

        return bookId;
    }

    // 更新書籍資訊
    @Override
    public void updateBook(Integer bookId, BookRequest bookRequest) {
        String sql = "UPDATE book SET book_name = :bookName, category = :category, author = :author, price = :price, stock = :stock, lastModifiedDate = :lastModifiedDate WHERE book_id = :bookId";

        Map<String, Object> map = new HashMap<>();
        map.put("bookId", bookId);
        map.put("bookName", bookRequest.getBookName());
        map.put("category", bookRequest.getBookCategory().name());
        map.put("author", bookRequest.getAuthor());
        map.put("price", bookRequest.getPrice());
        map.put("stock", bookRequest.getStock());
        
        Date now = new Date();
        map.put("lastModifiedDate", now);

        namedParameterJdbcTemplate.update(sql, map);
    }

    // 更新書籍庫存
    @Override
    public void updateStock(Integer bookId, Integer stock) {
        String sql = "UPDATE book SET stock = :stock WHERE book_id = :book_id";

        Map<String, Object> map = new HashMap<>();
        map.put("book_id", bookId);
        map.put("stock", stock);

        namedParameterJdbcTemplate.update(sql, map);
    }

    // 刪除書籍資訊
    @Override
    public void deleteBook(Integer bookId) {
        String sql = "DELETE FROM book WHERE book_id = :bookId";

        Map<String, Object> map = new HashMap<>();
        map.put("bookId", bookId);

        namedParameterJdbcTemplate.update(sql, map);
    }

    // 查詢書籍列表
    @Override
    public List<Book> getBooks(BookQueryParams bookQueryParams) {
        String sql = "SELECT book_id, book_name, category, author, price, stock, created_date, last_modified_date FROM book WHERE 1=1";

        Map<String, Object> map = new HashMap<>();

        // 查詢條件 - 類別 (category)
        if (bookQueryParams.getBookCategory() != null) {
            sql = sql + " AND category = :category";
            map.put("category", bookQueryParams.getBookCategory().name());
            System.out.println("Book Category: " + bookQueryParams.getBookCategory());
        }

        // 搜尋條件 - 書名 (模糊查詢)
        if (bookQueryParams.getSearch() != null) {
            sql = sql + " AND book_name LIKE :search";
            map.put("search", "%" + bookQueryParams.getSearch() + "%");
        }

        // 排序條件 - 排序欄位 & 排序方式 (asc / desc)
        sql = sql + " ORDER BY " + bookQueryParams.getOrderBy() + " " + bookQueryParams.getSort();
        sql = sql + " LIMIT :limit OFFSET :offset";

        map.put("limit", bookQueryParams.getLimit());
        map.put("offset", bookQueryParams.getOffset());

        // book 資料型態為 List<Book>
        List<Book> books = namedParameterJdbcTemplate.query(sql, map, new BookRowMapper());

        return books;
    }

    // 查詢書籍總筆數
    @Override
    public Integer countBooks(BookQueryParams bookQueryParams) {
        String sql = "SELECT COUNT(*) FROM book WHERE 1=1";

        Map<String, Object> map = new HashMap<>();

        // 查詢條件 - 類別 (category)
        if (bookQueryParams.getBookCategory() != null) {
            sql = sql + " AND category = :category";
            map.put("category", bookQueryParams.getBookCategory().name());
            System.out.println("Book Category: " + bookQueryParams.getBookCategory());
        }

        // 搜尋條件 - 書名 (模糊查詢)
        if (bookQueryParams.getSearch() != null) {
            sql = sql + " AND book_name LIKE :search";
            map.put("search", "%" + bookQueryParams.getSearch() + "%");
        }

        // 取得總筆數
        Integer total = namedParameterJdbcTemplate.queryForObject(sql, map, Integer.class);

        return total;
    }
}
