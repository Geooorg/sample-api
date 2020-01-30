package de.gs.api.auth;

import io.jsonwebtoken.*;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.concurrent.TimeUnit;

@Component
@Slf4j
public class AuthenticationProvider {

    private final String jwtSecret;

    @Autowired
    public AuthenticationProvider(@Value("${api.jwt.secret}") String secret) {
        this.jwtSecret = secret;
    }


    public String createJwtForApiUser(ApiUser user) {
        if (CollectionUtils.isEmpty(user.getRoles())) {
            throw new RuntimeException("User must have at least one role");
        }

        ApiUserRole role = new ArrayList<>(user.getRoles()).get(0);
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", user.getId());
        claims.put("username", user.getUsername());
        claims.put("roles", role); // TODO for the moment, one role must suffice :-)

        Date now = new Date();
        return Jwts.builder()
                .setClaims(claims)
                .setId(UUID.randomUUID().toString())
                .setIssuer("sample-api")
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + TimeUnit.SECONDS.toSeconds(3600)))
                .signWith(SignatureAlgorithm.HS256, jwtSecret.getBytes())
                .compact();
    }


    public boolean isValid(String jwt) {
        Jws<Claims> claimsJws;
        try {
            claimsJws = Jwts.parser().setSigningKey(jwtSecret.getBytes()).parseClaimsJws(jwt);
        } catch (Exception e) {
            log.warn("Token invalid, expired or could not be parsed! {}", jwt, e);
            return false;
        }

        // TODO userId should be re-validated against the repository
        // TODO return object containing userId and role(s)
        return true;
    }
}
