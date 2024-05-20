package com.example.demo.Entity;

//lớp record nghĩa là giá trị của chúng không thể được thay đổi sau khi tạo ra.
// Điều này giúp tránh các vấn đề liên quan đến tính đồng bộ và an toàn trong ứng dụng.
public record ChangePassword(String password,String repeatPassword) {
}
