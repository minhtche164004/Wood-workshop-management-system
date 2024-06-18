package com.example.demo.Service.Impl;

import com.example.demo.Dto.ProductItem;
import com.example.demo.Dto.RequestOrder;
import com.example.demo.Dto.RequestProductItem;
import com.example.demo.Entity.Orders;
import com.example.demo.Entity.Products;
import com.example.demo.Entity.Status_Order;
import com.example.demo.Entity.UserInfor;
import com.example.demo.Repository.InformationUserRepository;
import com.example.demo.Repository.OrderRepository;
import com.example.demo.Repository.Status_Order_Repository;
import com.example.demo.Service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {
    @Autowired
    private Status_Order_Repository statusOrderRepository;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private InformationUserRepository informationUserRepository;

    @Override
    public Orders AddOrder(RequestOrder requestOrder) {
        LocalDate currentDate = LocalDate.now();
        java.sql.Date sqlCompletionTime = java.sql.Date.valueOf(currentDate); // Chuyển đổi sang java.sql.Date

        Orders orders = new Orders();
        orders.setOrderDate(sqlCompletionTime);
        Status_Order statusOrder = statusOrderRepository.findById(1);//tự set cho nó là 1
        orders.setStatus(statusOrder);
        orders.setPaymentMethod(requestOrder.getPayment_method()); //1 là trả tiền trực tiếp, 2 là chuyển khoản
        orders.setAddress(requestOrder.getCusInfo().getAddress());
        orders.setFullname(requestOrder.getCusInfo().getFullname());
        orders.setPhoneNumber(requestOrder.getCusInfo().getPhone());

        UserInfor userInfor = new UserInfor();
        userInfor.setFullname(requestOrder.getCusInfo().getFullname());
        userInfor.setAddress(requestOrder.getCusInfo().getAddress());
        userInfor.setPhoneNumber(requestOrder.getCusInfo().getPhone());


        if (requestOrder.getSpecial_order() == 0) { // là hàng có sẵn
            BigDecimal total = BigDecimal.ZERO; // Khởi tạo total là 0
            List<ProductItem> productItems = requestOrder.getOderDetail().getProductItems(); // Lấy danh sách sản phẩm

            // Kiểm tra nếu danh sách sản phẩm không rỗng
            if (productItems != null && !productItems.isEmpty()) {
                for (ProductItem item : productItems) { // Duyệt qua từng sản phẩm
                    BigDecimal itemPrice = item.getPrice();
                    BigDecimal itemQuantity = BigDecimal.valueOf(item.getQuantity());
                    total = total.add(itemPrice.multiply(itemQuantity)); // Cộng dồn vào total
                }
            }
            orders.setDeposite(total.multiply(BigDecimal.valueOf(0.2))); // 20% tiền cọc của tổng tiền đơn hàng
            orders.setTotalAmount(total);
            orders.setSpecialOrder(false);
        }
        if (requestOrder.getSpecial_order() == 1) {//là hàng có sẵn

                BigDecimal total = BigDecimal.ZERO; // Khởi tạo total là 0
                List<RequestProductItem> requestProductItems = requestOrder.getOrderDetailRequest().getRequestProductItems();

                // Kiểm tra nếu danh sách sản phẩm không rỗng
                if (requestProductItems != null && !requestProductItems.isEmpty()) {
                    for (RequestProductItem item : requestProductItems) { // Duyệt qua từng sản phẩm
                        BigDecimal itemPrice = item.getPrice();
                        BigDecimal itemQuantity = BigDecimal.valueOf(item.getQuantity());
                        total = total.add(itemPrice.multiply(itemQuantity)); // Cộng dồn vào total
                    }
                }
            orders.setDeposite(total.multiply(BigDecimal.valueOf(0.2))); // 20% tiền cọc của tổng tiền đơn hàng
                orders.setTotalAmount(total);
                orders.setSpecialOrder(true);
            }
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

            return orders;
        }
    }


