package com.example.demo.Exception;

import com.example.demo.Response.ApiResponse;
import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    //define các exception , value này là cái class muốn bắt
    // ngoai nhung cai exception da bat ow duoi thi neu cos exception khac ngoai nhung loaij o duoi thi dinh nghiax nhu nayf
    @ExceptionHandler(value = Exception.class)
    ResponseEntity<ApiResponse> handlingRuntimeException(RuntimeException exception){
        ApiResponse apiResponse= new ApiResponse();
        apiResponse.setCode(ErrorCode.UNCATEGORIZED_EXCEPTION.getCode());
        apiResponse.setMessage(ErrorCode.UNCATEGORIZED_EXCEPTION.getMessage());
        return  ResponseEntity.badRequest().body(apiResponse);
    }

    //dinh nghia loai Exception , o day la loai AppException
    @ExceptionHandler(value = AppException.class)
    ResponseEntity<ApiResponse> handlingAppException(AppException exception){
        ErrorCode errorCode= exception.getErrorCode();
        ApiResponse apiResponse= new ApiResponse();

        apiResponse.setCode(errorCode.getCode());
        apiResponse.setMessage(errorCode.getMessage());

        return  ResponseEntity
                .status(errorCode.getStatusCode())
                .body(apiResponse);

    }
    @ExceptionHandler(value = UsernameNotFoundException.class)
    ResponseEntity<ApiResponse> handlinUsernameNotFoundException(UsernameNotFoundException exception){
        ErrorCode errorCode= ErrorCode.NOT_FOUND;
        ApiResponse apiResponse= new ApiResponse();
        apiResponse.setCode(errorCode.getCode());
        apiResponse.setMessage(errorCode.getMessage());

        return  ResponseEntity
                .status(errorCode.getStatusCode())
                .body(apiResponse);
    }
    //bat Exception AccessDeniedException
    @ExceptionHandler(value = AccessDeniedException.class)
    ResponseEntity<ApiResponse> handlingAccessDeniedException(AccessDeniedException exception){
        ErrorCode errorCode= ErrorCode.UNAUTHORIZED;
        ApiResponse apiResponse= new ApiResponse();
        apiResponse.setCode(errorCode.getCode());
        apiResponse.setMessage(errorCode.getMessage());
        return  ResponseEntity
                .status(errorCode.getStatusCode())
                .body(apiResponse);

    }
    //cái lỗi valid trong userdto là lỗi MethodArgumentNotValidException, nó đc định nghiax ở đây
    //ow day la tra ve cac response theo cau truc da dinh nghia trong dto
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    ResponseEntity<ApiResponse> handlingValidation(MethodArgumentNotValidException exception){
        String enumKey=exception.getFieldError().getDefaultMessage();
        ErrorCode errorCode=ErrorCode.INVALID_KEY;//nhap sai key
        try{
            errorCode= ErrorCode.valueOf(enumKey);
        }catch(IllegalArgumentException e){

        }
        ApiResponse apiResponse= new ApiResponse();
        apiResponse.setCode(errorCode.getCode());
        apiResponse.setMessage(errorCode.getMessage());
        return  ResponseEntity.badRequest().body(apiResponse);
    }

    //bat Exception AccessDeniedException
    @ExceptionHandler(value = IllegalArgumentException.class)
    ResponseEntity<ApiResponse> handlingIllegalArgumentException(IllegalArgumentException exception){
        ErrorCode errorCode= ErrorCode.WRONG_PASS_OR_EMAIL;
        ApiResponse apiResponse= new ApiResponse();
        apiResponse.setCode(errorCode.getCode());
        apiResponse.setMessage(errorCode.getMessage());

        return  ResponseEntity
                .status(errorCode.getStatusCode())
                .body(apiResponse);

    }

//    //bat Exception AccessDeniedException
//    @ExceptionHandler(value = ExpiredJwtException.class)
//    ResponseEntity<ApiResponse> handlingExpiredJwtException(ExpiredJwtException exception){
//        ErrorCode errorCode= ErrorCode.TOKEN_EXPIRED;
//        ApiResponse apiResponse= new ApiResponse();
//        apiResponse.setCode(errorCode.getCode());
//        apiResponse.setMessage(errorCode.getMessage());
//
//        return  ResponseEntity
//                .status(errorCode.getStatusCode())
//                .body(apiResponse);
//
//    }

}


