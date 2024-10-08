package com.example.demo.Controllers.Salary;

import com.example.demo.Dto.OrderDTO.DateDTO;
import com.example.demo.Dto.OrderDTO.OderDTO;
import com.example.demo.Entity.Advancesalary;
import com.example.demo.Repository.MaterialRepository;
import com.example.demo.Response.ApiResponse;
import com.example.demo.Service.JobService;
import com.example.demo.Service.MaterialService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Date;
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

    @PostMapping ("/MultiFilterSalary")
    public ApiResponse<?> MultiFilterSalary(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) Integer isAdvanceSuccess,
            @RequestBody(required = false) DateDTO dto
    ) {
        ApiResponse<List<Advancesalary>> apiResponse = new ApiResponse<>(); // Chỉ định rõ kiểu List<OderDTO>
        String searchTerm = search == null ? "" : search.trim();
        apiResponse.setResult(jobService.MultiFilterOrderSpecialOrder(searchTerm,isAdvanceSuccess, dto.getStartDate(), dto.getEndDate()));
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

    @PostMapping("/getMultiFillterSalary")
    public ApiResponse<?> getMultiFillterSalary(
            @RequestParam(required = false) String username,
            @RequestBody(required = false) DateDTO dto,
            @RequestParam(required = false) Integer isAdvanceSuccess,
            @RequestParam(required = false) Integer position_id,
            @RequestParam(required = false) String sortDirection
    ){
        ApiResponse<List> apiResponse = new ApiResponse<>();
        String searchTerm = username == null ? "" : username.trim();

        apiResponse.setResult(jobService.multi_filter_salary(dto.getStartDate(), dto.getEndDate(), isAdvanceSuccess,position_id, searchTerm,sortDirection));
        return apiResponse;
    }
}
