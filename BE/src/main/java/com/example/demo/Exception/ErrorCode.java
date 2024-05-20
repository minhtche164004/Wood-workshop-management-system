package com.example.demo.Exception;

import lombok.Data;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

@Getter
public enum ErrorCode {
    UNCATEGORIZED_EXCEPTION(9999,"UNCATEGORIZED ERROR", HttpStatus.INTERNAL_SERVER_ERROR), //day la loai exception khong ngo toi
    USER_EXISTED(1001,"User Existed",HttpStatus.BAD_REQUEST),
    INVALID_NAME_FORMAT(1002,"Wrong Name Format",HttpStatus.BAD_REQUEST),
    NOT_MATCH_PASS(1003,"Pass not match,Please enter again",HttpStatus.BAD_REQUEST),
    UNAUTHENTICATED(1004,"Unauthenticated , Please Login Again",HttpStatus.UNAUTHORIZED), //lỗi chưa xác thực , họặc token hết hạn thì đều bắt người dùng phải login để tạo lại token
    UNAUTHORIZED(1005,"You do not have permission",HttpStatus.FORBIDDEN),
    WRONG_PASS_OR_EMAIL(1006,"Invalid password or gmail",HttpStatus.BAD_REQUEST),
    //404 la NOTFOUND , vi du nhu tim user nhung ko ton tai


    INVALID_KEY(1007,"Invalid Message Key",HttpStatus.BAD_REQUEST), //dinh nghĩa 1 số cái nhập sai ở dto, ví dụ như PAS_INVALID mà thiếu chữ S
    USERNAME_INVALID(1008,"Username must be at least 5 character",HttpStatus.BAD_REQUEST),
    PASS_INVALID(1009,"Password must be at least 4 character",HttpStatus.BAD_REQUEST), //bỏ vào userdto thay cho lúc trước
    INVALID_OTP(1010,"Invalid OTP,Please Enter again",HttpStatus.BAD_REQUEST),
    TOKEN_EXPIRED(1011,"Token expired,Please Login again",HttpStatus.UNAUTHORIZED),
    WRONG_FORMAT_EMAIL(1012,"Wrong Format Email",HttpStatus.BAD_REQUEST),
    OTP_EXPIRED(1013,"OTP has expired! A new OTP has been sent to your email",HttpStatus.EXPECTATION_FAILED),
    UN_ACTIVE_ACCOUNT(1014,"Account Not Active or Block",HttpStatus.EXPECTATION_FAILED),
   // OTP_EXPIRED(1013,"OTP has expired! A new OTP has been sent to your email",HttpStatus.EXPECTATION_FAILED),
    ;

    ErrorCode(int code, String message, HttpStatusCode statusCode) {
        this.code = code;
        this.message = message;
        this.statusCode=statusCode;
    }

    private int code;
    private String message;
    private HttpStatusCode statusCode;

}
