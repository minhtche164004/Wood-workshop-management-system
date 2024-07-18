package com.example.demo.Controllers.Job;

import com.example.demo.Response.ApiResponse;
import com.example.demo.Service.StatisticService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RestController
@RequestMapping("/api/auth/statistic/")
@AllArgsConstructor
public class StatisticController {
    @Autowired
    private StatisticService statisticService;
    @GetMapping("/findTotalSalaryByMonthAndYear")
    public ApiResponse<?> getListProductRequestForJob(@RequestParam("month") int month,@RequestParam("year") int year) {
        ApiResponse<BigDecimal> apiResponse = new ApiResponse<>();
        apiResponse.setResult(statisticService.findTotalSalaryByMonthAndYear(month, year));
        return apiResponse;
    }
    @GetMapping("/countCompletedJobsByMonthAndYear")
    public ApiResponse<?> countCompletedJobsByMonthAndYear(@RequestParam("status_id") int status_id,@RequestParam("month") int month,@RequestParam("year") int year) {
        ApiResponse<Long> apiResponse = new ApiResponse<>();
        apiResponse.setResult(statisticService.countCompletedJobsByMonthAndYear(status_id,month, year));
        return apiResponse;
    }
    @GetMapping("/countCompletedJobsForProductByMonthAndYear")
    public ApiResponse<?> countCompletedJobsForProductByMonthAndYear(@RequestParam("status_id") int status_id,@RequestParam("month") int month,@RequestParam("year") int year) {
        ApiResponse<Integer> apiResponse = new ApiResponse<>();
        apiResponse.setResult(statisticService.countCompletedJobsForProductByMonthAndYear(status_id,month, year));
        return apiResponse;
    }
    @GetMapping("/countCompletedJobsForRequestProductByMonthAndYear")
    public ApiResponse<?> countCompletedJobsForRequestProductByMonthAndYear(@RequestParam("status_id") int status_id,@RequestParam("month") int month,@RequestParam("year") int year) {
        ApiResponse<Integer> apiResponse = new ApiResponse<>();
        apiResponse.setResult(statisticService.countCompletedJobsForRequestProductByMonthAndYear(status_id,month, year));
        return apiResponse;
    }
    @GetMapping("/countProduct")
    public ApiResponse<?> countProduct() {
        ApiResponse<Long> apiResponse = new ApiResponse<>();
        apiResponse.setResult(statisticService.countProduct());
        return apiResponse;
    }
    @GetMapping("/countTotalOrder")
    public ApiResponse<?> countTotalOrder() {
        ApiResponse<Long> apiResponse = new ApiResponse<>();
        apiResponse.setResult(statisticService.countTotalOrder());
        return apiResponse;
    }
    @GetMapping("/countRequestProduct")
    public ApiResponse<?> countRequestProduct() {
        ApiResponse<Long> apiResponse = new ApiResponse<>();
        apiResponse.setResult(statisticService.countRequestProduct());
        return apiResponse;
    }
    @GetMapping("/countSpecialOrder")
    public ApiResponse<?> countSpecialOrder() {
        ApiResponse<Long> apiResponse = new ApiResponse<>();
        apiResponse.setResult(statisticService.countSpecialOrder());
        return apiResponse;
    }
    @GetMapping("/countTotalOrderByMonthAndYear")
    public ApiResponse<?> countTotalOrderByMonthAndYear(@RequestParam("month") int month,@RequestParam("year") int year) {
        ApiResponse<Long> apiResponse = new ApiResponse<>();
        apiResponse.setResult(statisticService.countTotalOrderByMonthAndYear(month,year));
        return apiResponse;
    }
    @GetMapping("/countCompletedOrderByMonthAndYear")
    public ApiResponse<?> countCompletedOrderByMonthAndYear(@RequestParam("month") int month,@RequestParam("year") int year) {
        ApiResponse<Long> apiResponse = new ApiResponse<>();
        apiResponse.setResult(statisticService.countCompletedOrderByMonthAndYear(month,year));
        return apiResponse;
    }


    @GetMapping("/countOrderHaveDone")
    public ApiResponse<?> countOrderHaveDone(@RequestParam("query") int query) {
        ApiResponse<Long> apiResponse = new ApiResponse<>();
        apiResponse.setResult(statisticService.countOrderHaveDone(query));
        return apiResponse;
    }
    @GetMapping("/countEmployeeWithTypePosition")
    public ApiResponse<?> countEmployeeWithTypePosition(@RequestParam("query") int query) {
        ApiResponse<Long> apiResponse = new ApiResponse<>();
        apiResponse.setResult(statisticService.countEmployeeWithTypePosition(query));
        return apiResponse;
    }
    @GetMapping("/totalAmountOrderHaveDone")
    public ApiResponse<?> totalAmountOrderHaveDone() {
        ApiResponse<BigDecimal> apiResponse = new ApiResponse<>();
        apiResponse.setResult(statisticService.totalAmountOrderHaveDone());
        return apiResponse;
    }
    @GetMapping("/totalAmountSubMaterial")
    public ApiResponse<?> totalAmountSubMaterial() {
        ApiResponse<BigDecimal> apiResponse = new ApiResponse<>();
        apiResponse.setResult(statisticService.totalAmountSubMaterial());
        return apiResponse;
    }
    @GetMapping("/findTotalCostSubMaterialByMonthAndYear")
    public ApiResponse<?> findTotalCostSubMaterialByMonthAndYear(@RequestParam("month") int month,@RequestParam("year") int year) {
        ApiResponse<BigDecimal> apiResponse = new ApiResponse<>();
        apiResponse.setResult(statisticService.findTotalSubMaterialByMonthAndYear(month,year));
        return apiResponse;
    }

}
