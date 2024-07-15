package com.example.demo.Dto.JobDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;


@NoArgsConstructor
@Data
public class Employee_MaterialDTO {
    private int empMaterialId;
    private Integer userId;
    private String fullname;
    private String  position_name;
    private int  subMaterialId;
    private String subMaterialName;
    private double quantity;
    private Date timeStart; //ngày nhận
    private String code; //mã job


    public Employee_MaterialDTO(int empMaterialId,Integer userId, String fullname, String position_name, int subMaterialId, String subMaterialName, double quantity, Date timeStart, String code) {
        this.empMaterialId = empMaterialId;
        this.userId=userId;
        this.fullname=fullname;
        this.position_name = position_name;
        this.subMaterialId = subMaterialId;
        this.subMaterialName = subMaterialName;
        this.quantity = quantity;
        this.timeStart = timeStart;
        this.code = code;
    }
}
