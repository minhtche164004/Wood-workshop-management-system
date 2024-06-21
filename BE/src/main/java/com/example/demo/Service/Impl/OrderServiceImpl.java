package com.example.demo.Service.Impl;

import com.example.demo.Dto.RequestDTO.RequestAllDTO;
import com.example.demo.Dto.RequestDTO.RequestUpdateDTO;
import com.example.demo.Dto.ProductDTO.RequestProductAllDTO;
import com.example.demo.Dto.ProductDTO.RequestProductDTO;
import com.example.demo.Dto.OrderDTO.ProductItem;
import com.example.demo.Dto.RequestDTO.RequestDTO;
import com.example.demo.Dto.OrderDTO.RequestOrder;
import com.example.demo.Entity.*;
import com.example.demo.Exception.AppException;
import com.example.demo.Exception.ErrorCode;
import com.example.demo.Repository.*;
import com.example.demo.Service.CheckConditionService;
import com.example.demo.Service.OrderService;
import com.example.demo.Service.ProductService;
import com.example.demo.Service.UploadImageService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

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
    @Override
    public Orders AddOrder(RequestOrder requestOrder) {
        LocalDate currentDate = LocalDate.now();
        java.sql.Date sqlCompletionTime = java.sql.Date.valueOf(currentDate); // Chuyển đổi sang java.sql.Date

        Orders orders = new Orders();
        orders.setOrderDate(sqlCompletionTime);
        Status_Order statusOrder = statusOrderRepository.findById(4);//tự set cho nó là 1
        orders.setStatus(statusOrder);
        orders.setPaymentMethod(requestOrder.getPayment_method()); //1 là trả tiền trực tiếp, 2 là chuyển khoản
        orders.setAddress(requestOrder.getCusInfo().getAddress());
        orders.setFullname(requestOrder.getCusInfo().getFullname());
        orders.setPhoneNumber(requestOrder.getCusInfo().getPhone());
        //day la` dia chi nhan hang cua khach hang
        orders.setCity_province(requestOrder.getCusInfo().getAddress());
        orders.setDistrict(requestOrder.getCusInfo().getAddress());
        orders.setWards(requestOrder.getCusInfo().getAddress());
        //
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
                        if(orderdetail.getRequestProduct().getQuantity() < item.getQuantity()){
                            throw new AppException(ErrorCode.OUT_OF_STOCK);
                        }
                        requestProducts.setQuantity(requestProducts.getQuantity() - item.getQuantity());
                        requestProductRepository.save(requestProducts);
                        orderdetail.setProduct(null); //set product null
                        BigDecimal itemPrice = item.getPrice();
                        BigDecimal itemQuantity = BigDecimal.valueOf(item.getQuantity());
                        total = total.add(itemPrice.multiply(itemQuantity)); // Cộng dồn vào total
                        orderDetailRepository.save(orderdetail);
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
    public Requests AddNewRequest(RequestDTO requestDTO) {
        Requests requests = new Requests();
        UserDetails userDetails =(UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user =userRepository.getUserByUsername(userDetails.getUsername());
        //lấy thông tin thằng đang login
        //   User user = userRepository.findById(requestDTO.getUser_id()).get();
        requests.setUser(user);
        Status_Request statusRequest =statusRequestRepository.findById(1).get();//nghĩa là request đang chờ phê duyệt
        requests.setRequestDate(requestDTO.getRequestDate());
        requests.setDescription(requestDTO.getDescription());
        requests.setStatus(statusRequest);
        requests.setAddress(requestDTO.getAddress());
        requests.setFullname(requestDTO.getFullname());
        requests.setPhoneNumber(requestDTO.getPhoneNumber());
        requests.setResponse("");
        requests.setCity_province(requestDTO.getCity_province());
        requests.setDistrict(requestDTO.getDistrict());
        requests.setWards(requestDTO.getWards());
        LocalDate today = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("ddMMyy");
        String dateString = today.format(formatter);

        Requests lastRequest = requestRepository.findRequestTop(dateString + "RQ");
        int count = lastRequest != null ? Integer.parseInt(lastRequest.getCode().substring(8)) + 1 : 1;
        String code = dateString + "RQ" + String.format("%03d", count);
        requests.setCode(code);

        if (!checkConditionService.checkInputName(requestDTO.getFullname())) {
            throw new AppException(ErrorCode.INVALID_FORMAT_NAME);
        }

        requests = requestRepository.save(requests);
        uploadImageService.uploadFile_Request(requestDTO.getFiles(), requests.getRequestId());
        return requests;
    }
    @Override
    public Requests getRequestById(int id){
        return requestRepository.findById(id);
    }
    @Override
    public RequestProducts getRequestProductsById(int id){
        return requestProductRepository.findById(id);
    }
    @Transactional
    @Override
    public void Approve_Reject_Request(int id, int status_id){
        requestRepository.updateStatus(id,status_id);
    }
    //Tạo Request Product
    @Override
    public RequestProducts AddNewProductRequest(RequestProductDTO requestProductDTO) { //lấy từ request
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
        if (!checkConditionService.checkInputQuantity(requestProductDTO.getQuantity())) {
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
        uploadImageService.uploadFile_RequestProduct(requestProductDTO.getFiles(), requestProduct.getRequestProductId());
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
        if(requestProducts == null){
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
        for(Product_Requestimages productRequestimages : productRequestimagesList){
            productRequestimages.setFullPath(getAddressLocalComputer(productRequestimages.getFullPath()));
            processedImages.add(productRequestimages); // Thêm vào danh sách mới
        }
        requestProductAllDTO.setImagesList(processedImages); // Gán danh sách mới vào DTO

        return requestProductAllDTO;
    }

    @Override
    public RequestAllDTO GetRequestById(int id) {
        List<Requestimages> requestimagesList = requestimagesRepository.findById(id);
        Requests requests = requestRepository.findById(id);
        if(requests == null){
            throw new AppException(ErrorCode.NOT_FOUND);
        }
        RequestAllDTO requestAllDTO = new RequestAllDTO();
        requestAllDTO.setUser_id(requests.getUser().getUserId());
        requestAllDTO.setRequestDate(requests.getRequestDate());
        requestAllDTO.setResponse(requests.getResponse());
        requestAllDTO.setPhoneNumber(requests.getPhoneNumber());
        requestAllDTO.setFullname(requests.getFullname());
        requestAllDTO.setAddress(requests.getAddress());
        requestAllDTO.setCity_province(requests.getCity_province());
        requestAllDTO.setStatus_id(requests.getStatus().getStatus_id());
        requestAllDTO.setDistrict(requests.getDistrict());
        requestAllDTO.setWards(requests.getWards());
        requestAllDTO.setDescription(requests.getDescription());
        List<Requestimages> processedImages = new ArrayList<>(); // Danh sách mới
        for(Requestimages requestimages : requestimagesList){
            requestimages.setFullPath(getAddressLocalComputer(requestimages.getFullPath()));
            processedImages.add(requestimages); // Thêm vào danh sách mới
        }
        requestAllDTO.setImagesList(processedImages); // Gán danh sách mới vào DTO

        return requestAllDTO;
    }
    @Override
    public List<RequestProducts> GetAllProductRequest() {
        return requestProductRepository.findAll();
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


