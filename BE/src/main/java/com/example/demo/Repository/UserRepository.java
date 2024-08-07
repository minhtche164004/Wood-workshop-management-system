package com.example.demo.Repository;

import com.example.demo.Dto.UserDTO.UserDTO;
import com.example.demo.Dto.UserDTO.UserUpdateDTO;
import com.example.demo.Entity.Products;
import com.example.demo.Entity.User;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
@EnableJpaRepositories
public interface UserRepository extends JpaRepository<User,Integer> {
    User getUserByUsername(String username);
    @Query("SELECT u FROM User u WHERE u.userInfor.phoneNumber = :phone")
    Optional<User> findByPhone(String phone);

    @Query("SELECT u FROM User u WHERE u.userId = :userId")
    User findByIdCheck(int userId);

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

//    @Transactional
//    @Modifying
//    @Query("update User u set u.password = ?2 where u.username = ?1")
//    void ChangePassword(String username, String password);



    @Transactional
    @Modifying
    @Query("update User u set u.status.status_id = ?2 where u.userId = ?1")
    void editStatus(int userId, int status_id);

    @Query("SELECT u FROM User u WHERE u.position.position_id IS NOT NULL AND u.position.position_id <> 4")
    List<User> findUsersWithPosition();

//    @Query("SELECT u FROM User u WHERE u.position.position_id IS NOT NULL AND ")
//    List<User> CountJobEmployee(@Param("userId") int userId);


//    @Transactional
//    @Modifying
//    @Query("update User u set u.email = ?2 , u.userInfor.address =?3,u.userInfor.fullname=?4,u.userInfor.phoneNumber=?5 ,u.username=?6 where u.userId = ?1")
//    void updateUser(int userId, String email,String address,String fullname,String phoneNumber,String username);


//    @Query("SELECT u FROM User u WHERE u.username LIKE %:username%" )
//    List<User> findByUsername(@Param("username") String username);

//    @Query("SELECT u FROM User u WHERE u.username LIKE %:keyword% OR u.userInfor.address LIKE %:keyword% OR u.userInfor.wards LIKE %:keyword%" +
//            "OR u.userInfor.district LIKE %:keyword% OR u.userInfor.city_province LIKE %:keyword%")
//    List<User> findByUsernameOrAddress(@Param("keyword") String keyword);

    @Query("SELECT u FROM User u JOIN u.userInfor ui WHERE u.username LIKE CONCAT('%', :keyword, '%') OR " +
            "ui.wards LIKE CONCAT('%', :keyword, '%') OR " +
            "ui.district LIKE CONCAT('%', :keyword, '%') OR " +
            "ui.city_province LIKE CONCAT('%', :keyword, '%')")
    List<User> findByUsernameOrAddress(@Param("keyword") String keyword);

    @Query("SELECT u FROM User u WHERE u.status.status_id=?1")
    List<User> FilterByStatus(@Param("status_id") int status_id);

    @Query("SELECT u FROM User u WHERE u.role.roleId=?1")
    List<User> FilterByRole(@Param("roleId") int roleId);

    @Query("SELECT u FROM User u WHERE u.position.position_id=?1")
    List<User> FilterByPosition(@Param("position_id") int position_id);


    @Query(value =
         "SELECT new com.example.demo.Dto.UserDTO.UserUpdateDTO(u.username,ui.address,s.status_name,p.position_name,r.roleName,u.email)" +
                 "FROM User u " +
                 "INNER JOIN u.userInfor ui " +
                 "INNER JOIN u.position p  " +
                 "INNER JOIN u.role r " +
                 "INNER JOIN u.status s" +
                 " WHERE u.userId = :userId")
 Optional<UserUpdateDTO> findByIdTest1(@Param("userId") int userId);

    @Query("SELECT u FROM User u  " +
            " LEFT JOIN u.role r " +
            " LEFT JOIN u.position p WHERE" +
            "(u.userInfor.fullname LIKE %:search% OR :search IS NULL) " +
            "AND (r.roleId = :roleId OR :roleId IS NULL) " +
            "AND (p.position_id = :position_id OR :position_id IS NULL)")
    List<User> MultiFilterUser(@Param("search") String search,
                                        @Param("roleId") Integer roleId,
                                        @Param("position_id") Integer position_id);


//    @Query("SELECT new com.example.demo.Dto.TestDTO(u.username, ui.address) " +
//           "FROM User u INNER JOIN u.userInfor ui " +
//           "WHERE u.userId = :userId")
//    Optional<TestDTO> findByIdTest(@Param("userId") int userId);

    @Query(value="SELECT u FROM User u WHERE u.userId = :userId")
    Optional<User> findById(@Param("userId") int userId);

    @Query(value="SELECT u FROM User u WHERE u.userId = :userId")
    Optional<UserDTO> findById1(@Param("userId") int userId);


    @Query(value="SELECT u FROM User u WHERE u.userId = :userId")
    User findByIdJob(@Param("userId") int userId);




}
