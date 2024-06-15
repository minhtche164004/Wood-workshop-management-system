package com.example.demo.Controllers.Authentication;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class SendMailRequest {
    private int amount;
    private String orderInfo;
    private String to;
    private String[] cc;
    private String subject;
    private String body;

}