package com.example.demo.Repository;

import com.example.demo.Dto.OrderDTO.JobProductDTO;
import com.example.demo.Entity.Orderdetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderDetailRepository extends JpaRepository<Orderdetails,Integer> {

    @Query("SELECT u FROM Orderdetails u WHERE u.order.orderId = :query")
    List<Orderdetails> getOrderDetailByOrderId(int query);

//    @Query("SELECT u FROM Orderdetails u WHERE u.product.productId = :query")
//    List<Orderdetails> getOrderDetailsInByProductId(int query);

    @Query("SELECT od FROM Orderdetails od JOIN FETCH od.requestProduct rp WHERE od.order.orderId = 6") // Thêm điều kiện lọc nếu cần
    List<Orderdetails> getOrderDetailsWithRequestProduct();

//    @Query(value =
//            "SELECT new com.example.demo.Dto.OrderDTO.OrderDetailDTO(ui.code, p.requestProductId, p.requestProductName, p.description, p.price, p.status, u.quantity) " +
//                    "FROM Orderdetails u " +
//                    "INNER JOIN u.order ui " +
//                    "INNER JOIN u.requestProduct p " +
//                    "WHERE u.requestProduct.requestProductId IS NOT NULL ")
//    List<OrderDetailDTO> getRequestProductInOrderDetail();
//






    
}
