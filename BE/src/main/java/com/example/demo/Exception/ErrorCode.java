package com.example.demo.Exception;

import lombok.Data;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

@Getter
public enum ErrorCode {

    UNCATEGORIZED_EXCEPTION(9999,"Lỗi Hệ Thống", HttpStatus.INTERNAL_SERVER_ERROR), //day la loai exception khong ngo toi

    //---------Exception của đăng kí người dùng-----------------------
    GMAIL_EXISTED(1001,"Gmail Đã Tồn Tại",HttpStatus.BAD_REQUEST),
    INVALID_NAME_FORMAT(1002,"Sai Format Của Tên ",HttpStatus.BAD_REQUEST),
    NOT_MATCH_PASS(1003,"Mật Khẩu Không Khớp,Vui Lòng Nhập Lại",HttpStatus.BAD_REQUEST),
    USERNAME_INVALID(1008,"Tên Đăng Nhập Phải Có ít nhất 5 kí tự",HttpStatus.BAD_REQUEST),
    PASS_INVALID(1009,"Mật Khẩu Phải có ít Nhất 4 kí tự",HttpStatus.BAD_REQUEST), //bỏ vào userdto thay cho lúc trước
    INVALID_OTP(1010,"Sai OTP, vui lòng nhập lại",HttpStatus.BAD_REQUEST),
    WRONG_FORMAT_EMAIL(1012,"Sai Format Email",HttpStatus.BAD_REQUEST),
    INVALID_FORMAT_ADDRESS(1016,"Sai Format của địa chỉ",HttpStatus.BAD_REQUEST),
    INVALID_FORMAT_PHONE_NUMBER(1017,"Sai Format số điện thoại",HttpStatus.BAD_REQUEST),
    INVALID_FORMAT_NAME(1018,"Sai Format của Đặt Tên",HttpStatus.BAD_REQUEST),
    USERNAME_EXISTED(1019,"Username Đã Tồn Tại",HttpStatus.BAD_REQUEST),
    //-----------------------------------

    //------------------Exception của Xác thực , Phân quyền )----------
    UNAUTHENTICATED(1004,"Please Login ,Token Expired or Unauthorized",HttpStatus.UNAUTHORIZED), //lỗi chưa xác thực , họặc token hết hạn thì đều bắt người dùng phải login để tạo lại token
    UNAUTHORIZED(1005,"Bạn không có quyền truy cập thao tác này ",HttpStatus.FORBIDDEN),
    TOKEN_EXPIRED(1011,"Token hết hạn , vui lòng đăng nhập lại",HttpStatus.UNAUTHORIZED),
    //-----------------------------------

    //--------------Exception của Login----------------

    WRONG_PASS(1006,"Sai mật khẩu",HttpStatus.BAD_REQUEST),
    WRONG_USER_NAME(1028,"Sai Tên đăng nhập",HttpStatus.BAD_REQUEST),
    //404 la NOTFOUND , vi du nhu tim user nhung ko ton tai
    //-----------------------------------

    INVALID_KEY(1007,"Invalid Message Key",HttpStatus.BAD_REQUEST), //dinh nghĩa 1 số cái nhập sai ở dto, ví dụ như PAS_INVALID mà thiếu chữ S


    //-------------Exception SendMail, Verify OTP-------------
    OTP_EXPIRED(1013,"OTP đã hết hạn , OTP mới đã được gửi, vui lòng check mail",HttpStatus.EXPECTATION_FAILED),
    UN_ACTIVE_ACCOUNT(1014,"Account đã bị ban hoặc chưa Active",HttpStatus.EXPECTATION_FAILED),
    NOT_FOUND_EMAIL(1020,"Không tìm thấy Email",HttpStatus.BAD_REQUEST),
    TOO_MANY_ATTEMPTS(1027, "Nhập sai OTP quá 3 lần , vui lòng check lại OTP mới trong mail",HttpStatus.BAD_REQUEST),
    //-----------------------------------

    NOT_FOUND(1015,"Không tìm thấy kết quả tìm kiếm ",HttpStatus.NOT_FOUND),


    //Exception của Materials
    NAME_EXIST(1021,"Tên đã tồn tại , vui lòng nhập tên khác",HttpStatus.BAD_REQUEST),
    MUST_REQUIRED(1022,"Không được bỏ trống trường thông tin ",HttpStatus.BAD_REQUEST),
    PRICE_INVALID(1023,"Giá tiền phải lớn hơn 0 , nhập sai định dạng ",HttpStatus.BAD_REQUEST),
    QUANTITY_INVALID(1024,"Số lượng phải là số nguyên và lớn hơn 0 , nhập sai định dạng ",HttpStatus.BAD_REQUEST),
    PHONE_INVALID(1025," nhập sai định dạng số điện thoại ",HttpStatus.BAD_REQUEST),




    FILE_EXCEL_INVALID(1025,"Sai định dạng file",HttpStatus.BAD_REQUEST),
    IMAGE_INVALID(1026, "Lỗi ảnh truyền vào , hãy thử lại",HttpStatus.BAD_REQUEST),





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
