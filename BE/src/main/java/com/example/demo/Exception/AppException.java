package com.example.demo.Exception;

import lombok.*;

@Getter
@Setter
@Data
@NoArgsConstructor

//taoj AppException riêng để trả về trong service( vis dụ trong userservice)
public class AppException extends  RuntimeException{
    public AppException(ErrorCode errorCode) {
        super(errorCode.getMessage()); //ke thua constructer cua RuntimeException
        this.errorCode = errorCode;
    }

    private ErrorCode errorCode;
}
