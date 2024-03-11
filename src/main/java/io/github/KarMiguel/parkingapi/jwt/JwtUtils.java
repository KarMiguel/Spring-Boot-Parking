package io.github.KarMiguel.parkingapi.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@Slf4j
public class JwtUtils {

    public static final  String JWT_BEARER ="Bearer ";
    public static final  String JWT_AUTHORIZATION = "Authorization";
    public static final String SECRET_KEY = "0123456789-0123456789-0123456789";
    public static final  long EXPIRE_DAY = 0;
    public static final  long EXPIRE_HOURS = 0;
    public static final  long EXPIRE_MINUTES = 2;

    private JwtUtils(){

    }

    private static Key generateKey(){
        return Keys.hmacShaKeyFor(SECRET_KEY.getBytes(StandardCharsets.UTF_8));
    }

    public static Date toExpireDate(Date start){
        LocalDateTime dateTime = start.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        LocalDateTime end = dateTime.plusDays(EXPIRE_DAY).plusHours(EXPIRE_HOURS).plusMinutes(EXPIRE_MINUTES);
        return Date.from(end.atZone(ZoneId.systemDefault()).toInstant());
    }

    public static JwtToken cratedToken(String username,String role){
        Date issuedAt = new Date();
        Date limit = toExpireDate(issuedAt);

        String token = Jwts.builder()
                .setHeaderParam("typ","Jwt")
                .setSubject(username)
                .setIssuedAt(issuedAt)
                .setExpiration(limit)
                .signWith(generateKey(), SignatureAlgorithm.HS256)
                .claim("role",role)
                .compact();

        return new JwtToken(token);
    }

    private  static Claims getClaimsFromToken(String token){
        try{
            return Jwts.parserBuilder()
                    .setSigningKey(generateKey()).build()
                    .parseClaimsJwt(refactorToken(token)).getBody();
        }catch (JwtException ex){
            log.error(String.format("Token inválido %s ",ex.getMessage()));
        }
        return null;
    }

    public static String getUsernameFromToken(String token){
        return getClaimsFromToken(token).getSubject();
    }

    public static boolean isTokenValid(String token){
        try{
            Jwts.parserBuilder()
                    .setSigningKey(generateKey()).build()
                    .parseClaimsJwt(refactorToken(token));
            return true;
        }catch (JwtException ex){
            log.error(String.format("Token inválido %s ",ex.getMessage()));
        }
        return false;
    }

    private static  String refactorToken(String token){
        if (token.contains(JWT_BEARER)){
            return token.substring(JWT_BEARER.length());
        }
        return token;
    }

}
