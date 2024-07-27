package com.example.demo.Service.Impl;

import com.example.demo.Dto.JobDTO.JobCreateDTO;
import com.example.demo.Dto.JobDTO.JobDTO;
import com.example.demo.Dto.JobDTO.JobDoneDTO;
import com.example.demo.Dto.JobDTO.ListEmployeeDTO;
import com.example.demo.Dto.OrderDTO.JobProductDTO;
import com.example.demo.Dto.OrderDTO.OderDTO;
import com.example.demo.Dto.OrderDTO.OrderDetailWithJobStatusDTO;
import com.example.demo.Dto.ProductDTO.ProductErrorAllDTO;
import com.example.demo.Dto.ProductDTO.ProductErrorDTO;
import com.example.demo.Entity.*;
import com.example.demo.Exception.AppException;
import com.example.demo.Exception.ErrorCode;
import com.example.demo.Repository.*;
import com.example.demo.Service.CheckConditionService;
import com.example.demo.Service.JobService;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.ZoneId;
import java.util.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Service
public class JobServiceImpl implements JobService {
    @Autowired
    private JobRepository jobRepository;
    @Autowired
    private OrderDetailRepository orderDetailRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RequestProductRepository requestProductRepository;
    @Autowired
    private Status_Product_Repository statusProductRepository;
    @Autowired
    private Status_Job_Repository statusJobRepository;
    @Autowired
    private EntityManager entityManager;
    @Autowired
    private ProcessproducterrorRepository processproducterrorRepository;
    @Autowired
    private Status_Order_Repository statusOrderRepository;
    @Autowired
    private AdvancesalaryRepository advancesalaryRepository;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private CheckConditionService checkConditionService;
    @Autowired
    private Employee_Material_Repository employeeMaterialRepository;

    @Autowired
    private RequestProductsSubmaterialsRepository requestProductsSubmaterialsRepository;
    @Autowired
    private ProductSubMaterialsRepository productSubMaterialsRepository;

    @Override
    public List<JobProductDTO> getListRequestProductJob() {
        List<JobProductDTO> orderdetailsList = jobRepository.getRequestProductInJob();
        if (orderdetailsList == null) {
            throw new AppException(ErrorCode.NOT_FOUND);
        }
        return orderdetailsList;
    }

    @Override
    public List<JobProductDTO> getListProductJob() {
        List<JobProductDTO> orderdetailsList = jobRepository.getListProductJob();
        if (orderdetailsList == null) {
            throw new AppException(ErrorCode.NOT_FOUND);
        }
        return orderdetailsList;

    }

    @Override
    public List<JobProductDTO> getListProductJobByNameOrCode(String key) {
        List<JobProductDTO> orderdetailsList = jobRepository.getListProductJobByNameOrCodeProduct(key);
        if (orderdetailsList == null) {
            throw new AppException(ErrorCode.NOT_FOUND);
        }
        return orderdetailsList;
    }

    @Override
    public List<JobProductDTO> getRequestProductInOrderDetailByCode(String code) {
        List<JobProductDTO> jobProductDTOS = jobRepository.getRequestProductInOrderDetailByCode(code);
        if (jobProductDTOS.isEmpty()) {
            throw new AppException(ErrorCode.NOT_FOUND);
        }
        return jobProductDTOS;
    }

    //nếu check màn bên kia status đang là chưa giao việc , thì lúc giao việc , status tự động là giao làm mộc
    //nếu màn bên kia là đang là Đã nghiệm thu làm mộc thì lúc giao việc , status tự động màn bên cạnh là đang làm nhám
    //nếu màn bên kia là đang là Đã nghiệm thu làm nhám thì lúc giao việc , status tự động màn bên cạnh là đang làm sơn
    //nếu màn bên kia là đang là Đã nghiệm thu làm sơn thì lúc sửa status, nó sẽ sửa thành Sản phẩm đã hoàn thành , job thành công , quantity được cộng vào
    @Transactional
    @Override
    public Jobs CreateJob(JobDTO jobDTO, int user_id, int p_id, int status_id, int job_id, int type_job) {
        //  type_job để phân biệt alf hàng có sẵn hay hàng theo yêu cầu , 0 là ko có sẵn , 1 là có sẵn
        Jobs jobs = new Jobs();
        User user = userRepository.findByIdJob(user_id);
        jobs.setUser(user);
        if (type_job == 0) { //tức là đang phân job cho requets product
            RequestProducts requestProducts = requestProductRepository.findById(p_id);
            jobs.setRequestProducts(requestProducts);
            jobs.setProduct(null);
        } else {////tức là đang phân job cho  product có sẵn
            Products products = productRepository.findById(p_id);
            jobs.setProduct(products);
            jobs.setRequestProducts(null);
        }
        jobs.setDescription(jobDTO.getDescription());
        jobs.setQuantityProduct(jobDTO.getQuantity_product());
        jobs.setCost(jobDTO.getCost());
        jobs.setJob_name(jobDTO.getJob_name());
        jobs.setTimeFinish(jobDTO.getFinish());
        Jobs jobs_order_detail = jobRepository.getJobById(job_id);
        jobs.setOrderdetails(jobs_order_detail.getOrderdetails());
        jobs.setTimeStart(jobDTO.getStart());
        Status_Job statusJob = statusJobRepository.findById(status_id); //set  status job theo input
        jobs.setStatus(statusJob);
//        LocalDate today = LocalDate.now();
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("ddMMyy");
//        String dateString = today.format(formatter);
//        Jobs lastJob = jobRepository.findJobsTop(dateString + "JB");
//        int count = lastJob != null ? Integer.parseInt(lastJob.getCode().substring(8)) + 1 : 1;
//        String code = dateString + "JB" + String.format("%03d", count);
        jobs.setCode(jobs_order_detail.getCode());
        jobs.setJob_log(false);
        jobRepository.save(jobs);
        List<Processproducterror> processproducterrorList = processproducterrorRepository.getProcessproducterrorByJobId(job_id);
        for (Processproducterror p : processproducterrorList) {
            p.setJob(jobs);
            processproducterrorRepository.save(p);
        }
        jobRepository.delete(jobs_order_detail);
//        if(jobs_order_detail.getUser() == null){
//            jobRepository.delete(jobs_order_detail);
//        }

        return jobs;
    }

    @Override
    public List<ProductErrorAllDTO> getAllProductErrorByJobId(int job_id) {
        List<ProductErrorAllDTO> list = jobRepository.getAllProductErrorByJobId(job_id);
        if(list == null){
            throw new AppException(ErrorCode.NOT_FOUND);
        }
        return list;
    }

    @Override
    public List<ProductErrorAllDTO> MultiFilterErrorProduct(String search, Integer is_fixed) {
        List<ProductErrorAllDTO> error_list = new ArrayList<>();

        if (is_fixed != null) {
            if (is_fixed == -1) {
                // Không lọc theo is_fixed, chỉ lọc theo các tham số khác
                error_list= jobRepository.MultiFilterErrorProduct(search);
            } else {
                // Lọc theo is_fixed (true hoặc false) và các tham số khác
                boolean is_fixedValue = (is_fixed == 1); // Chuyển đổi 1/0 thành true/false
                error_list= jobRepository.MultiFilterErrorProductWithBoolean(search, is_fixedValue);
            }
        } else {
            // Không có tham số lọc nào, lấy tất cả đơn hàng
            error_list= jobRepository.getAllProductError();
        }

        if (error_list.isEmpty()) {
            throw new AppException(ErrorCode.NOT_FOUND);
        }
        return error_list;
    }


    @Override
    public Jobs CreateJob_Log(int job_id, int status_id) {
        //tạo mới job_log, lúc sửa status chứ ko phải lúc phân việc , sau khi đã sauwr status thành đã nghiệm thu của mộc, sơn , nhám
        //sau khi nghiem thu xong thi tra ve trang thai chờ cong viec tiep theo
        Jobs jobs_log = new Jobs();
        Jobs jobs_history = jobRepository.getJobById(job_id);
        jobs_log.setUser(jobs_history.getUser());
        jobs_log.setProduct(jobs_history.getProduct());
        jobs_log.setDescription(jobs_history.getDescription());
        jobs_log.setCost(jobs_history.getCost());
        jobs_log.setTimeStart(jobs_history.getTimeStart());
        jobs_log.setStatus(statusJobRepository.findById(status_id)); //set status thanh da nghiem thu
        jobs_log.setTimeFinish(jobs_history.getTimeFinish());
        jobs_log.setRequestProducts(jobs_history.getRequestProducts());
        jobs_log.setQuantityProduct(jobs_history.getQuantityProduct());
        jobs_log.setOrderdetails(jobs_history.getOrderdetails());
        jobs_log.setJob_name(jobs_history.getJob_name());
        jobs_log.setCode(jobs_history.getCode());
        jobs_log.setJob_log(false); // false để hiển thị , ví dụ đã nghiệm thu mộc ,dã nghiệm thu sơn
        jobRepository.save(jobs_log);

        //cai nay` la de tao ra job tiep theo cho san pham va o trang thai dang cho giao cong viec tiep
        Jobs waitNextJob = jobRepository.findById(job_id).orElseThrow(() -> new RuntimeException("Job not found"));
        waitNextJob.setProduct(jobs_history.getProduct());
        waitNextJob.setRequestProducts(jobs_history.getRequestProducts());
        if (status_id == 12) {
            //nếu công việc hoàn thành thì + số lượng của sản phẩm vào số lượng đã có trước đấy
            if (jobs_log.getProduct() == null) {
                RequestProducts requestProducts = requestProductRepository.findById(jobs_history.getRequestProducts().getRequestProductId());
                requestProducts.setQuantity(requestProducts.getQuantity() + jobs_history.getQuantityProduct());
                requestProductRepository.save(requestProducts);
                jobs_log.setProduct(null);
                //------ đoạn này chỉ dành cho request product , vì nó là đơn hàng , còn product có sẵn thì lúc cọc xong thì chuyển sang status là đã thi công xong luôn
                Orders orders = orderRepository.findByCode(jobs_history.getOrderdetails().getOrder().getCode());
                if (checkOderDoneOrNot(orders.getOrderId()) == true) {
                    orders.setStatus(statusOrderRepository.findById(4)); //nghĩa là đơn hàng đã thi công xong
                    orderRepository.save(orders);
                }
            }
            if (jobs_log.getRequestProducts() == null) {
                Products products = productRepository.findById(jobs_history.getProduct().getProductId());
                products.setQuantity(products.getQuantity() + jobs_history.getQuantityProduct());
                productRepository.save(products);
                jobs_log.setRequestProducts(null);
            }

            jobs_log.setStatus(statusJobRepository.findById(13));
            jobs_log.setJob_log(true);

            //thêm lương cho thằng thợ sơn
            //nếu công việc hoàn thành thì set lương cho nhân viên luôn --------------------------------------
            Advancesalary advancesalary = new Advancesalary();
            LocalDate today = LocalDate.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("ddMMyy");
            String dateString = today.format(formatter);
            Advancesalary lastadvan = advancesalaryRepository.findAdvancesalaryTop(dateString + "AD");
            int count = lastadvan != null ? Integer.parseInt(lastadvan.getCode().substring(8)) + 1 : 1;
            String code = dateString + "AD" + String.format("%03d", count);
            advancesalary.setDate(Date.from(today.atStartOfDay(ZoneId.systemDefault()).toInstant()));

            //  advancesalary.setDate(Date.valueOf(today));
            advancesalary.setAmount(jobs_history.getCost());
//            advancesalary.setApprove(null);
            advancesalary.setAdvanceSuccess(false);
            advancesalary.setCode(code);
            advancesalary.setUser(jobs_history.getUser());
            advancesalaryRepository.save(advancesalary);



            //khi da nghiem thu o cong doan cuoi la son thi` se chuyen status sang trang thai da hoan` thanh
        } else {
            //nếu công việc hoàn thành thì set lương cho nhân viên luôn --------------------------------------
            Advancesalary advancesalary = new Advancesalary();
            LocalDate today = LocalDate.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("ddMMyy");
            String dateString = today.format(formatter);
            Advancesalary lastadvan = advancesalaryRepository.findAdvancesalaryTop(dateString + "AD");
            int count = lastadvan != null ? Integer.parseInt(lastadvan.getCode().substring(8)) + 1 : 1;
            String code = dateString + "AD" + String.format("%03d", count);
            advancesalary.setDate(Date.from(today.atStartOfDay(ZoneId.systemDefault()).toInstant()));

            //  advancesalary.setDate(Date.valueOf(today));
            advancesalary.setAmount(jobs_history.getCost());
//            advancesalary.setApprove(null);
            advancesalary.setAdvanceSuccess(false);
            advancesalary.setCode(code);
            advancesalary.setUser(jobs_history.getUser());
            advancesalaryRepository.save(advancesalary);
            ///------------------------------------------------------------------------------------------
            waitNextJob.setStatus(statusJobRepository.findById(15)); // khi da nghiem thu thi se chuyen sang trang thai chờ cong viec tiep theo
        }
        waitNextJob.setJob_log(true); // true đển ẩn , ví dụ đã nghiệm thu xong chờ giai đoạn tiếp theo , ở đây list trong job ra thì bắt đc user_id cảu
        // thằng đảm nhận -> bắt dc position thằng đảm nhận là thợ mộc ,thợ sơn ,thợ nhám luôn rồi , không cần phải hiện status mới đc
        jobRepository.save(waitNextJob);
        //  jobRepository.delete(jobs_history);
        return jobs_log;
    }
//    private boolean checkOderDoneOrNot(int order_id) {
//        List<OrderDetailWithJobStatusDTO> results = orderDetailRepository.getAllOrderDetailByOrderIdCheck(order_id);
//        if(!results.isEmpty()){
//            return false;
//        }
//        return true; // Chỉ trả về true nếu tất cả các job đều đã hoàn thành (status_id = 13)
//    }

    private boolean checkOderDoneOrNot(int order_id) {
        List<OrderDetailWithJobStatusDTO> results = orderDetailRepository.getAllOrderDetailByOrderIdCheck(order_id);
     //   for(OrderDetailWithJobStatusDTO p :results){
            if(results.size() >= 1){
                return false;
            }
       // }
        return true;
    }




    @Override
    public Jobs EditJobs(JobDTO jobDTO, int job_id) {
        Jobs jobs = jobRepository.getJobById(job_id);
        jobs.setJob_name(jobDTO.getJob_name());
        jobs.setCost(jobDTO.getCost());
        jobs.setDescription(jobDTO.getDescription());
        jobs.setQuantityProduct(jobDTO.getQuantity_product());
        jobs.setTimeFinish(jobDTO.getFinish());
        jobs.setTimeStart(jobDTO.getStart());
        jobRepository.save(jobs);
//        entityManager.refresh(jobs);
        return jobs;
    }

    @Override
    public Jobs GetJobById(int job_id) {
        Jobs jobs = jobRepository.getJobById(job_id);
        if (jobs == null) {
            throw new AppException(ErrorCode.NOT_FOUND);
        }
        return jobs;
    }

//    @Override
//    public List<Jobs> GetAllJob() {
//        List<Jobs> jobsList = jobRepository.findAll();
//        if(jobsList == null ){
//            throw new AppException(ErrorCode.NOT_FOUND);
//        }
//        return jobsList;
//    }

    //đây là api cho viẹc thêm product có sẵn vào lits job ,
    // thêm vào thì nó chưa đc giao việc , chưa có người nhận
    @Override
    public Jobs AddProductForJob(int p_id, int quantity_product) {
        Jobs jobs = new Jobs();
        Products products = productRepository.findById(p_id);
        jobs.setJob_log(false);
        jobs.setProduct(products);
        jobs.setQuantityProduct(quantity_product);
        jobs.setStatus(statusJobRepository.findById(3));

        LocalDate today = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("ddMMyy");
        String dateString = today.format(formatter);
        Jobs lastJob = jobRepository.findJobsTop(dateString + "JB");
        int count = lastJob != null ? Integer.parseInt(lastJob.getCode().substring(8)) + 1 : 1;
        String code = dateString + "JB" + String.format("%03d", count);
        jobs.setCode(code);
        jobRepository.save(jobs);
        return jobs;
    }

    @Override
    public List<Jobs> getJobWasDone() {
        List<Jobs> jobsList = jobRepository.getJobWasDone();
        if (jobsList == null) {
            throw new AppException(ErrorCode.NOT_FOUND);
        }
        return jobsList;
    }

    @Override
    public List<JobDoneDTO> filterJobWasDoneByEmployeeName(String keyword) {
        List<JobDoneDTO> jobsList = jobRepository.filterJobWasDoneByEmployeeName(keyword);
        if (jobsList == null) {
            throw new AppException(ErrorCode.NOT_FOUND);
        }
        return jobsList;
    }

    @Override
    public List<JobDoneDTO> findAllJobForDoneByEmployeeID() {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userRepository.getUserByUsername(userDetails.getUsername());
        List<JobDoneDTO> jobsList = jobRepository.findAllJobForDoneByEmployeeID(user.getUserId());
        if (jobsList == null) {
            throw new AppException(ErrorCode.NOT_FOUND);
        }
        return jobsList;
    }

    @Override
    public List<JobDoneDTO> findAllJobForDoneByEmployeeIDWithJobCode(String query) {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userRepository.getUserByUsername(userDetails.getUsername());
        List<JobDoneDTO> jobsList = jobRepository.findAllJobForDoneByEmployeeIDWithJobCode(user.getUserId(), query);
        if (jobsList == null) {
            throw new AppException(ErrorCode.NOT_FOUND);
        }
        return jobsList;
    }

    @Override
    public List<Status_Job> getAllStatusJob() {
        return statusJobRepository.getAllStatus();
    }

    @Override
    public Processproducterror AddProductError(int job_id, ProductErrorDTO productErrorDTO) {
        Processproducterror processproducterror = new Processproducterror();
        Jobs jobs = jobRepository.getJobById(job_id);
        processproducterror.setJob(jobs);
        processproducterror.setCode(jobs.getCode());
        processproducterror.setIsFixed(false);
        processproducterror.setQuantity(productErrorDTO.getQuantity());
        if (!checkConditionService.checkInputQuantityInt(productErrorDTO.getQuantity())) {
            throw new AppException(ErrorCode.QUANTITY_INVALID);
        }
        if (checkConditionService.checkInputQuantityIntForProductError(processproducterror.getQuantity(), jobs.getQuantityProduct()) == false) {
            throw new AppException(ErrorCode.INVALID_QUANTITY_PRODUCT_ERROR);
        }
        processproducterror.setDescription(productErrorDTO.getDescription());
        processproducterror.setSolution(productErrorDTO.getSolution());

        Products product = jobs.getProduct(); // Lấy đối tượng Product
        if (product == null) { // Kiểm tra product có null không
            processproducterror.setProduct(null);
            processproducterror.setRequestProducts(jobs.getRequestProducts());
        } else {
            processproducterror.setRequestProducts(null);
            processproducterror.setProduct(product); // Gán trực tiếp product
        }

        processproducterrorRepository.save(processproducterror);
        return processproducterror;
    }

    @Override
    public List<ProductErrorAllDTO> getAllProductError() {
        List<ProductErrorAllDTO> list = jobRepository.getAllProductError();
        if (list == null) {
            throw new AppException(ErrorCode.NOT_FOUND);
        }
        return list;
    }

    @Override
    public ProductErrorAllDTO getProductErrorDetailById(int query) {
        ProductErrorAllDTO productErrorAllDTO = jobRepository.getProductErrorDetailById(query);
        if (productErrorAllDTO == null) {
            throw new AppException(ErrorCode.NOT_FOUND);
        }
        return productErrorAllDTO;
    }

    @Override
    public ProductErrorAllDTO EditProductError(int error_id, ProductErrorDTO productErrorDTO) {
        Processproducterror processproducterror = processproducterrorRepository.FindByIdProductErrorId(error_id);
        processproducterror.setDescription(productErrorDTO.getDescription());
        processproducterror.setIsFixed(productErrorDTO.getIsFixed());
        processproducterror.setSolution(productErrorDTO.getSolution());
        processproducterror.setQuantity(productErrorDTO.getQuantity());
        processproducterrorRepository.save(processproducterror);
        return modelMapper.map(processproducterror, ProductErrorAllDTO.class);
    }

    @Override
    public List<Advancesalary> getAllAdvancesalary() {
        List<Advancesalary> list = advancesalaryRepository.getAllByFalseAdvanceSuccess();
        if (list == null) {
            throw new AppException(ErrorCode.NOT_FOUND);
        }
        return list;
    }


    @Override
    public List<Advancesalary> multi_filter_salary(Date fromDate, Date toDate, Integer isAdvanceSuccess, Integer position_id, String username, String sortDirection) {
        List<Advancesalary> advancesalaryList = new ArrayList<>();
        boolean advance;
        if (position_id != null || fromDate != null || toDate != null || username != null || isAdvanceSuccess != null) {
            if (isAdvanceSuccess == null) {
                advancesalaryList = advancesalaryRepository.filterAdvancesalary(fromDate, toDate, position_id, username);
            } else {
                advancesalaryList = advancesalaryRepository.filterAdvancesalaryWithAdvanceSuccess(fromDate, toDate, isAdvanceSuccess == 0 ? false : true, position_id, username);
            }
        } else {
            advancesalaryList = advancesalaryRepository.findAll();
        }
//        if (search != null || categoryId != null || statusId != null || minPrice != null || maxPrice != null) {
//            productList = productRepository.filterProductsForAdmin(search, categoryId, statusId, minPrice, maxPrice);
//        } else {
//            productList = productRepository.findAll();
//        }
        //    List<Advancesalary> advancesalaryList = advancesalaryRepository.filterAdvancesalary(fromDate, toDate, position_id, "%" + username + "%");

        if (advancesalaryList.isEmpty()) {
            throw new AppException(ErrorCode.NOT_FOUND);
        }

        // Sắp xếp danh sách lương tạm ứng theo ngày
        if (sortDirection != null) {
            if (sortDirection.equalsIgnoreCase("asc")) {
                advancesalaryList.sort(Comparator.comparing(Advancesalary::getAmount));
            } else if (sortDirection.equalsIgnoreCase("desc")) {
                advancesalaryList.sort(Comparator.comparing(Advancesalary::getAmount).reversed());
            }
        }

        return advancesalaryList;
    }

    @Override
    public List<Advancesalary> getAdvancesalaryByEmployeeId() {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userRepository.getUserByUsername(userDetails.getUsername());
        List<Advancesalary> list = advancesalaryRepository.findByUserId(user.getUserId());
        if (list == null) {
            throw new AppException(ErrorCode.NOT_FOUND);
        }
        return list;
    }

    @Transactional
    @Override
    public Advancesalary ChangeStatus(int id, boolean check) {
        advancesalaryRepository.update_banking(id, check);
        return advancesalaryRepository.findById(id);
    }

    @Override
    public Long CountQuantityOfJob(String status_name, int month, int year) {
        return jobRepository.countCompletedJobsByMonthAndYear(status_name, month, year);
    }

    @Override
    public List<Employeematerials> getAllMaterialForEmployee() {
        List<Employeematerials> list = employeeMaterialRepository.findAll();
        if (list == null) {
            throw new AppException(ErrorCode.NOT_FOUND);
        }
        return list;
    }

    @Override
    public boolean checkErrorOfJobHaveFixDoneOrNot(int job_id) {
        List<Processproducterror> list = processproducterrorRepository.getProcessproducterrorByJobIdHaveFixNotDone(job_id);
        for(Processproducterror p : list){
            if(p.getIsFixed() == false){
                return false;
            }
        }
        return true;
    }

    @Override
    public List<JobProductDTO> MultiFilterRequestProductInJob(String search, Integer status_id, Integer position_id) {
        List<JobProductDTO> productList = new ArrayList<>();

        if (search != null || position_id != null || status_id != null) {
            productList = jobRepository.MultiFilterRequestProductInJob(search, status_id, position_id);
        } else {
            productList = jobRepository.getRequestProductInJob();
        }

        if (productList.isEmpty()) {
            throw new AppException(ErrorCode.NOT_FOUND);
        }
        return productList;
    }

    @Override
    public List<JobProductDTO> MultiFilterListProductJob(String search, Integer status_id, Integer position_id) {
        List<JobProductDTO> productList = new ArrayList<>();

        if (search != null || position_id != null || status_id != null) {
            productList = jobRepository.MultiFilterListProductJob(search, status_id, position_id);
        } else {
            productList = jobRepository.getListProductJob();
        }

        if (productList.isEmpty()) {
            throw new AppException(ErrorCode.NOT_FOUND);
        }
        return productList;
    }

    public static void main(String[] args) {
        System.out.println();
    }

    @Override
    public List<Advancesalary> MultiFilterOrderSpecialOrder(String search,Integer isAdvanceSuccess, Date startDate, Date endDate) {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userRepository.getUserByUsername(userDetails.getUsername());
        List<Advancesalary> order_list = new ArrayList<>();

        if (isAdvanceSuccess != null) {
            if (isAdvanceSuccess == -1) {
                // Không lọc theo specialOrder, chỉ lọc theo các tham số khác
                order_list= advancesalaryRepository.MultiFilterAdvansalaryByEmployeeIdByDate(user.getUserId(),search,startDate,endDate);
            } else {
                // Lọc theo specialOrder (true hoặc false) và các tham số khác
                boolean specialOrderValue = (isAdvanceSuccess == 1); // Chuyển đổi 1/0 thành true/false
                order_list= advancesalaryRepository.MultiFilterAdvansalaryByEmployeeId(user.getUserId(),search,specialOrderValue,startDate,endDate);
            }
        } else {
            // Không có tham số lọc nào, lấy tất cả đơn hàng
            order_list= advancesalaryRepository.findByUserId(user.getUserId());
        }

        if (order_list.isEmpty()) {
            throw new AppException(ErrorCode.NOT_FOUND);
        }
        return order_list;
    }

    @Override
    public List<JobDoneDTO> MultiFilterJobDone(String search, Integer status_id, Integer position_id) {
        List<JobDoneDTO> productList = new ArrayList<>();

        if (search != null || status_id != null || position_id != null ) {
            productList = jobRepository.MultiFilterJobDone(search, status_id, position_id);
        } else {
            productList = jobRepository.findAllJobForEmployeeDone();
        }

        if (productList.isEmpty()) {
            throw new AppException(ErrorCode.NOT_FOUND);
        }

        return productList;
    }


}

//note : co ne de status cua thang product luc add no la chua thuc hien
