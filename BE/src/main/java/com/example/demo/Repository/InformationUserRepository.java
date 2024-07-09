package com.example.demo.Repository;

import com.example.demo.Dto.ProductDTO.ProductErrorAllDTO;
import com.example.demo.Dto.UserDTO.UserInforDTO;
import com.example.demo.Entity.User;
import com.example.demo.Entity.UserInfor;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@EnableJpaRepositories
public interface InformationUserRepository extends JpaRepository<UserInfor, Integer> {
//    @Query("SELECT u FROM UserInfor u WHERE u.phoneNumber = :phoneNumber and u.has_Account = 1")
//    UserInfor findUsersByPhoneNumber(@Param("phoneNumber") String phoneNumber);

    @Query("SELECT u.phoneNumber FROM UserInfor u WHERE u.has_Account = 1")
    List<String> listPhoneNumberHasAccount();


    @Query("SELECT new com.example.demo.Dto.UserDTO.UserInforDTO(" +
            "u.inforId,u.phoneNumber,u.fullname,u.address,ur.email,u.bank_name,u.bank_number,u.city_province,u.district,u.wards,u.has_Account)" + // Sử dụng COALESCE
            " FROM UserInfor u " +
            " LEFT JOIN u.user ur WHERE u.phoneNumber = :phoneNumber and u.has_Account = 1")
    UserInforDTO findUsersByPhoneNumber(@Param("phoneNumber") String phoneNumber);
}
