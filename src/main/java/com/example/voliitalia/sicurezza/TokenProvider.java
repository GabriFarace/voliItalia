package com.example.voliitalia.sicurezza;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.JWTVerifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static com.auth0.jwt.algorithms.Algorithm.HMAC512;
import static java.util.Arrays.stream;

@Component
public class TokenProvider {

    private static final long EXPIRATION_DATE = 86_400_000;

    @Value("${jwt.secret}")
    private String secret;

    public String generateJwtTokenUser(UtentePrincipal utentePrincipal) {
        String[] claims = getClaimsFromUser(utentePrincipal);
        return JWT.create().withIssuer("voliItalia").withAudience("voliItalia portal")
                .withIssuedAt(new Date()).withSubject(utentePrincipal.getUsername())
                .withArrayClaim("permessi", claims).withExpiresAt(new Date(System.currentTimeMillis() + EXPIRATION_DATE))
                .sign(HMAC512(secret.getBytes()));
    }

    public String generateJwtTokenAirline(CompagniaPrincipal compagniaPrincipal) {
        String[] claims = getClaimsFromAirline(compagniaPrincipal);
        return JWT.create().withIssuer("voliItalia").withAudience("voliItalia portal")
                .withIssuedAt(new Date()).withSubject(compagniaPrincipal.getUsername())
                .withArrayClaim("permessi", claims).withExpiresAt(new Date(System.currentTimeMillis() + EXPIRATION_DATE))
                .sign(HMAC512(secret.getBytes()));
    }

    public List<GrantedAuthority> getAuthorities(String token) {
        String[] claims = getClaimsFromToken(token);
        return stream(claims).map(SimpleGrantedAuthority::new).collect(Collectors.toList());
    }

    public Authentication getAuthentication(String username, List<GrantedAuthority> authorities, HttpServletRequest request) {
        UsernamePasswordAuthenticationToken userPasswordAuthToken = new
                UsernamePasswordAuthenticationToken(username, null, authorities);
        userPasswordAuthToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        return userPasswordAuthToken;
    }

    public boolean isTokenValid(String username, String token) {
        JWTVerifier verifier = getJWTVerifier();
        return username!=null && !isTokenExpired(verifier, token);
    }

    public String getSubject(String token) {
        JWTVerifier verifier = getJWTVerifier();
        return verifier.verify(token).getSubject();
    }

    private boolean isTokenExpired(JWTVerifier verifier, String token) {
        Date expiration = verifier.verify(token).getExpiresAt();
        return expiration.before(new Date());
    }

    private String[] getClaimsFromToken(String token) {
        JWTVerifier verifier = getJWTVerifier();
        return verifier.verify(token).getClaim("permessi").asArray(String.class);
    }

    private JWTVerifier getJWTVerifier() {
        JWTVerifier verifier;
        try {
            Algorithm algorithm = HMAC512(secret);
            verifier = JWT.require(algorithm).withIssuer("voliItalia").build();
        }catch (JWTVerificationException exception) {
            throw new JWTVerificationException("Il token non può essere verificato");
        }
        return verifier;
    }

    private String[] getClaimsFromUser(UtentePrincipal utente) {
        List<String> authorities = new ArrayList<>();
        for (GrantedAuthority grantedAuthority : utente.getAuthorities()){
            authorities.add(grantedAuthority.getAuthority());
        }
        return authorities.toArray(new String[0]);
    }

    private String[] getClaimsFromAirline(CompagniaPrincipal compagnia) {
        List<String> authorities = new ArrayList<>();
        for (GrantedAuthority grantedAuthority : compagnia.getAuthorities()){
            authorities.add(grantedAuthority.getAuthority());
        }
        return authorities.toArray(new String[0]);
    }
}
