package com.example.demo.Dto.RequestDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@NoArgsConstructor

public class RequestEditCusDTO {
    private String description;
    private List<String> files;


//    public RequestEditCusDTO(String description, List<String> files) {
//        this.description = description;
//        this.files = files;
//    }
}
