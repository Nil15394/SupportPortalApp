package com.supportportal.filter;

import com.supportportal.constant.SecurityConstant;
import com.supportportal.utility.JWTTokenProvider;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

import static com.supportportal.constant.SecurityConstant.OPTIONS_HTTP_METHOD;
import static com.supportportal.constant.SecurityConstant.TOKEN_PREFIX;
import static org.springframework.http.HttpHeaders.*;

@Component
public class JWTAuthorizationFilter extends OncePerRequestFilter {

    private JWTTokenProvider jwtTokenProvider;

    public JWTAuthorizationFilter(JWTTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        if(request.getMethod().equalsIgnoreCase(OPTIONS_HTTP_METHOD)){
            response.setStatus(HttpStatus.OK.value());
        }else{
            String authorizationHeader = request.getHeader(AUTHORIZATION);
            System.out.println("Token received in Header: " + authorizationHeader);
            if (authorizationHeader == null || !authorizationHeader.startsWith(TOKEN_PREFIX)) {
                filterChain.doFilter(request,response);
                return;
            }else{
                String token = authorizationHeader.substring(TOKEN_PREFIX.length());
                System.out.println("Actual token received : " + token);
                String userName = jwtTokenProvider.getSubject(token);
                if(jwtTokenProvider.isTokenValid(userName, token) && SecurityContextHolder.getContext().getAuthentication() == null){
                    List<GrantedAuthority> authorityList = jwtTokenProvider.getAuthorities(token);
                    Authentication authentication = jwtTokenProvider.getAuthentication(userName, authorityList, request);
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }else{
                    SecurityContextHolder.clearContext();
                }
            }
            filterChain.doFilter(request,response);
        }
    }
}
