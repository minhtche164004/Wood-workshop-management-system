package com.example.demo.Service.Impl;

import com.example.demo.Dto.OrderDTO.OrderDetailDTO;
import com.example.demo.Dto.OrderDTO.OrderDetailWithJobStatusDTO;
import com.example.demo.Dto.ProductDTO.Product_Thumbnail;
import com.example.demo.Dto.RequestDTO.RequestAllDTO;
import com.example.demo.Dto.RequestDTO.RequestEditDTO;
import com.example.demo.Dto.RequestDTO.RequestUpdateDTO;
import com.example.demo.Dto.ProductDTO.RequestProductAllDTO;
import com.example.demo.Dto.ProductDTO.RequestProductDTO;
import com.example.demo.Dto.OrderDTO.ProductItem;
import com.example.demo.Dto.RequestDTO.RequestDTO;
import com.example.demo.Dto.OrderDTO.RequestOrder;
import com.example.demo.Entity.*;
import com.example.demo.Exception.AppException;
import com.example.demo.Exception.ErrorCode;
import com.example.demo.Mail.EmailService;
import com.example.demo.Mail.MailBody;
import com.example.demo.Repository.*;
import com.example.demo.Service.*;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
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
public class OrderServiceImpl implements OrderService {
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
    @Autowired
    private Status_Request_Repository statusRequestRepository;
    @Autowired
    private UploadImageService uploadImageService;
    @Autowired
    private RequestProductRepository requestProductRepository;
    @Autowired
    private RequestRepository requestRepository;
    @Autowired
    private Product_RequestimagesRepository productRequestimagesRepository;
    @Autowired
    private RequestimagesRepository requestimagesRepository;
    @Autowired
    private OrderDetailRepository orderDetailRepository;
    @Autowired
    private Status_Product_Repository statusProduct;
    @Autowired
    private JobRepository jobRepository;
    @Autowired
    private Status_Product_Repository status_Product_Repository;
    @Autowired
    private Status_Job_Repository statusJobRepository;
    @Autowired
    private EntityManager entityManager;
    @Autowired
    CloudinaryService cloudinaryService;
    @Autowired
    private EmailService emailService;



    @Override
    public Orders AddOrder(RequestOrder requestOrder) {
        LocalDate currentDate = LocalDate.now();
        java.sql.Date sqlCompletionTime = java.sql.Date.valueOf(currentDate); // Chuyển đổi sang java.sql.Date

        Orders orders = new Orders();
        orders.setOrderDate(sqlCompletionTime);
        orders.setOrderFinish(requestOrder.getOrderFinish());
        Status_Order statusOrder = statusOrderRepository.findById(1);//tự set cho nó là 1
        orders.setStatus(statusOrder);
        orders.setPaymentMethod(requestOrder.getPayment_method()); //1 là trả tiền trực tiếp, 2 là chuyển khoản
        orders.setAddress(requestOrder.getCusInfo().getAddress());
        orders.setFullname(requestOrder.getCusInfo().getFullname());
        orders.setPhoneNumber(requestOrder.getCusInfo().getPhone());
        //day la` dia chi nhan hang cua khach hang
        orders.setCity_province(requestOrder.getCusInfo().getCity_province());
        orders.setDistrict(requestOrder.getCusInfo().getDistrict());
        orders.setWards(requestOrder.getCusInfo().getWards());
        //luu thong tin nguoi dat
        UserInfor userInfor = new UserInfor();
        userInfor.setFullname(requestOrder.getCusInfo().getFullname());
        userInfor.setAddress(requestOrder.getCusInfo().getAddress());
        userInfor.setPhoneNumber(requestOrder.getCusInfo().getPhone());

        informationUserRepository.save(userInfor);
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
                    if(orderdetail.getProduct().getQuantity() < item.getQuantity()){
                        throw new AppException(ErrorCode.OUT_OF_STOCK);
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
        if (requestOrder.getSpecial_order() == 1) {//là hàng có sẵn

            BigDecimal total = BigDecimal.ZERO; // Khởi tạo total là 0
            List<ProductItem> requestProductItems = requestOrder.getOrderDetail().getProductItems();

            // Kiểm tra nếu danh sách sản phẩm không rỗng
            if (requestProductItems != null && !requestProductItems.isEmpty()) {
                for (ProductItem item : requestProductItems) { // Duyệt qua từng sản phẩm
                    RequestProducts requestProducts = requestProductRepository.findById(item.getId());
                    Orderdetails orderdetail= new Orderdetails();
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

    //Tạo Request
    //Tạo Request Product
    @Override
    public Requests AddNewRequest(RequestDTO requestDTO, MultipartFile[] multipartFiles) {
        Requests requests = new Requests();
        UserDetails userDetails =(UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user =userRepository.getUserByUsername(userDetails.getUsername());
        //lấy thông tin thằng đang login
        //   User user = userRepository.findById(requestDTO.getUser_id()).get();
        LocalDate today = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("ddMMyy");
        String dateString = today.format(formatter);
        Date requestDate = Date.from(today.atStartOfDay(ZoneId.systemDefault()).toInstant());
        requests.setUser(user);
        Status_Request statusRequest =statusRequestRepository.findById(1).get();//nghĩa là request đang chờ phê duyệt
        requests.setRequestDate(requestDate); //lấy time hiện tại
        requests.setDescription(requestDTO.getDescription());
        requests.setStatus(statusRequest);
        requests.setAddress(requestDTO.getAddress());
        requests.setEmail(requestDTO.getEmail());
        requests.setFullname(requestDTO.getFullname());
        requests.setPhoneNumber(requestDTO.getPhoneNumber());
        requests.setResponse("");
        requests.setCity_province(requestDTO.getCity_province());
        requests.setDistrict(requestDTO.getDistrict_province());
        requests.setWards(requestDTO.getWards_province());

        Requests lastRequest = requestRepository.findRequestTop(dateString + "RQ");
        int count = lastRequest != null ? Integer.parseInt(lastRequest.getCode().substring(8)) + 1 : 1;
        String code = dateString + "RQ" + String.format("%03d", count);
        requests.setCode(code);
        if (!checkConditionService.checkInputName(requestDTO.getFullname())) {
            throw new AppException(ErrorCode.INVALID_FORMAT_NAME);
        }

        requests = requestRepository.save(requests);
        uploadImageService.uploadFileRequest(multipartFiles, requests.getRequestId());

        return requests;
    }

//    @Override
//    public Requests EditRequest(int request_id,RequestEditDTO requestEditDTO,MultipartFile[] multipartFiles) throws IOException {
//        Requests requests = requestRepository.findById(request_id);
//        Date today = new Date();
//        if (multipartFiles != null &&
//                Arrays.stream(multipartFiles).anyMatch(file -> file != null && !file.isEmpty())) {
//            List<Requestimages> requestimagesList= requestimagesRepository.findRequestImageByRequestId(request_id);
//            for(Requestimages requestimages : requestimagesList){
//                String full_path= requestimages.getFullPath();
//                String id_image =cloudinaryService.extractPublicIdFromUrl(full_path);
//                cloudinaryService.deleteImage(id_image);
//            }
//            requestimagesRepository.deleteRequestImages(request_id); // Xóa những ảnh trước đó
//            uploadImageService.uploadFile(multipartFiles, requests.getRequestId());
//        }
//        requestRepository.updateRequest(request_id,
//                requestEditDTO.getDescription(),
//                today
//        );
//        entityManager.refresh(requests); // Làm mới đối tượng products
//        return requests;
//    }
    @Override
    public Requests getRequestById(int id) {
        return requestRepository.findById(id);
    }

    @Override
    public RequestProducts getRequestProductsById(int id) {
        return requestProductRepository.findById(id);
    }

    @Transactional
    @Override
    public void Approve_Reject_Request(int id, int status_id) {
        requestRepository.updateStatus(id, status_id);
    }

    //Tạo Request Product
    @Override
    public RequestProducts AddNewProductRequest(RequestProductDTO requestProductDTO, MultipartFile[] multipartFiles) { //lấy từ request

        RequestProducts requestProducts = new RequestProducts();
        requestProducts.setRequestProductName(requestProductDTO.getRequestProductName());
        requestProducts.setDescription(requestProductDTO.getDescription());
        requestProducts.setPrice(requestProductDTO.getPrice());
        requestProducts.setQuantity(requestProductDTO.getQuantity());
        requestProducts.setCompletionTime(requestProductDTO.getCompletionTime());
        Requests requests = requestRepository.findById(requestProductDTO.getRequest_id());
        requestProducts.setRequests(requests);
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



    @Override
    public List<Requests> GetAllRequests() {
        List<Requests> request_list = requestRepository.findAll();
        if (request_list.isEmpty()) {
            throw new AppException(ErrorCode.NOT_FOUND);
        }
        return request_list;
    }

    @Override
    public RequestProductAllDTO GetProductRequestById(int id) {
        List<Product_Requestimages> productRequestimagesList = productRequestimagesRepository.findById(id);
        RequestProducts requestProducts = requestProductRepository.findById(id);
        if (requestProducts == null) {
            throw new AppException(ErrorCode.NOT_FOUND);
        }
        RequestProductAllDTO requestProductAllDTO = new RequestProductAllDTO();
        requestProductAllDTO.setId(requestProducts.getRequestProductId());
        requestProductAllDTO.setRequest_id(requestProducts.getRequestProductId());
        requestProductAllDTO.setQuantity(requestProducts.getQuantity());
        requestProductAllDTO.setPrice(requestProducts.getPrice());
        requestProductAllDTO.setCompletionTime(requestProducts.getCompletionTime());
        requestProductAllDTO.setDescription(requestProducts.getDescription());
        List<Product_Requestimages> processedImages = new ArrayList<>(); // Danh sách mới
        for (Product_Requestimages productRequestimages : productRequestimagesList) {
            productRequestimages.setFullPath(productRequestimages.getFullPath());
            processedImages.add(productRequestimages); // Thêm vào danh sách mới
        }
        requestProductAllDTO.setImagesList(processedImages); // Gán danh sách mới vào DTO

        return requestProductAllDTO;
    }

    @Override
    public RequestAllDTO GetRequestById(int id) {
        List<Requestimages> requestimagesList = requestimagesRepository.findById(id);
        Requests requests = requestRepository.findById(id);
        if (requests == null) {
            throw new AppException(ErrorCode.NOT_FOUND);
        }
        RequestAllDTO requestAllDTO = new RequestAllDTO();
        requestAllDTO.setUser_id(requests.getUser().getUserId());
        requestAllDTO.setRequestDate(requests.getRequestDate());
        requestAllDTO.setResponse(requests.getResponse());
        requestAllDTO.setPhoneNumber(requests.getPhoneNumber());
        requestAllDTO.setFullname(requests.getFullname());
        requestAllDTO.setEmail(requests.getEmail());
        requestAllDTO.setAddress(requests.getAddress());
        requestAllDTO.setCity_province(requests.getCity_province());
        requestAllDTO.setStatus_id(requests.getStatus().getStatus_id());
        requestAllDTO.setDistrict(requests.getDistrict());
        requestAllDTO.setWards(requests.getWards());
        requestAllDTO.setDescription(requests.getDescription());
        List<Requestimages> processedImages = new ArrayList<>(); // Danh sách mới
        for (Requestimages requestimages : requestimagesList) {
            requestimages.setFullPath(requestimages.getFullPath());
            processedImages.add(requestimages); // Thêm vào danh sách mới
        }
        requestAllDTO.setImagesList(processedImages); // Gán danh sách mới vào DTO

        return requestAllDTO;
    }

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


    @Transactional
    @Override
    public Requests ManagerEditRequest(int request_id, RequestEditDTO requestEditDTO) {
        Requests requests = requestRepository.findById(request_id);
        Status_Request statusRequest = statusRequestRepository.getById(requestEditDTO.getStatus_id());
        requests.setStatus(statusRequest);
        requests.setResponse(requestEditDTO.getResponse());
        requestRepository.save(requests);
      //  entityManager.refresh(requests);
        return requests;
    }

    @Override
    public OrderDetailDTO getOrderDetailById(int id) {
        OrderDetailDTO orderDetailDTO = orderDetailRepository.getOrderDetailById(id);
        if (orderDetailDTO == null) {
            throw new AppException(ErrorCode.NOT_FOUND);
        }
        return orderDetailDTO;
    }

    @Override
    public void deleteRequestById(int requestId) {
        requestRepository.deleteById(requestId);
    }

    @Override
    public void ChangeStatusOrder(int orderId, int status_id) {
        //send mail cho những đơn hàng đặt theo yêu cầu , vì đơn hàng mau có sẵn thì mua luôn rồi, trả tiền luôn r cần đéo gì nữa mà phải theo dõi tình trạng đơn hàng
        orderRepository.UpdateStatusOrder(orderId,status_id);
        Status_Order statusOrder =statusOrderRepository.findById(status_id);
        Orders orders = orderRepository.findById(orderId);
        if(orders.getSpecialOrder() == true){
            String email=orderDetailRepository.getOrderDetailsByOrderIdForSendMail(orderId);
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
    }


    @Override
    public List<RequestProducts> filterRequestProductsForAdmin(String search,  Integer statusId, BigDecimal minPrice, BigDecimal maxPrice, String sortDirection) {
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


        return productList;
    }
    @Override
    public List<RequestProducts> findByPriceRange(BigDecimal min, BigDecimal max) {
        List<RequestProducts> productsList = requestProductRepository.findByPriceRange(min,max);
        if(productsList == null ){
            throw new AppException(ErrorCode.NOT_FOUND);
        }
        return productsList;
    }


}