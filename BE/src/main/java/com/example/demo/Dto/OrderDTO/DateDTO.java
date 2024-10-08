package com.example.demo.Dto.OrderDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class DateDTO {
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date startDate;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date endDate;

//    //@DateTimeFormat(pattern = "yyyy-MM-dd")
//    private LocalDateTime startDate1;
//   // @DateTimeFormat(pattern = "yyyy-MM-dd")
//    private LocalDateTime endDate1;

}
