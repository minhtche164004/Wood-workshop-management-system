package com.example.demo.Repository;

import com.example.demo.Dto.TestDTO;
import com.example.demo.Dto.UserUpdateDTO;
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
//@EnableJpaRepositories
public interface UserRepository extends JpaRepository<User,Integer> {
    User getUserByEmail(String email);
    Optional<User> findByEmail(String email);
    List<User> findAll();
    @Transactional
    @Modifying
    @Query("update User u set u.password = ?2 where u.email = ?1")
    void updatePassword(String email, String password);

    @Query("SELECT u FROM User u WHERE u.username LIKE %:username%" )
    List<User> findByUsername(@Param("username") String username);

    @Query("SELECT u FROM User u WHERE u.username LIKE %:keyword% OR u.userInfor.address LIKE %:keyword%")
    List<User> findByUsernameOrAddress(@Param("keyword") String keyword);


 @Query(value =
         "SELECT " +
                 "u.username AS username, " +
                 "ui.address AS address, " +
                 "s.status_name AS status, " +
                 "p.position_name AS position, " +
                 "r.role_name AS role, " +
                 "u.email AS email " +
                 "FROM users u " +
                 "INNER JOIN information_user ui ON u.infor_id = ui.infor_id " +
                 "INNER JOIN positions p ON u.position_id = p.position_id " +
                 "INNER JOIN roles r ON u.role_id = r.role_id " +
                 "INNER JOIN status s ON u.status_id = s.status_id " +
                 "WHERE u.user_id = :userId",
         nativeQuery = true)
 UserUpdateDTO findUserById(int userId);

@Transactional
@Query(value = """
    SELECT u.username AS username, ui.address AS address
    FROM users u 
    INNER JOIN information_user ui ON u.infor_id = ui.infor_id 
    WHERE u.user_id = :user_id
    """, nativeQuery = true)
Optional<TestDTO> findByIdTest(@Param("user_id") int user_id);

}
