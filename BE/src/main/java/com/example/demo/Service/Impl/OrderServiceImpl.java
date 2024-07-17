package com.example.demo.Service.Impl;

import com.example.demo.Dto.OrderDTO.OrderDetailDTO;
import com.example.demo.Dto.OrderDTO.OrderDetailWithJobStatusDTO;
import com.example.demo.Dto.ProductDTO.*;
import com.example.demo.Dto.RequestDTO.*;
import com.example.demo.Dto.OrderDTO.ProductItem;
import com.example.demo.Dto.OrderDTO.RequestOrder;
import com.example.demo.Entity.*;
import com.example.demo.Entity.UserInfor;
import com.example.demo.Exception.AppException;
import com.example.demo.Exception.ErrorCode;
import com.example.demo.Mail.EmailService;
import com.example.demo.Mail.MailBody;
import com.example.demo.Repository.*;
import com.example.demo.Service.*;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
public class    OrderServiceImpl implements OrderService {
    @Autowired
    private Status_Order_Repository statusOrderRepository;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private InformationUserRepository informationUserRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private CheckConditionService checkConditionService;
    @Autowired
    private UserRepository userRepository;
//    @Autowired
//    private Status_Request_Repository statusRequestRepository;
    @Autowired
    private UploadImageService uploadImageService;
    @Autowired
    private RequestProductRepository requestProductRepository;
//    @Autowired
//    private RequestRepository requestRepository;
    @Autowired
    private Product_RequestimagesRepository productRequestimagesRepository;
    @Autowired
    private RequestimagesRepository requestimagesRepository;
    @Autowired
    private OrderDetailRepository orderDetailRepository;
    @Autowired
    private JobRepository jobRepository;
    @Autowired
    private Status_Job_Repository statusJobRepository;
    @Autowired
    private EntityManager entityManager;
    @Autowired
    CloudinaryService cloudinaryService;
    @Autowired
    private EmailService emailService;
    @Autowired
    private RequestProductsSubmaterialsRepository requestProductsSubmaterialsRepository;
    @Autowired
    private Status_Product_Repository statusProductRepository;
    @Autowired
    private ProcessproducterrorRepository processproducterrorRepository;


    @Override
    public Orders AddOrder(RequestOrder requestOrder) {
        Map<String, String> errors = new HashMap<>(); //hashmap cho error

        LocalDate currentDate = LocalDate.now();
        java.sql.Date sqlCompletionTime = java.sql.Date.valueOf(currentDate); // Chuyển đổi sang java.sql.Date

        UserInfor userInfor;
        boolean userIsExists = informationUserRepository.existsById(requestOrder.getCusInfo().getUserid());
        if (!userIsExists) {
            // Nếu không tìm thấy thông tin người dùng thi tao moi
            userInfor = new UserInfor();
            userInfor.setFullname(requestOrder.getCusInfo().getFullname());
            userInfor.setAddress(requestOrder.getCusInfo().getAddress());
            userInfor.setPhoneNumber(requestOrder.getCusInfo().getPhone());
            informationUserRepository.save(userInfor);
        } else {
            userInfor = informationUserRepository.findById(requestOrder.getCusInfo().getUserid()).get();
        }


        Orders orders = new Orders();
        orders.setOrderDate(sqlCompletionTime);
        Status_Order statusOrder = statusOrderRepository.findById(1);//tự set cho nó là 1
        orders.setStatus(statusOrder);
        orders.setOrderFinish(requestOrder.getOrderFinish()); // set ngay hoan thanh order
        orders.setPaymentMethod(requestOrder.getPayment_method()); //1 là trả tiền trực tiếp, 2 là chuyển khoản
        orders.setAddress(requestOrder.getCusInfo().getAddress());
        orders.setFullname(requestOrder.getCusInfo().getFullname());
        orders.setPhoneNumber(requestOrder.getCusInfo().getPhone());
        //day la` dia chi nhan hang cua khach hang
        orders.setCity_province(requestOrder.getCusInfo().getAddress());
        orders.setDistrict(requestOrder.getCusInfo().getAddress());
        orders.setWards(requestOrder.getCusInfo().getAddress());


        orders.setUserInfor(userInfor);
        LocalDate today = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("ddMMyy");
        String dateString = today.format(formatter);

        Orders lastOrder = orderRepository.findOrderTop(dateString + "OD");
        int count = lastOrder != null ? Integer.parseInt(lastOrder.getCode().substring(8)) + 1 : 1;
        String code = dateString + "OD" + String.format("%03d", count);
        orders.setCode(code);

        orderRepository.save(orders);


        if (requestOrder.getSpecial_order() == 0) { // là hàng có sẵn
            BigDecimal total = BigDecimal.ZERO; // Khởi tạo total là 0
            List<ProductItem> productItems = requestOrder.getOrderDetail().getProductItems(); // Lấy danh sách sản phẩm

            // Kiểm tra nếu danh sách sản phẩm không rỗng
            if (productItems != null && !productItems.isEmpty()) {
                for (ProductItem item : productItems) { // Duyệt qua từng sản phẩm
                    Products product = productRepository.findById(item.getId());
                    Orderdetails orderdetail = new Orderdetails();
                    orderdetail.setOrder(orders);
                    orderdetail.setProduct(productRepository.findById(item.getId()));
                    orderdetail.setQuantity(item.getQuantity()); //set quantity
                    orderdetail.setUnitPrice(item.getPrice()); //set unit price

                    if (orderdetail.getProduct().getQuantity() < item.getQuantity()) {
//                        throw new AppException(ErrorCode.OUT_OF_STOCK);
                        int b = orderdetail.getProduct().getQuantity();
                        int c = item.getQuantity();
                        int a = c-b;
                        String errorMessage = String.format("Sản phẩm có mã sản phẩm là:" +orderdetail.getProduct().getCode()+" đang thiếu số lượng để đủ cho đơn hàng là " +a+" cái");
                        errors.put(orderdetail.getProduct().getCode(), errorMessage);
                    }
                    product.setQuantity(product.getQuantity() - item.getQuantity());
                    productRepository.save(product);
                    orderdetail.setRequestProduct(null);
                    BigDecimal itemPrice = item.getPrice();
                    BigDecimal itemQuantity = BigDecimal.valueOf(item.getQuantity());
                    total = total.add(itemPrice.multiply(itemQuantity)); // Cộng dồn vào total
                    orderDetailRepository.save(orderdetail);


                }
            }
            orders.setDeposite(total.multiply(BigDecimal.valueOf(0.2))); // 20% tiền cọc của tổng tiền đơn hàng
            orders.setTotalAmount(total);
            orders.setSpecialOrder(false);
        }
        if (requestOrder.getSpecial_order() == 1) {//là hàng ko có sẵn

            BigDecimal total = BigDecimal.ZERO; // Khởi tạo total là 0
            List<ProductItem> requestProductItems = requestOrder.getOrderDetail().getProductItems();

            // Kiểm tra nếu danh sách sản phẩm không rỗng
            if (requestProductItems != null && !requestProductItems.isEmpty()) {
                for (ProductItem item : requestProductItems) { // Duyệt qua từng sản phẩm
                    RequestProducts requestProducts = requestProductRepository.findById(item.getId());
                    Orderdetails orderdetail = new Orderdetails();
                    orderdetail.setOrder(orders);
                    orderdetail.setRequestProduct(requestProductRepository.findById(item.getId()));
                    orderdetail.setQuantity(item.getQuantity()); //set quantity
                    orderdetail.setUnitPrice(item.getPrice()); //set unit price
//                        if(orderdetail.getRequestProduct().getQuantity() < item.getQuantity()){
//                            throw new AppException(ErrorCode.OUT_OF_STOCK);
//                        }
                    requestProducts.setQuantity(requestProducts.getQuantity() - item.getQuantity());
                    requestProductRepository.save(requestProducts);
                    orderdetail.setProduct(null); //set product null
                    BigDecimal itemPrice = item.getPrice();
                    BigDecimal itemQuantity = BigDecimal.valueOf(item.getQuantity());
                    total = total.add(itemPrice.multiply(itemQuantity)); // Cộng dồn vào total
                    orderDetailRepository.save(orderdetail);

                    Jobs jobs = new Jobs();
                    jobs.setRequestProducts(requestProducts);
                    jobs.setQuantityProduct(orderdetail.getQuantity());
                    Jobs lastJob = jobRepository.findJobsTop(dateString + "JB");
                    int count1 = lastJob != null ? Integer.parseInt(lastJob.getCode().substring(8)) + 1 : 1;
                    String code1 = dateString + "JB" + String.format("%03d", count1);
                    jobs.setCode(code1);
                    jobs.setJob_name("");
                    jobs.setOrderdetails(orderdetail);
                    jobs.setJob_log(false);
                    jobs.setStatus(statusJobRepository.findById(14));
                    jobRepository.save(jobs);

                }
            }
            orders.setDeposite(total.multiply(BigDecimal.valueOf(0.2))); // 20% tiền cọc của tổng tiền đơn hàng
            orders.setTotalAmount(total);
            orders.setSpecialOrder(true);
        }

        orderRepository.save(orders);

        return orders;
    }
    @Override
    public ResponseEntity<String> Cancel_Order(int order_id, boolean special_order_id) {

        Orders orders = orderRepository.findById(order_id);
        if(special_order_id == false){//là hàng có sẵn
            List<Orderdetails> list = orderDetailRepository.getOrderDetailByOrderId(order_id);
            for(Orderdetails orderdetails : list){
              int product_id =  orderdetails.getProduct().getProductId();
                Products products = productRepository.findById(product_id);
                products.setQuantity(products.getQuantity()+orderdetails.getProduct().getQuantity());
                productRepository.save(products);
            }
            orders.setStatus(statusOrderRepository.findById(6));//set cho nó là đơn hàng bị huỷ
            orderRepository.save(orders);


            return ResponseEntity.ok("Huỷ đơn hàng thành công");

        }
        if(special_order_id == true){//là hàng ko có sẵn (nếu jb đang làm dở thì cho làm cho xong , còn nếu job chưa giao việc thì xoá nó đi )
            List<Orderdetails> list = orderDetailRepository.getOrderDetailByOrderId(order_id);
            for(Orderdetails orderdetails : list){
//                int request_product_id =  orderdetails.getRequestProduct().getRequestProductId();
//                RequestProducts requestProducts = requestProductRepository.findById(request_product_id);
                List<Jobs> jobsList = jobRepository.getJobByOrderDetailByOrderCode(orders.getCode());

                for(Jobs jobs : jobsList){
                    if(jobs.isJob_log() == false && jobs.getUser() == null){
                        List<Processproducterror> processproducterrorList=processproducterrorRepository.getProcessproducterrorByJobId(jobs.getJobId());
                        for(Processproducterror processproducterror : processproducterrorList){
                            processproducterrorRepository.delete(processproducterror);
                        }
                        jobRepository.delete(jobs);
                        return ResponseEntity.ok("Huỷ đơn hàng thành công");
                    }
                    if(jobs.isJob_log()==false && jobs.getUser() != null){
                        return ResponseEntity.badRequest().body("Hãy hoàn thành công việc của "+jobs.getUser().getPosition().getPosition_name()+" có tên là "+jobs.getUser().getUsername()+" trước khi huỷ đơn hàng");
                    }
                }
//                requestProducts.setQuantity(requestProducts.getQuantity()+orderdetails.getRequestProduct().getQuantity());
//                requestProductRepository.save(requestProducts);
            }
            orders.setStatus(statusOrderRepository.findById(6));//set cho nó là đơn hàng bị huỷ
            orderRepository.save(orders);

        }
        return ResponseEntity.ok("Huỷ đơn hàng thành công");
    }

    //Tạo Request
    //Tạo Request Product
    @Override
    public Orders AddNewRequest(RequestDTO requestDTO, MultipartFile[] multipartFiles) {
        Orders requests = new Orders();
        UserDetails userDetails =(UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user =userRepository.getUserByUsername(userDetails.getUsername());
        //lấy thông tin thằng đang login
        //   User user = userRepository.findById(requestDTO.getUser_id()).get();
        LocalDate today = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("ddMMyy");
        String dateString = today.format(formatter);
        Date requestDate = Date.from(today.atStartOfDay(ZoneId.systemDefault()).toInstant());
        requests.setUserInfor(user.getUserInfor());
        Status_Order statusRequest =statusOrderRepository.findById(7);//nghĩa là request đang chờ phê duyệt
        requests.setOrderDate(requestDate); //lấy time hiện tại
        requests.setDescription(requestDTO.getDescription());
        requests.setStatus(statusRequest);
        requests.setAddress(requestDTO.getAddress()); //thông tin người đặt sẽ bắt theo infor_id, còn thông tin người nhận với đơn hàng đẳc biệt sẽ cho người đặt nhập
        requests.setFullname(requestDTO.getFullname());
        requests.setPhoneNumber(requestDTO.getPhoneNumber());
        requests.setCity_province(requestDTO.getCity_province());
        requests.setDistrict(requestDTO.getDistrict_province());
        requests.setWards(requestDTO.getWards_province());
        requests.setResponse("");

        requests.setSpecialOrder(true);//đặt cho đơn hàng theo yêu cầu là đơn hàng đặc biệt

        Orders lastOrder = orderRepository.findOrderTop(dateString + "OD");
        int count = lastOrder != null ? Integer.parseInt(lastOrder.getCode().substring(8)) + 1 : 1;
        String code = dateString + "OD" + String.format("%03d", count);
        requests.setCode(code);

//        Requests lastRequest = requestRepository.findRequestTop(dateString + "RQ");
//        int count = lastRequest != null ? Integer.parseInt(lastRequest.getCode().substring(8)) + 1 : 1;
//        String code = dateString + "RQ" + String.format("%03d", count);
//        requests.setCode(code);
        if (!checkConditionService.checkInputName(requestDTO.getFullname())) {
            throw new AppException(ErrorCode.INVALID_FORMAT_NAME);
        }

        requests = orderRepository.save(requests);
        uploadImageService.uploadFileRequest(multipartFiles, requests.getOrderId());

        return requests;
    }

    @Transactional
    @Override
    public Orders EditRequest(int request_id, RequestEditCusDTO requestEditDTO, MultipartFile[] multipartFiles) throws IOException {
        Orders requests = orderRepository.findById(request_id);
        if (multipartFiles != null && Arrays.stream(multipartFiles).anyMatch(file -> file != null && !file.isEmpty())) {
            List<Requestimages> requestimagesList= requestimagesRepository.findRequestImageByRequestId(request_id);
            for(Requestimages requestimages : requestimagesList){
                String full_path= requestimages.getFullPath();
                String id_image =cloudinaryService.extractPublicIdFromUrl(full_path);
                cloudinaryService.deleteImage(id_image);
            }
            requestimagesRepository.deleteRequestImages(request_id); // Xóa những ảnh trước đó
            uploadImageService.uploadFileRequest(multipartFiles, requests.getOrderId());
        }
        orderRepository.updateRequest(request_id,
                requestEditDTO.getDescription()
        );
        entityManager.refresh(requests); // Làm mới đối tượng products
        return requests;
    }

//    @Override
//    public Requests getRequestById(int id) {
//        return requestRepository.findById(id);
//    }

    @Override
    public RequestProducts getRequestProductsById(int id) {
        return requestProductRepository.findById(id);
    }

    @Transactional
    @Override
    public void Approve_Reject_Request(int id, int status_id) {
        orderRepository.updateStatus(id, status_id);
    }

    //Tạo Request Product
    @Override
    public RequestProducts AddNewProductRequest(RequestProductDTO requestProductDTO, MultipartFile[] multipartFiles) { //lấy từ request

        RequestProducts requestProducts = new RequestProducts();
        requestProducts.setRequestProductName(requestProductDTO.getRequestProductName());
        requestProducts.setDescription(requestProductDTO.getDescription());
        requestProducts.setPrice(requestProductDTO.getPrice());
        Status_Product status = statusProductRepository.findById(2);//tuc la kich hoạt
        requestProducts.setStatus(status);
        requestProducts.setQuantity(requestProductDTO.getQuantity());
        requestProducts.setCompletionTime(requestProductDTO.getCompletionTime());
        Orders requests = orderRepository.findById(requestProductDTO.getRequest_id());
        requestProducts.setOrders(requests);
        if (!checkConditionService.checkInputName(requestProductDTO.getRequestProductName())) {
            throw new AppException(ErrorCode.INVALID_FORMAT_NAME);
        }
//        if (requestProductRepository.countByRequestProductName(requestProductDTO.getRequestProductName()) > 0) {
//            throw new AppException(ErrorCode.NAME_EXIST);
//        }
        if (!checkConditionService.checkInputQuantityInt(requestProductDTO.getQuantity())) {
            throw new AppException(ErrorCode.QUANTITY_INVALID);
        }
        if (!checkConditionService.checkInputPrice(requestProductDTO.getPrice())) {
            throw new AppException(ErrorCode.PRICE_INVALID);
        }
        requestProducts = requestProductRepository.save(requestProducts);
//        //set ảnh thumbnail
//        Product_Thumbnail t = uploadImageService.uploadFile_Thumnail(multipartFiles_thumbnal);
//        requestProducts.setImage(t.getFullPath());
        //set ảnh của product
        RequestProducts requestProduct = requestProductRepository.findByName(requestProductDTO.getRequestProductName());
        uploadImageService.uploadFileRequestProduct(multipartFiles, requestProduct.getRequestProductId());
        return requestProducts;
    }



//    @Override
//    public List<Requests> GetAllRequests() {
//        List<Requests> request_list = requestRepository.findAllRequest();
//        if (request_list.isEmpty()) {
//            throw new AppException(ErrorCode.NOT_FOUND);
//        }
//        return request_list;
//    }
//
//    @Override
//    public List<Requests> GetAllRequestsAccept() {
//        List<Requests> request_list = requestRepository.findAllRequestAccept();
//        if (request_list.isEmpty()) {
//            throw new AppException(ErrorCode.NOT_FOUND);
//        }
//        return request_list;
//    }

//    @Override
//    public RequestProductAllDTO GetProductRequestById(int id) {
//        List<Product_Requestimages> productRequestimagesList = productRequestimagesRepository.findById(id);
//        RequestProducts requestProducts = requestProductRepository.findById(id);
//        if (requestProducts == null) {
//            throw new AppException(ErrorCode.NOT_FOUND);
//        }
//        RequestProductAllDTO requestProductAllDTO = new RequestProductAllDTO();
//        requestProductAllDTO.setId(requestProducts.getRequestProductId());
//        requestProductAllDTO.setRequest_id(requestProducts.getRequestProductId());
//        requestProductAllDTO.setQuantity(requestProducts.getQuantity());
//        requestProductAllDTO.setPrice(requestProducts.getPrice());
//        requestProductAllDTO.setCompletionTime(requestProducts.getCompletionTime());
//        requestProductAllDTO.setDescription(requestProducts.getDescription());
//        List<Product_Requestimages> processedImages = new ArrayList<>(); // Danh sách mới
//        for (Product_Requestimages productRequestimages : productRequestimagesList) {
//            productRequestimages.setFullPath(productRequestimages.getFullPath());
//            processedImages.add(productRequestimages); // Thêm vào danh sách mới
//        }
//        requestProductAllDTO.setImagesList(processedImages); // Gán danh sách mới vào DTO
//
//        return requestProductAllDTO;
//    }

//    @Override
//    public RequestAllDTO GetRequestById(int id) {
//        List<Requestimages> requestimagesList = requestimagesRepository.findById(id);
//        Requests requests = requestRepository.findById(id);
//        if (requests == null) {
//            throw new AppException(ErrorCode.NOT_FOUND);
//        }
//        RequestAllDTO requestAllDTO = new RequestAllDTO();
//        requestAllDTO.setRequest_id(requests.getRequestId());
//
//        requestAllDTO.setUser_id(requests.getUser().getUserId());
//        requestAllDTO.setRequestDate(requests.getRequestDate());
//        requestAllDTO.setResponse(requests.getResponse());
//        requestAllDTO.setPhoneNumber(requests.getPhoneNumber());
//        requestAllDTO.setFullname(requests.getFullname());
//        requestAllDTO.setEmail(requests.getEmail());
//        requestAllDTO.setAddress(requests.getAddress());
//        requestAllDTO.setCity_province(requests.getCity_province());
//        requestAllDTO.setStatus_id(requests.getStatus().getStatus_id());
//        requestAllDTO.setStatus_name(requests.getStatus().getStatus_name());
//        requestAllDTO.setDistrict(requests.getDistrict());
//        requestAllDTO.setWards(requests.getWards());
//        requestAllDTO.setDescription(requests.getDescription());
//        List<Requestimages> processedImages = new ArrayList<>(); // Danh sách mới
//        for (Requestimages requestimages : requestimagesList) {
//            requestimages.setFullPath(requestimages.getFullPath());
//            processedImages.add(requestimages); // Thêm vào danh sách mới
//        }
//        requestAllDTO.setImagesList(processedImages); // Gán danh sách mới vào DTO
//
//        return requestAllDTO;
//    }

    @Override
    public List<Orders> GetAllOrder() {
        return orderRepository.findAll();
    }

    @Override
    public List<Orders> FindByNameOrCode(String key) {
        List<Orders> ordersList = orderRepository.findOrderByAddressorCode(key);
        if (ordersList.isEmpty()) {
            throw new AppException(ErrorCode.NOT_FOUND);
        }
        return ordersList;
    }

    @Override
    public List<Orders> FilterByDate(Date from, Date to) {
        List<Orders> ordersList = orderRepository.findByOrderDateBetween(from, to);
        if (ordersList.isEmpty()) {
            throw new AppException(ErrorCode.NOT_FOUND);
        }
        return ordersList;

    }

    @Override
    public List<Orders> FilterByStatus(int status_id) {
        List<Orders> ordersList = orderRepository.filterByStatus(status_id);
        if (ordersList.isEmpty()) {
            throw new AppException(ErrorCode.NOT_FOUND);
        }
        return ordersList;

    }

    @Override
    public List<Orders> HistoryOrder() {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userRepository.getUserByUsername(userDetails.getUsername());
        List<Orders> ordersList = orderRepository.findHistoryOrder(user.getUserId());
        if (ordersList.isEmpty()) {
            throw new AppException(ErrorCode.NOT_FOUND);
        }
        return ordersList;

    }

    @Override
    public Orders getOrderById(int order_id) {
        Orders orders = orderRepository.findById(order_id);
        if (orders == null) {
            throw new AppException(ErrorCode.NOT_FOUND);
        }
        return orders;

    }

    @Override
    public List<Orderdetails> getAllOrderDetail() {
        List<Orderdetails> orderdetailsList = orderDetailRepository.findAll();
        if(orderdetailsList.isEmpty()){
            throw new AppException(ErrorCode.NOT_FOUND);
        }
        return orderdetailsList;
    }

    @Override
    public List<RequestProducts> GetAllProductRequest() {

        return requestProductRepository.findAll();
    }

//    private String getAddressLocalComputer(String imagePath) {
//        int assetsIndex = imagePath.indexOf("/assets/");
//        if (assetsIndex != -1) {
//            imagePath = imagePath.substring(assetsIndex); // Cắt từ "/assets/"
//            if (imagePath.startsWith("/")) { // Kiểm tra xem có dấu "/" ở đầu không
//                imagePath = imagePath.substring(1); // Loại bỏ dấu "/" đầu tiên
//            }
//        }
//        return imagePath;
//    }// Trả về đường dẫn tương đối hoặc đường dẫn ban đầu nếu không tìm thấy "/assets/"

    @Override
    public List<OrderDetailWithJobStatusDTO> getOrderDetailByOrderId(int order_id) {
        List<OrderDetailWithJobStatusDTO> results = orderDetailRepository.getAllOrderDetailByOrderId(order_id);
        if(results.isEmpty()){
            throw new AppException(ErrorCode.NOT_FOUND);
        }
        return results;
    }




//    @Override
//    public Requests CustomerEditRequest(int request_id, Requests requests) {
//        Requests requests1 = requestRepository.findById(request_id);
//        requests1.
//    }

    @Override
    public OrderDetailDTO getOrderDetailById(int id) {
        OrderDetailDTO orderDetailDTO = orderDetailRepository.getOrderDetailById(id);
        if (orderDetailDTO == null) {
            throw new AppException(ErrorCode.NOT_FOUND);
        }
        return orderDetailDTO;
    }

//    @Override
//    public RequestEditCusDTO getRequestEditCusDTOById(int id) {
//        return requestRepository.getRequestEditCusDTOById(id);
//    }

    @Override
    public void deleteRequestById(int requestId) {
        orderRepository.deleteById(requestId);
    }
    @Transactional
    @Override
    public Orders ManagerEditRequest(int request_id, RequestEditDTO requestEditDTO) {
        Orders requests = orderRepository.findById(request_id);
        Status_Order statusRequest = statusOrderRepository.getById(requestEditDTO.getStatus_id());
        requests.setStatus(statusRequest);
        requests.setResponse(requestEditDTO.getResponse());
        orderRepository.save(requests);
        //  entityManager.refresh(requests);
        return requests;
    }

    @Transactional
    @Override
    public String ChangeStatusOrder(int orderId, int status_id) {
        //send mail cho những đơn hàng đặt theo yêu cầu , vì đơn hàng mau có sẵn thì mua luôn rồi, trả tiền luôn r cần đéo gì nữa mà phải theo dõi tình trạng đơn hàng
        orderRepository.UpdateStatusOrder(orderId,status_id);
        Status_Order statusOrder =statusOrderRepository.findById(status_id);
        Orders orders = orderRepository.findById(orderId);
        List<Jobs> list_jobs = jobRepository.getJobByOrderDetailByOrderCode(orders.getCode());
        for(Jobs job : list_jobs){
            if(job.getStatus().getStatus_id() != 13) { //tức là công việc đã hoàn thành
                return "Đơn hàng chưa hoàn thành công việc, không thể sửa trạng thái đơn hàng !";
            }
        }
        if(orders.getSpecialOrder() == true){
            String email=orderDetailRepository.getMailOrderForSendMail(orderId);
            String code = orders.getCode();
            SimpleDateFormat dateFormatter = new SimpleDateFormat("dd/MM/yyyy");
            String time_finish = dateFormatter.format(orders.getOrderFinish());
            String time_start = dateFormatter.format(orders.getOrderDate());
            String status_name=statusOrder.getStatus_name();
            MailBody mailBody = MailBody.builder()
                    .to(email)
                    .text("Đơn hàng có mã đơn hàng là : " + code + "\n" +
                            "Có trạng thái: " + status_name + "\n" +
                            "Với thời gian tạo đơn là: " + time_start + "\n" +
                            "Và thời gian dự kiến hoàn thành là: " + time_finish)
                    .subject("[Thông tin tiến độ của đơn hàng]")
                    .build();
            emailService.sendSimpleMessage(mailBody);
        }
        return "Thay đổi trạng thái đơn hàng thành công";
    }


    @Override
    public List<RequestProductDTO_Show> filterRequestProductsForAdmin(String search,  Integer statusId, BigDecimal minPrice, BigDecimal maxPrice, String sortDirection) {
        List<RequestProducts> productList = new ArrayList<>();
        if (search != null|| statusId != null  || minPrice != null || maxPrice != null) {
            productList = requestProductRepository.filterRequestProductsForAdmin(search, statusId, minPrice, maxPrice);
        } else {
            productList = requestProductRepository.findAll();
        }

        if (productList.isEmpty()) {
            throw new AppException(ErrorCode.NOT_FOUND);
        }

//        for (RequestProducts product : productList) {
//            product.setImage(getAddressLocalComputer(product.getImage())); // Cập nhật lại đường dẫn ảnh
//        }

        // Sắp xếp danh sách sản phẩm theo giá
        if (sortDirection != null) {
            if (sortDirection.equals("asc")) {
                productList.sort(Comparator.comparing(RequestProducts::getPrice));
            } else if (sortDirection.equals("desc")) {
                productList.sort(Comparator.comparing(RequestProducts::getPrice).reversed());
            }
        }
        List<RequestProductDTO_Show> result = new ArrayList<>();
        for (RequestProducts requestProducts : productList) {
            int id = requestProducts.getRequestProductId();
            List<Product_Requestimages> productRequestimagesList = productRequestimagesRepository.findImageByProductId(id);
            RequestProductDTO_Show requestProductDTOShow = new RequestProductDTO_Show();
            requestProductDTOShow.setRe_productId(requestProducts.getRequestProductId());
            requestProductDTOShow.setRe_productName(requestProducts.getRequestProductName());
            requestProductDTOShow.setQuantity(requestProducts.getQuantity());
            requestProductDTOShow.setDescription(requestProducts.getDescription());
            requestProductDTOShow.setPrice(requestProducts.getPrice());
            requestProductDTOShow.setCompletionTime(requestProducts.getCompletionTime());
            requestProductDTOShow.setStatus(requestProducts.getStatus());
            int request_id = requestProducts.getOrders().getOrderId();
            System.out.println(request_id);
            Orders requests= orderRepository.findById(request_id);
            requestProductDTOShow.setRequest_id(request_id);
            requestProductDTOShow.setCode(requests.getCode());
            List<Product_Requestimages> imageList = productRequestimagesList.stream()
                    .map(img -> {
                        img.setFullPath(getAddressLocalComputer(img.getFullPath()));
                        return img;
                    })
                    .toList();
            requestProductDTOShow.setImageList(imageList);
            List<String> subMaterialNames = requestProductsSubmaterialsRepository.GetSubNameByProductId(id);
            requestProductDTOShow.setSub_material_name(subMaterialNames.isEmpty() ? null : subMaterialNames);
            result.add(requestProductDTOShow);
        }


        return result;
    }
    @Override
    public List<RequestProducts> findByPriceRange(BigDecimal min, BigDecimal max) {
        List<RequestProducts> productsList = requestProductRepository.findByPriceRange(min,max);
        if(productsList == null ){
            throw new AppException(ErrorCode.NOT_FOUND);
        }
        return productsList;
    }

    @Override
    public List<RequestProducts> GetAllProductRequestByUserId() {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userRepository.getUserByUsername(userDetails.getUsername());
        List<RequestProducts> list = requestProductRepository.findByUserId(user.getUserId());
        if(list == null ){
            throw new AppException(ErrorCode.NOT_FOUND);
        }
        return list;
    }

    @Override
    public List<Orders> GetAllRequestByUserId() {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userRepository.getUserByUsername(userDetails.getUsername());
        List<Orders> list = orderRepository.findByUserId(user.getUserId());
        if(list == null ){
            throw new AppException(ErrorCode.NOT_FOUND);
        }
        return list;
    }
//    @Override
//    public List<Requests> GetAllRequestByAccountId(int acc_id) {
//        List<Requests> list = requestRepository.findByUserId(acc_id);
//        if(list == null ){
//            throw new AppException(ErrorCode.NOT_FOUND);
//        }
//        return list;
//    }

    @Override
    public RequestProductDTO_Show GetRequestProductByIdWithImage(int id) {
        List<Product_Requestimages> productRequestimagesList = productRequestimagesRepository.findImageByProductId(id);
        RequestProducts requestProducts = requestProductRepository.findById(id);
        if (requestProducts == null) {
            throw new AppException(ErrorCode.NOT_FOUND);
        }
        RequestProductDTO_Show requestProductDTOShow = new RequestProductDTO_Show();
        if (productRequestimagesList == null) {
            requestProductDTOShow.setImageList(null);
        }
        requestProductDTOShow.setRe_productId(requestProducts.getRequestProductId());
        requestProductDTOShow.setRe_productName(requestProducts.getRequestProductName());
        requestProductDTOShow.setDescription(requestProducts.getDescription());
        requestProductDTOShow.setPrice(requestProducts.getPrice());
        requestProductDTOShow.setQuantity(requestProducts.getQuantity());
        requestProductDTOShow.setCompletionTime(requestProducts.getCompletionTime());
        requestProductDTOShow.setStatus(requestProducts.getStatus());
        int request_id = requestProducts.getOrders().getOrderId();
        Orders requests= orderRepository.findById(request_id);
        requestProductDTOShow.setRequest_id(request_id);
        requestProductDTOShow.setCode(requests.getCode());
//        requestProductDTOShow.setRequests(requests);
        List<Product_Requestimages> processedImages = new ArrayList<>(); // Danh sách mới
        for (Product_Requestimages productimages : productRequestimagesList) {
            productimages.setFullPath(getAddressLocalComputer(productimages.getFullPath()));
            processedImages.add(productimages); // Thêm vào danh sách mới
        }
        List<String> list = requestProductsSubmaterialsRepository.GetSubNameByProductId(id);
        if (list.isEmpty()) {
            requestProductDTOShow.setSub_material_name(null);
        }
        requestProductDTOShow.setSub_material_name(list);
        requestProductDTOShow.setImageList(processedImages); // Gán danh sách mới vào DTO

        return requestProductDTOShow;

    }

    @Override
    public List<RequestProductDTO_Show> GetAllRequestProductWithImage() {
        List<RequestProducts> requestProductsList = requestProductRepository.findAll();
        List<RequestProductDTO_Show> result = new ArrayList<>();
        for (RequestProducts requestProducts : requestProductsList) {
            int id = requestProducts.getRequestProductId();
            List<Product_Requestimages> productRequestimagesList = productRequestimagesRepository.findImageByProductId(id);
            RequestProductDTO_Show requestProductDTOShow = new RequestProductDTO_Show();
            requestProductDTOShow.setRe_productId(requestProducts.getRequestProductId());
            requestProductDTOShow.setRe_productName(requestProducts.getRequestProductName());
            requestProductDTOShow.setQuantity(requestProducts.getQuantity());
            requestProductDTOShow.setDescription(requestProducts.getDescription());
            requestProductDTOShow.setPrice(requestProducts.getPrice());
            requestProductDTOShow.setCompletionTime(requestProducts.getCompletionTime());
            requestProductDTOShow.setStatus(requestProducts.getStatus());
            int request_id = requestProducts.getOrders().getOrderId();
            System.out.println(request_id);
            Orders requests= orderRepository.findById(request_id);
            requestProductDTOShow.setRequest_id(request_id);
            requestProductDTOShow.setCode(requests.getCode());
//            requestProductDTOShow.setRequests(requests);
            List<Product_Requestimages> imageList = productRequestimagesList.stream()
                    .map(img -> {
                        img.setFullPath(getAddressLocalComputer(img.getFullPath()));
                        return img;
                    })
                    .toList();
            requestProductDTOShow.setImageList(imageList);
            List<String> subMaterialNames = requestProductsSubmaterialsRepository.GetSubNameByProductId(id);
            requestProductDTOShow.setSub_material_name(subMaterialNames.isEmpty() ? null : subMaterialNames);
            result.add(requestProductDTOShow);
        }

        return result;
    }



    private String getAddressLocalComputer(String imagePath) {
        int assetsIndex = imagePath.indexOf("/assets/");
        if (assetsIndex != -1) {
            imagePath = imagePath.substring(assetsIndex); // Cắt từ "/assets/"
            if (imagePath.startsWith("/")) { // Kiểm tra xem có dấu "/" ở đầu không
                imagePath = imagePath.substring(1); // Loại bỏ dấu "/" đầu tiên
            }
        }
        return imagePath;
    }// Trả về đường dẫn tương đối hoặc đường dẫn ban đầu nếu không tìm thấy "/assets/"



}