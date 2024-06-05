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

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "status_id")
    @JsonIgnore
    private Status status;

    @ManyToOne(cascade = CascadeType.ALL)
    @JsonIgnore
    @JoinColumn(name = "position_id")
    private Position position;

    @Column(name = "hire_date")
    @Temporal(TemporalType.DATE)
    private Date hireDate;

    @ManyToOne(cascade = CascadeType.ALL)
//    @JsonIgnore
    @JsonManagedReference
    @JoinColumn(name = "role_id")
    private Role role;

    @OneToOne(cascade = CascadeType.ALL)// tức là khi tạo User thì infor cũng đc tạo
    @JsonIgnore
    @JoinColumn(name = "infor_id")

    private UserInfor userInfor;
        public User(String username, String password, String email, Status status, Position position, Date hireDate, Role role,UserInfor userInfor) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.status = status;
        this.position = position;
        this.hireDate = hireDate;
        this.role = role;
        this.userInfor=userInfor;
    }
}
