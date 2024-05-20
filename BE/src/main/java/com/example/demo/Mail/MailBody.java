package com.example.demo.Mail;

import lombok.Builder;

@Builder
public record MailBody(String to, String subject,String text) {

}
