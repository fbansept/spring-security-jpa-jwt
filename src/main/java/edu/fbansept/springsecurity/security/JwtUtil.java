package edu.fbansept.springsecurity.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class JwtUtil {

    @Value("${jwt.secret}")
    private String secret;

    //Retourne le corp du token
    public Claims extractionDuCorpDuToken(String token) {
        return Jwts.parser()
                .setSigningKey(secret)
                .parseClaimsJws(token)
                .getBody();
    }

    //Retourne vrai si le token n'a pas dépassé la date d'expiration
    private Boolean tokenNonDepasseDateExpiration(String token) {
        return extractionDuCorpDuToken(token)
                .getExpiration()
                .after(new Date());
    }

    //Retourne un token
    public String generateToken(UserDetails userDetails) {

        Map<String, Object> tokenData = new HashMap<>();

        //ici vous pouvez rajouter tout ce que vous voulez
        tokenData.put("roles", userDetails.getAuthorities().stream()
            .map(GrantedAuthority::getAuthority)
            .collect(Collectors.joining(",")));

        return Jwts.builder()
            .setClaims(tokenData)
            .setSubject(userDetails.getUsername())
            .setIssuedAt(new Date(System.currentTimeMillis()))
            .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10))
            .signWith(SignatureAlgorithm.HS256, secret).compact();
    }

    //Retourne vrai si le nom de l'utilisateur tentant de se connecter correspond
    //au subject du corp du token et si la date d'expiration n'est pas passée.
    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractionDuCorpDuToken(token).getSubject();
        return (username.equals(userDetails.getUsername()) && !tokenNonDepasseDateExpiration(token));
    }
}
