package com.example.urbannest.config;


import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CloudinaryConfig {

    @Bean
    public Cloudinary cloudinary() {
        return new Cloudinary(
                ObjectUtils.asMap(
                        "cloud_name", "dduduxhdt",
                        "api_key", "475139556537118",
                        "api_secret", "lUMTxI9zgO0WBal83VT0wHA5t54"
                )
        );
    }
}
