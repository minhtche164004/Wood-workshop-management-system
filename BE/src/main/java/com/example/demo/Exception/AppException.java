package com.example.demo.Exception;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data

//taoj AppException riêng để trả về trong service( vis dụ trong userservice)
public class AppException extends  RuntimeException{
    public AppException(ErrorCode errorCode) {
        super(errorCode.getMessage()); //ke thua constructer cua RuntimeException
        this.errorCode = errorCode;
    }

    private ErrorCode errorCode;
}
