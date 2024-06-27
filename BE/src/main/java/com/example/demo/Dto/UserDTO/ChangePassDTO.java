package com.example.demo.Dto.UserDTO;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class ChangePassDTO {
    private String old_pass;
    private String new_pass;
    private String check_pass;
}
