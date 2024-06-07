//package com.example.demo.Config;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.web.multipart.MultipartResolver;
//
//@Configuration
//public class MultipartConfig {
//    @Bean
//    public MultipartResolver multipartResolver() {
//        CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver();
//        multipartResolver.setMaxUploadSize(10000000); // Giới hạn kích thước tối đa của tệp tải lên (10MB)
//        return multipartResolver;
//    }
//}