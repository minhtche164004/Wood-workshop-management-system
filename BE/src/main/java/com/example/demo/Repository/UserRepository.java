package com.example.demo.Repository;

import com.example.demo.Dto.UserDTO.UserDTO;
import com.example.demo.Dto.UserDTO.UserUpdateDTO;
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
    User getUserByUsername(String username);
    int countByEmail(String email);
    int countByUsername(String username);
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);
    List<User> findAll();
    @Modifying
    @Query("DELETE FROM User u WHERE u.userId = ?1") // Assuming 'userId' is the name of your ID field
    void DeleteById(@Param("userId") int userId);
  //  User getUserById(int userId);
    @Transactional
    @Modifying
    @Query("update User u set u.password = ?2 where u.email = ?1")
    void updatePassword(String email, String password);

    @Transactional
    @Modifying
    @Query("update User u set u.email = ?2 , u.userInfor.address =?3,u.userInfor.fullname=?4,u.userInfor.phoneNumber=?5 where u.userId = ?1")
    void updateUser(String userId, String email,String address,String fullname,String phoneNumber);


//    @Query("SELECT u FROM User u WHERE u.username LIKE %:username%" )
//    List<User> findByUsername(@Param("username") String username);

    @Query("SELECT u FROM User u WHERE u.username LIKE %:keyword% OR u.userInfor.address LIKE %:keyword%")
    List<User> findByUsernameOrAddress(@Param("keyword") String keyword);

 @Query(value =
         "SELECT new com.example.demo.Dto.UserDTO.UserUpdateDTO(u.username,ui.address,s.status_name,p.position_name,r.roleName,u.email)" +
                 "FROM User u " +
                 "INNER JOIN u.userInfor ui " +
                 "INNER JOIN u.position p  " +
                 "INNER JOIN u.role r " +
                 "INNER JOIN u.status s" +
                 " WHERE u.userId = :userId")
 Optional<UserUpdateDTO> findByIdTest1(@Param("userId") int userId);


//    @Query("SELECT new com.example.demo.Dto.TestDTO(u.username, ui.address) " +
//           "FROM User u INNER JOIN u.userInfor ui " +
//           "WHERE u.userId = :userId")
//    Optional<TestDTO> findByIdTest(@Param("userId") int userId);

    @Query(value="SELECT u FROM User u WHERE u.userId = :userId")
    Optional<User> findById(@Param("userId") int userId);

    @Query(value="SELECT u FROM User u WHERE u.userId = :userId")
    Optional<UserDTO> findById1(@Param("userId") int userId);

}
