package com.example.demo.Dto.OrderDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data

@NoArgsConstructor
public class SendMailOrderDTO {
    private String email;

    public SendMailOrderDTO(String email) {
        this.email = email;
    }
}
