package com.example.bookstore.rowmapper;

import com.example.bookstore.constant.BookCategory;
import com.example.bookstore.model.Book;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class BookRowMapper implements RowMapper<Book> {

    @Override
    public Book mapRow(ResultSet resultSet, int i) throws SQLException {

        Book book = new Book();

        book.setBookId(resultSet.getInt("book_id"));
        book.setBookName(resultSet.getString("book_name"));

        // 透過 BookCategory.valueOf() 方法將資料庫中的字串轉換為 BookCategory 列舉型別
        String categoryStr = resultSet.getString("category");
        BookCategory category = BookCategory.valueOf(categoryStr);
        book.setBookCategory(category);

        book.setAuthor(resultSet.getString("author"));
        book.setPrice(resultSet.getInt("price"));
        book.setStock(resultSet.getInt("stock"));
        book.setCreatedDate(resultSet.getTimestamp("created_date"));
        book.setLastModifiedDate(resultSet.getTimestamp("last_modified_date"));

        return book;
    }

}
