package com.example.demo.Entity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import java.util.Date;

@Entity
@Table(name = "users")
@Data
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class User  {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Integer userId;
    @Column(name = "username", nullable = false, length = 255)
    private String username;
    @Column(name = "password", nullable = false, length = 255)
    private String password;

    @Column(name = "email", nullable = false, length = 255)
    private String email;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "status_id")
    @JsonIgnore
    private Status_User status;

    @ManyToOne(cascade = CascadeType.ALL,fetch = FetchType.EAGER)
//    @JsonIgnore
    @JoinColumn(name = "position_id")
    private Position position;

    @Column(name = "hire_date")
    @Temporal(TemporalType.DATE)
    private Date hireDate;

    @ManyToOne(cascade = CascadeType.ALL)
   @JsonIgnore
    @JoinColumn(name = "role_id")
    private Role role;

//    @OneToOne(cascade = CascadeType.ALL,fetch = FetchType.EAGER)// tức là khi tạo User thì infor cũng đc tạo
//    @JsonIgnore
//    @JoinColumn(name = "infor_id")
//    private UserInfor userInfor;

    // Trong lớp User:
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "infor_id")
    @JsonManagedReference
    private UserInfor userInfor;



        public User(String username, String password, String email, Status_User status, Position position, Date hireDate, Role role, UserInfor userInfor) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.status = status;
        this.position = null;//khởi tạo position là null
        this.hireDate = hireDate;
        this.role = role;
        this.userInfor=userInfor;

    }
}


/*
User - Status: Nhiều-Một (@ManyToOne). Một user có một trạng thái (status), một trạng thái có thể áp dụng cho nhiều user.
User - Position: Nhiều-Một (@ManyToOne). Một user có một chức vụ (position), một chức vụ có thể áp dụng cho nhiều user.
User - Role: Nhiều-Một (@ManyToOne). Một user có một vai trò (role), một vai trò có thể áp dụng cho nhiều user.
User - UserInfor: Một-Một (@OneToOne). Một user có một thông tin cá nhân (user information), và thông tin đó chỉ thuộc về user đó.
User - Materials: Nhiều-Nhiều (@ManyToMany). Một user có thể có nhiều vật liệu (materials) và một vật liệu có thể thuộc về nhiều user.



@OneToMany(mappedBy = "user", cascade = CascadeType.ALL) private List<Jobs> jobs;: Thể hiện mối quan hệ một-nhiều với Jobs. Một user có thể thực hiện nhiều job.
*/
