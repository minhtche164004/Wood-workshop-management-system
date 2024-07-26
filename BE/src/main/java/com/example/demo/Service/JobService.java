package com.example.demo.Service;

import com.example.demo.Dto.JobDTO.JobDTO;
import com.example.demo.Dto.JobDTO.JobDoneDTO;
import com.example.demo.Dto.OrderDTO.JobProductDTO;
import com.example.demo.Dto.ProductDTO.ProductErrorAllDTO;
import com.example.demo.Dto.ProductDTO.ProductErrorDTO;
import com.example.demo.Entity.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Service
public interface JobService {

//    Jobs CreateJobForEmployee(JobDTO jobDTO);
    List<JobProductDTO> getListRequestProductJob();
//
    List<JobProductDTO> getListProductJob();

    List<JobProductDTO> getListProductJobByNameOrCode(String key);
//
    List<JobProductDTO> getRequestProductInOrderDetailByCode(String code);
    Jobs CreateJob(JobDTO jobDTO, int user_id, int p_id, int status_id, int job_id,int type_job);

    Jobs CreateJob_Log(int job_id,int status_id);

    Jobs EditJobs(JobDTO jobDTO,int job_id);
    Jobs GetJobById(int job_id);

//    List<Jobs> GetAllJob();

    Jobs AddProductForJob(int p_id, int quantity_product);

    List<Jobs> getJobWasDone();
    List<JobDoneDTO> filterJobWasDoneByEmployeeName(String keyword);
    List<JobDoneDTO> findAllJobForDoneByEmployeeID();
    List<JobDoneDTO> findAllJobForDoneByEmployeeIDWithJobCode(String query);
    List<Status_Job> getAllStatusJob();


    Processproducterror AddProductError(int job_id,ProductErrorDTO productErrorDTO);
    List<ProductErrorAllDTO> getAllProductError();
    List<ProductErrorAllDTO> getAllProductErrorByJobId(int job_id);
     List<ProductErrorAllDTO> MultiFilterErrorProduct(String search, Integer is_fixed);


    ProductErrorAllDTO getProductErrorDetailById(int query);
    ProductErrorAllDTO EditProductError(int error_id, ProductErrorDTO productErrorDTO);
   // boolean checkOderDoneOrNot(int order_id);

    List<Advancesalary> getAllAdvancesalary();

    List<Advancesalary> multi_filter_salary(java.util.Date fromDate, Date toDate, Integer isAdvanceSuccess, Integer position_id, String username, String sortDirection);
    List<Advancesalary> getAdvancesalaryByEmployeeId();
    Advancesalary ChangeStatus(int id,boolean check);

    Long CountQuantityOfJob(String status_name,int month,int year);

    List<Employeematerials> getAllMaterialForEmployee();

    boolean checkErrorOfJobHaveFixDoneOrNot(int job_id);


    List<JobProductDTO> MultiFilterRequestProductInJob(String search, Integer status_id, Integer position_id);

    List<JobProductDTO> MultiFilterListProductJob(String search, Integer status_id, Integer position_id);
}
