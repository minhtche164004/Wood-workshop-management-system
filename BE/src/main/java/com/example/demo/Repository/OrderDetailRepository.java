package com.example.demo.Repository;

import com.example.demo.Dto.JobDTO.JobDoneDTO;
import com.example.demo.Dto.OrderDTO.JobProductDTO;
import com.example.demo.Dto.OrderDTO.OrderDetailDTO;
import com.example.demo.Dto.OrderDTO.OrderDetailWithJobStatusDTO;
import com.example.demo.Entity.Orderdetails;
import com.example.demo.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderDetailRepository extends JpaRepository<Orderdetails, Integer> {

    @Query("SELECT u FROM Orderdetails u WHERE u.order.orderId = :query")
    List<Orderdetails> getOrderDetailByOrderId(int query);

    @Query("SELECT u FROM Orderdetails u WHERE u.product.productId = :query")
    List<Orderdetails> getOrderDetailByProductId(int query);
    @Query("SELECT u FROM Orderdetails u WHERE u.requestProduct.requestProductId = :query")
    List<Orderdetails> getOrderDetailByRequestProductId(int query);

//    @Query("SELECT u FROM Orderdetails u WHERE u.orderDetailId = :query")
//    Orderdetails getOrderDetailById(int query);


    @Query("SELECT new com.example.demo.Dto.OrderDTO.OrderDetailDTO(" +
            "od.orderDetailId, " +
            "COALESCE(p.productId, 0), " +
            "COALESCE(p.productName, ''), " +
            "COALESCE(rp.requestProductId, 0), " +
            "COALESCE(rp.requestProductName, ''), " +
            "od.unitPrice, " +
            "od.quantity," +
            "od.order " +
            ") " +
            "FROM Orderdetails od " +
            "LEFT JOIN od.product p " +
            "LEFT JOIN od.requestProduct rp " +
            "WHERE od.orderDetailId = :query")
    OrderDetailDTO getOrderDetailById(int query);



    @Query("SELECT r.userInfor.user.email FROM Orderdetails od " +
            "LEFT JOIN od.requestProduct rp " +
            "LEFT JOIN rp.orders r " +
            "WHERE od.order.orderId = :orderId")
    String getOrderDetailsByOrderIdForSendMail(@Param("orderId") int orderId);

    @Query("SELECT od.userInfor.user.email FROM Orders od " +
            "WHERE od.orderId = :orderId")
    String getMailOrderForSendMail(@Param("orderId") int orderId);




    @Query("SELECT new com.example.demo.Dto.OrderDTO.OrderDetailWithJobStatusDTO(" +
            "od.orderDetailId, " +
            "COALESCE(p.productId, 0), " +
            "COALESCE(p.productName, ''), " +
            "COALESCE(rp.requestProductId, 0), " +
            "COALESCE(rp.requestProductName, ''), " +
            "od.unitPrice, " +
            "COALESCE(j.status.status_id, 0), " + // Sửa lại thành j.status.statusId
            "COALESCE(s.status_name, ''), " +  // Sửa lại thành s.statusName
            "od.quantity " +
            ") " +
            "FROM Orderdetails od " +
            "LEFT JOIN od.product p " +
            "LEFT JOIN od.requestProduct rp " +
            "LEFT JOIN od.jobs j " +
            "LEFT JOIN j.status s " + // Thêm LEFT JOIN với status_job
            "WHERE od.order.orderId = :query")
    List<OrderDetailWithJobStatusDTO> getAllOrderDetailByOrderId(int query);


    @Query("SELECT new com.example.demo.Dto.OrderDTO.OrderDetailWithJobStatusDTO(" +
            "od.orderDetailId, " +
            "COALESCE(p.productId, 0), " +
            "COALESCE(p.productName, ''), " +
            "COALESCE(rp.requestProductId, 0), " +
            "COALESCE(rp.requestProductName, ''), " +
            "od.unitPrice, " +
            "COALESCE(j.status.status_id, 0), " + // Sửa lại thành j.status.statusId
            "COALESCE(s.status_name, ''), " +  // Sửa lại thành s.statusName
            "od.quantity " +
            ") " +
            "FROM Orderdetails od " +
            "LEFT JOIN od.product p " +
            "LEFT JOIN od.requestProduct rp " +
            "LEFT JOIN od.jobs j " +
            "LEFT JOIN j.status s " + // Thêm LEFT JOIN với status_job
            "WHERE od.order.orderId = :query AND (j.status.status_id = 15 OR j.status.status_id = 13)")
    List<OrderDetailWithJobStatusDTO> getAllOrderDetailByOrderIdCheck(int query);


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