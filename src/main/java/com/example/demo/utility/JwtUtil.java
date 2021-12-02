package com.example.demo.utility;

import com.example.demo.springSecurityDatabase.CustomUserDetails;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@Service
public class JwtUtil {

    private static String secret = "secret";

    //method for extracting user ID from JWT
    public Integer extractUserId(String token) {
        return Integer.parseInt(extractClaim(token, Claims::getSubject));
    }

    //method for extracting Date from JWT
    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    //method for extracting one Claim(email or IP address) from JWT
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    //method for extracting one Claim(email & IP address) from JWT
    private Claims extractAllClaims(String token) {
        return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
    }

    //method for checking does JWT expired
    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    //method for getting IP address of HTTP request
    public static String getRequestRemoteAddress() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes())
                .getRequest();
        System.out.println("IP adresa sa koje dolazi request: " + request.getRemoteAddr());

        return request.getRemoteAddr();
    }

    //method for creating token with subject(user ID) and Claims(email & IP address)
    public String createToken(String email, Integer id) {            //static
        Map<String, Object> claims = new HashMap<>();
        claims.put("email", email);
        claims.put("IP address", getRequestRemoteAddress());

        return Jwts.builder().setClaims(claims).setSubject(String.valueOf(id)).setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 100))  //100h
                .signWith(SignatureAlgorithm.HS256, secret).compact();
    }

    //method for validating token by subject(user ID) and expiration(10h)
    public Boolean validateToken(String token, UserDetails userDetails) {
        final Integer id = extractUserId(token);

        CustomUserDetails myDetails = (CustomUserDetails) userDetails;
        return (id.equals(myDetails.getUser().getId()) && !isTokenExpired(token));

    }

    //method for getting authorization header from request
    public String resolveToken(HttpServletRequest req) {
        String bearerToken = req.getHeader(AUTHORIZATION);

        return (!Objects.isNull(bearerToken) && bearerToken.startsWith("Bearer")) ?
                bearerToken.substring(7) : null;
    }

    //method for getting subject (user ID) from JWT
    public Integer getUserIdFromToken(HttpServletRequest request) {
        return Integer.parseInt(Jwts.parser().setSigningKey(secret).parseClaimsJws(
                resolveToken(request)
        ).getBody().getSubject());
    }
}
