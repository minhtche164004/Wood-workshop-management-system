package com.example.demo.Controllers.Salary;

import com.example.demo.Repository.MaterialRepository;
import com.example.demo.Response.ApiResponse;
import com.example.demo.Service.JobService;
import com.example.demo.Service.MaterialService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.List;

@RestController
@RequestMapping("/api/auth/salary/")
@AllArgsConstructor
public class SalaryController {

    @Autowired
    private JobService jobService;
    @Autowired
    private MaterialRepository materialRepository;
    @GetMapping("/getAllSalary")
    public ApiResponse<?> getAllSalary(){
        ApiResponse<List> apiResponse = new ApiResponse<>();
        apiResponse.setResult(jobService.getAllAdvancesalary());
        return apiResponse;
    }

    @GetMapping("/getSalaryByEmployeeID")
    public ApiResponse<?> getSalaryByEmployeeID(){
        ApiResponse<List> apiResponse = new ApiResponse<>();
        apiResponse.setResult(jobService.getAdvancesalaryByEmployeeId());
        return apiResponse;
    }

    @GetMapping("/getMultiFillterSalary")
    public ApiResponse<?> getAllRequestProductForAdmin(
            @RequestParam(required = false) String employeeName,
            @RequestParam(required = false) Date fromDate,
            @RequestParam(required = false) Date toDate,
            @RequestParam(required = false) String sortDirection
    ){
        ApiResponse<List> apiResponse = new ApiResponse<>();
        apiResponse.setResult(jobService.multi_filter_salary(fromDate, toDate, employeeName,sortDirection));
        return apiResponse;
    }
}
