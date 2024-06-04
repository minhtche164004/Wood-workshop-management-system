package com.example.demo.Jwt;


import com.example.demo.Exception.AppException;
import com.example.demo.Exception.ErrorCode;
import com.example.demo.Service.JWTService;
import com.example.demo.Service.UserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    @Autowired
    private JWTService jwtService;
    @Autowired
    private UserService userService;
    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    //để xác thưc
    /*
    Cụ thể, filter này thực hiện các bước sau:

Lấy mã JWT từ tiêu đề "Authorization" của yêu cầu HTTP.
Nếu không có mã JWT hoặc không bắt đầu bằng "Bearer", filter tiếp tục chuyển tiếp yêu cầu đến filter tiếp theo trong chuỗi.
Nếu có mã JWT hợp lệ, filter sẽ trích xuất tên người dùng từ JWT bằng cách sử dụng JWTService, sau đó kiểm tra xem người dùng có tồn tại trong hệ thống không.
Nếu người dùng tồn tại và mã JWT hợp lệ, filter sẽ xác thực người dùng bằng cách tạo một đối tượng UsernamePasswordAuthenticationToken và đặt nó vào SecurityContextHolder.
Cuối cùng, filter chuyển tiếp yêu cầu đến filter tiếp theo trong chuỗi.
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        final String authHeader=request.getHeader("Authorization");
        final String jwt;
        final String userEmail;
        if(StringUtils.isEmpty(authHeader)|| !org.apache.commons.lang3.StringUtils.startsWith(authHeader,"Bearer")){
            filterChain.doFilter(request,response);
            return;
        }
        jwt=authHeader.substring(7);
        userEmail= jwtService.extractUserName(jwt);
        //check xem nếu trong SecurityContextHolder chưa được xác thực (getAuthentication() == null)
        if(!StringUtils.isEmpty(userEmail) && SecurityContextHolder.getContext().getAuthentication()==null){
            UserDetails userDetails = userDetailsService.loadUserByUsername(userEmail);
            try {
                if(jwtService.isTokenValid(jwt,userDetails)){
                    SecurityContext securityContext=SecurityContextHolder.createEmptyContext();
                    UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
                            userDetails,null,userDetails.getAuthorities()

                    );
                    token.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    securityContext.setAuthentication(token);
                    SecurityContextHolder.setContext(securityContext); //đặt thông tin người dùng vừa đưcọ xác thực vào securityContext
                }
            } catch (AppException e) {
                // Token hết hạn
                throw new AppException(ErrorCode.TOKEN_EXPIRED);
            }
        }
        filterChain.doFilter(request,response);
    }
}
