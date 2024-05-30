package com.example.demo.Repository;


import com.example.demo.Entity.ForgotPassword;
import com.example.demo.Entity.User;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ForgotPasswordRepository extends JpaRepository<ForgotPassword,Integer> {

    @Query("select fp from ForgotPassword fp where fp.otp= ?1 and fp.user= ?2")
    Optional<ForgotPassword> findByOtpAndUser(Integer otp, User user);

    @Transactional
    @Modifying
    @Query("DELETE FROM ForgotPassword u WHERE u.fpid = ?1")
    void deleteById(Integer fid);
}