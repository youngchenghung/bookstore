package com.example.bookstore.dto;

import org.springframework.web.multipart.MultipartFile;

import com.example.bookstore.constant.BookCategory;

import jakarta.validation.constraints.NotNull;

public class BookRequest {

    @NotNull
    private String bookName;

    @NotNull
    private String author;

    @NotNull
    private  BookCategory bookCategory;

    @NotNull
    private Integer price;

    @NotNull
    private Integer stock;

    private MultipartFile image;

    private String imageUrl;

    public String getBookName() {
        return bookName;
    }
    public void setBookName(String bookName) {
        this.bookName = bookName;
    }
    public String getAuthor() {
        return author;
    }
    public void setAuthor(String author) {
        this.author = author;
    }
    public BookCategory getBookCategory() {
        return bookCategory;
    }
    public void setBookCategory(BookCategory bookCategory) {
        this.bookCategory = bookCategory;
    }
    public Integer getPrice() {
        return price;
    }
    public void setPrice(Integer price) {
        this.price = price;
    }
    public Integer getStock() {
        return stock;
    }
    public void setStock(Integer stock) {
        this.stock = stock;
    }
    public MultipartFile getImage() {
        return image;
    }
    public void setImage(MultipartFile image) {
        this.image = image;
    }
    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
    public String getImageUrl() {
        return imageUrl;
    }

}
