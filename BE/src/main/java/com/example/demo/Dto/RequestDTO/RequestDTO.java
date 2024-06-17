package com.example.demo.Dto.RequestDTO;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.repository.Modifying;

import java.sql.Timestamp;
import java.util.Date;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RequestDTO {
    @NotNull(message = "MUST_REQUIRED")
    private Integer user_id;
    @NotNull(message = "MUST_REQUIRED")
    private Integer status_id;
    @NotNull(message = "MUST_REQUIRED")
    private Date requestDate;
    @NotNull(message = "MUST_REQUIRED")
    private String response;
    @NotNull(message = "MUST_REQUIRED")
    private String description;
    private String phoneNumber;
    private String fullname;
    private String address;
}
