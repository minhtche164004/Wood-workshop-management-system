package com.example.demo.Jwt;

import com.example.demo.Entity.Role;
import com.example.demo.Entity.User;
import lombok.AllArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@AllArgsConstructor
public class UserDetailsImpl implements UserDetails {

    private final User user;
    private final Role role;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(user.getRole().getRoleName().toString()));
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

//    @Override
//    public String getUsername() {
//        return user.getEmail();
//    }
    @Override
    public String getUsername() {
        return user.getUsername();
    }
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}


/*
Dưới đây là một số phương thức quan trọng trong giao diện UserDetails:

getAuthorities(): Trả về một danh sách các quyền (roles) mà người dùng có.
getPassword(): Trả về mật khẩu được mã hóa của người dùng.
getUsername(): Trả về tên người dùng.
isAccountNonExpired(): Xác định xem tài khoản của người dùng có hết hạn không.
isAccountNonLocked(): Xác định xem tài khoản của người dùng có bị khóa không.
isCredentialsNonExpired(): Xác định xem các thông tin xác thực của người dùng có hết hạn không.
isEnabled(): Xác định xem tài khoản của người dùng có được kích hoạt không.
 */