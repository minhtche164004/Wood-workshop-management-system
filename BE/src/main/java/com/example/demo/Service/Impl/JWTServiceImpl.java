package com.example.demo.Service.Impl;

import com.example.demo.Service.JWTService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

@Service
public class JWTServiceImpl implements JWTService {

    @Value("${jwt.time}")
    private int jwtTime;
    //Tạo mã JWT từ thông tin người dùng. Mã JWT được tạo bằng
    // cách sử dụng thư viện JSON Web Token (io.jsonwebtoken.Jwts) và ký bằng một khóa bí mật.
    public String generateToken(Map<String, Object> claims, UserDetails userDetails){
        Collection<? extends GrantedAuthority> authorities = userDetails.getAuthorities();
        List<String> roles = authorities.stream()
                .map(GrantedAuthority::getAuthority).toList();


        claims.put("roles", roles); // Thêm danh sách quyền vào claims
        // extraClaims.put("roles", userDetails.getAuthorities());
        return Jwts.builder().setClaims(claims).setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
             //   .setExpiration(new Date(System.currentTimeMillis() + 1000*60*24))
                .setExpiration(new Date(System.currentTimeMillis() + jwtTime))
                .signWith(getSiginKey(), SignatureAlgorithm.HS256)
                .compact();
    }
//    public String generateRefreshToken(Map<String,Object> extraClaims, UserDetails userDetails){
//        return Jwts.builder().setClaims(extraClaims).setSubject(userDetails.getUsername())
//                .setIssuedAt(new Date(System.currentTimeMillis()))
//                .setExpiration(new Date(System.currentTimeMillis() + 1000*60*60))
//                .signWith(getSiginKey(), SignatureAlgorithm.HS256)
//                .compact();
//    }
    //Trích xuất tên người dùng từ mã JWT.
    public String extractUserName(String token){

        return extractClaim(token, Claims::getSubject);
    }
    private <T> T extractClaim(String token, Function<Claims,T> claimsResolvers){
        final Claims claims=extractAllClaims(token);
        return claimsResolvers.apply(claims);
    }

    @Value("${jwt.key}")
    private String jwtKey;

    private Key getSiginKey() {
        byte[] key= Decoders.BASE64.decode(jwtKey);
        return Keys.hmacShaKeyFor(key);
    }
    private Claims extractAllClaims(String token){
        return Jwts.parserBuilder().setSigningKey(getSiginKey()).build().parseClaimsJws(token).getBody();
    }
    //Kiểm tra tính hợp lệ của mã JWT bằng cách kiểm tra xem người dùng từ mã JWT có trùng khớp
    // với người dùng được cung cấp hay không và xem mã JWT đã hết hạn chưa.
    public boolean isTokenValid(String token,UserDetails userDetails){
        final String username= extractUserName(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpried(token));
//nếu đúng thì trả về true
    }
    private boolean isTokenExpried(String token){
        return extractClaim(token,Claims::getExpiration).before(new Date());
    }
}
