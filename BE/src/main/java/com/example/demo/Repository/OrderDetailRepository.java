package com.example.demo.Repository;

import com.example.demo.Dto.OrderDTO.JobProductDTO;
import com.example.demo.Dto.OrderDTO.OrderDetailWithJobStatusDTO;
import com.example.demo.Entity.Orderdetails;
import com.example.demo.Entity.UserInfor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderDetailRepository extends JpaRepository<Orderdetails, Integer> {

    @Query("SELECT u FROM Orderdetails u WHERE u.order.orderId = :query")
    List<Orderdetails> getOrderDetailByOrderId(int query);

    @Query("SELECT new com.example.demo.Dto.OrderDTO.OrderDetailWithJobStatusDTO(od, j.status, " +
            "(CASE " +
            "   WHEN o.specialOrder = false THEN p.productName " +
            "   WHEN o.specialOrder = true THEN rp.requestProductName " +
            "END)) " +
            "FROM Orderdetails od " +
            "JOIN od.order o " +
            "JOIN Jobs j ON od.orderDetailId = j.orderdetails.orderDetailId " +
            "JOIN Status_Job sj ON j.jobId = sj.status_id " +
            "LEFT JOIN Products p ON od.product.productId = p.productId " +
            "LEFT JOIN RequestProducts rp ON od.requestProduct.requestProductId = rp.requestProductId " +
            "WHERE od.order.orderId = :order_id AND j.job_log = false")
    List<OrderDetailWithJobStatusDTO> getOrderDetailWithJobStatusByOrderId(@Param("order_id") int order_id);
//    @Query("SELECT u FROM Orderdetails u WHERE u.product.productId = :query")
//    List<Orderdetails> getOrderDetailsInByProductId(int query);

//    @Query("SELECT od FROM Orderdetails od JOIN FETCH od.requestProduct rp WHERE od.order.orderId = 6") // Thêm điều kiện lọc nếu cần
//    List<Orderdetails> getOrderDetailsWithRequestProduct();

    //    @Query(value =
//            "SELECT new com.example.demo.Dto.OrderDTO.OrderDetailDTO(ui.code, p.requestProductId, p.requestProductName, p.description, p.price, p.status, u.quantity) " +
//                    "FROM Orderdetails u " +
//                    "INNER JOIN u.order ui " +
//                    "INNER JOIN u.requestProduct p " +
//                    "WHERE u.requestProduct.requestProductId IS NOT NULL ")
//    List<OrderDetailDTO> getRequestProductInOrderDetail();
//
}
