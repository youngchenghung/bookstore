package com.example.bookstore.cloudinary;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CloudinaryConfig {

    // 設定 Cloudinary 的帳號、金鑰、密鑰
    @Bean
    public Cloudinary cloudinary() {
        return new Cloudinary(ObjectUtils.asMap(
            "cloud_name", "dspx6kwrf",
            "api_key", "481672482219893",
            "api_secret", "-MCmuB_ahxru_JM1DVA7AH_HUpE"));
    }
}