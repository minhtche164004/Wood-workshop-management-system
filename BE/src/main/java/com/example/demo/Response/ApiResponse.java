package com.example.demo.Response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)//khai bao de biet rang khi seriablez sang json , neu fill nao la null thi khong kem vao json(ko hien thi)
@Data
@Getter
@Setter
//chuẩn hoá 1 api trả về, tất ca các api nào mà trả về response thì cần đc chuẩn hoá chung 1 kiểu, 1 format
//kieu <T> laf kieu tra ve co the thay doi tuy thuoc vao tung api
public class ApiResponse <T> {
    private int code=1000; //tự quy dinh la neu tra ve code la 1000 thi la api thanh cong
    private String message;
    private T result;
    private Map<String, String> errors;

    public void setError(int code, Map<String, String> errors) {
        this.code = code;
        this.errors = errors;
    }

}
