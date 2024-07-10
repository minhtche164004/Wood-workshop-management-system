package com.example.demo.Controllers.Salary;

import com.example.demo.Entity.Advancesalary;
import com.example.demo.Repository.MaterialRepository;
import com.example.demo.Response.ApiResponse;
import com.example.demo.Service.JobService;
import com.example.demo.Service.MaterialService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
    @PutMapping("/updatebanking")
    public ApiResponse<?> updatebanking(@RequestParam("id") int id, @RequestParam("is_advance_success") boolean is_advance_success){
        ApiResponse<Advancesalary> apiResponse = new ApiResponse<>();
        apiResponse.setResult(jobService.ChangeStatus(id,is_advance_success));
        return apiResponse;
    }

    @GetMapping("/getMultiFillterSalary")
    public ApiResponse<?> getMultiFillterSalary(
            @RequestParam(required = false) String username,
            @RequestParam(required = false) Date fromDate,
            @RequestParam(required = false) Date toDate,
            @RequestParam(required = false) Integer position_id,
            @RequestParam(required = false) String sortDirection
    ){
        ApiResponse<List> apiResponse = new ApiResponse<>();
        apiResponse.setResult(jobService.multi_filter_salary(fromDate, toDate,position_id, username,sortDirection));
        return apiResponse;
    }
}
