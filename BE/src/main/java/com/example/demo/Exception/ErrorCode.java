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
    PHONE_EXISTED(1031," Số điện thoại này đã được sử dụng",HttpStatus.BAD_REQUEST),
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

    NOT_FOUND(1015,"Không tìm thấy kết quả tìm kiếm ",HttpStatus.OK),


    //Exception của Materials
    NAME_EXIST(1021,"Tên đã tồn tại , vui lòng nhập tên khác",HttpStatus.BAD_REQUEST),
    MUST_REQUIRED(1022,"Không được bỏ trống trường thông tin ",HttpStatus.BAD_REQUEST),
    PRICE_INVALID(1023,"Giá tiền phải lớn hơn 0 , nhập sai định dạng ",HttpStatus.BAD_REQUEST),
    PHONE_INVALID(1025," nhập sai định dạng số điện thoại ",HttpStatus.BAD_REQUEST),





    FILE_EXCEL_INVALID(1025,"Sai định dạng file",HttpStatus.BAD_REQUEST),
    IMAGE_INVALID(1026, "Lỗi ảnh truyền vào , hãy thử lại",HttpStatus.BAD_REQUEST),


    //Exception của Order
    OUT_OF_STOCK(1029,"Sản phẩm đã hết hàng",HttpStatus.BAD_REQUEST),

    PRODUCT_HAS_RELATIONSHIPS(1030,"Không thể xoá sản phẩm do sản phẩm đang tồn tại trong các đơn hàng và các công việc đang thi công",HttpStatus.BAD_REQUEST),
    SUPPLIER_HAS_RELATIONSHIPS(1032,"Không thể xoá nhà cung cấp này  do vật liệu từ họ đang được sử dụng cho đơn hàng",HttpStatus.BAD_REQUEST),
    NOT_EDIT_EMPLOYEE(1033,"Không thể thay đổi quyền của nhân viên này vì họ đang đảm nhận công việc ở vị trí của họ",HttpStatus.BAD_REQUEST),
    EXISTED_WISHLIST(1034,"Sản phẩm đã tồn tại trong danh sách yêu thích, không cần thêm vào nữa  ",HttpStatus.BAD_REQUEST),
    INVALID_FULL_NAME(1035,"Tên không được có chữ số",HttpStatus.BAD_REQUEST),
    INVALID_QUANTITY_PRODUCT_ERROR(1036,"Không được nhập quá số lượng sản phẩm làm theo công việc ",HttpStatus.BAD_REQUEST),
    QUANTITY_INVALID(1024,"Số lượng phải là số nguyên và lớn hơn 0 , nhập sai định dạng ",HttpStatus.BAD_REQUEST),
    EMPLOYEE_MATERIAL_EXISTED(1038,"Sản phẩm đã được giao nguyên vật liệu cho nhân viên, không thể chỉnh sửa",HttpStatus.BAD_REQUEST),
    QUANTITY_JOB_EXCEPTION(1040,"Số lượng sản phẩm được phân vượt qúa số lượng yêu cầu",HttpStatus.BAD_REQUEST),
    COST_JOB_EXCEPTION(1041,"Số tiền công vượt quá số tiền lợi nhuận , vui lòng nhập lại ",HttpStatus.BAD_REQUEST),
    COST_DISCOUNT_ORDER_EXCEPTION(1042,"Số tiền giảm giá vượt quá số tiền lợi nhuận , vui lòng nhập lại",HttpStatus.BAD_REQUEST),
    COST_DEPOSIT(1043,"Tiền đặt cọc không đúng với giá trị cần trả , vui lòng nhập lại!",HttpStatus.BAD_REQUEST),
    TIME_FINISH_INVALID(1044,"Thời gian hoàn thành công việc vượt quá thời gian dự kiến hoàn thành đã thống nhất trong đơn hàng, vui lòng nhập lại!",HttpStatus.BAD_REQUEST),
    TIME_START_INVALID(1045,"Thời gian bắt đầu công việc đang có trước thời gian mà đơn hàng được tạo, vui lòng nhập lại!",HttpStatus.BAD_REQUEST),
    TIME_START_JOB_INVALID(1046,"Thời gian bắt đầu giai đoạn công việc hiện tại đang có trước thời gian hoàn thành giai đoạn trước đó của cùng công việc này , vui lòng nhập lại!",HttpStatus.BAD_REQUEST),
    CATEGORY_HAS_RELATIONSHIPS(1047,"Không thể xoá loại sản phẩm do các sản phẩm liên quan đến loại này đang tồn tại trong các đơn hàng",HttpStatus.BAD_REQUEST),




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
