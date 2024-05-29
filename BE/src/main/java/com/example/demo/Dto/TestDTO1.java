package com.example.demo.Dto;

import lombok.*;

import java.io.Serializable;

@Data
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Setter
public class TestDTO1 implements Serializable {
    private String username;
    private String email;
    private String role_name;

    //public TestDTO1() {}
}
