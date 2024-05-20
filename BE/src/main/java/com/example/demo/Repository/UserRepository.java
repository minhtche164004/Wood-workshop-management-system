package com.example.demo.Repository;

import com.example.demo.Entity.User;
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
public interface UserRepository extends JpaRepository<User,Integer> {
    User getUserByEmail(String email);

   // @Query("select u from User u WHERE u.email =?1 and u.status= true")
    Optional<User> findByEmail(String email);

//    @Query("select u from User u WHERE u.status= true")
//    Optional<User> CheckActive();

    List<User> findAll();

    @Transactional
    @Modifying
    @Query("update User u set u.password = ?2 where u.email = ?1")
    void updatePassword(String email, String password);

    @Query("SELECT u FROM User u WHERE u.username LIKE %:username%" )
    List<User> findByUsername(@Param("username") String username);

    @Query("SELECT u FROM User u WHERE u.username LIKE %:keyword% OR u.address LIKE %:keyword%")
    List<User> findByUsernameOrAddress(@Param("keyword") String keyword);

    @Query("SELECT u FROM User u WHERE u.userId = :query")
    List<User> filterById(Integer query);
}
