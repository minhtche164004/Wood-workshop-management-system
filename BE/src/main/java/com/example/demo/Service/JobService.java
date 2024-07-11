package com.example.demo.Service;

import com.example.demo.Dto.JobDTO.JobDTO;
import com.example.demo.Dto.JobDTO.JobDoneDTO;
import com.example.demo.Dto.OrderDTO.JobProductDTO;
import com.example.demo.Dto.ProductDTO.ProductErrorAllDTO;
import com.example.demo.Dto.ProductDTO.ProductErrorDTO;
import com.example.demo.Entity.Advancesalary;
import com.example.demo.Entity.Jobs;
import com.example.demo.Entity.Processproducterror;
import com.example.demo.Entity.Status_Job;
import org.springframework.stereotype.Service;

import java.sql.Date;
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
    List<Status_Job> getAllStatusJob();


    Processproducterror AddProductError(int job_id,ProductErrorDTO productErrorDTO);
    List<ProductErrorAllDTO> getAllProductError();
    ProductErrorAllDTO getProductErrorDetailById(int query);
    ProductErrorAllDTO EditProductError(int error_id, ProductErrorDTO productErrorDTO);
   // boolean checkOderDoneOrNot(int order_id);

    List<Advancesalary> getAllAdvancesalary();
    List<Advancesalary> multi_filter_salary(Date fromDate, Date toDate,Integer position_id, String username, String sortDirection);
    List<Advancesalary> getAdvancesalaryByEmployeeId();
    Advancesalary ChangeStatus(int id,boolean check);
}
