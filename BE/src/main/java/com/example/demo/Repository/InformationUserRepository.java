package com.example.demo.Repository;

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
    @Query("SELECT u FROM UserInfor u WHERE u.phoneNumber = :phoneNumber")
    UserInfor findUsersByPhoneNumber(@Param("phoneNumber") String phoneNumber);

    @Query("SELECT u.phoneNumber FROM UserInfor u")
    List<String> listPhoneNumber();
}
