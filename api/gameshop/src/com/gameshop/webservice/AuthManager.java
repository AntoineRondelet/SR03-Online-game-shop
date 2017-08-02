package com.gameshop.webservice;

import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.security.Key;

import com.gameshop.model.Customer;
import io.jsonwebtoken.*;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.Claims;
import org.hibernate.Session;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static com.gameshop.model.SessionFactoryUtil.getSessionFactory;

public class AuthManager {
    private String apiKey;
    private List<String> userScope;
    private String issuer;

    public AuthManager() {
        setApiKey();
        setUserScope();
        setIssuer();
    }

    private void setUserScope() {
        userScope = Arrays.asList("profile", "games", "consoles", "keywords", "payment-modes", "payment-status");
    }

    private void setIssuer() {
        issuer = "gameshop-authmanager-v1";
    }

    private String getIssuer() {return issuer;}

    private void setApiKey() {
        apiKey = "aEePuStgY";
    }

    public String getSubject(String jwt) {
        Claims claims = Jwts.parser()
                .setSigningKey(DatatypeConverter.parseBase64Binary(apiKey))
                .parseClaimsJws(jwt).getBody();

        return claims.getSubject();
    }

    // -- Sample method to construct a JWT -- //
    public String createJWT(String subject) {

        // -- The JWT signature algorithm we will be using to sign the token -- //
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);

        // -- We will sign our JWT with our ApiKey secret -- //
        byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(apiKey);
        Key signingKey = new SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.getJcaName());

        // -- Set the JWT Claims -- //
        JwtBuilder builder = Jwts.builder()
                .setIssuedAt(now) // time when the token is generated
                .setSubject(subject) // represents the user
                .setIssuer(issuer) // represents the issuer of the token
                .claim("scope", userScope) // scopes of the user (what the user can do)
                .signWith(signatureAlgorithm, signingKey);

        // -- If it has been specified, let's add the expiration -- //
        long expMillis = nowMillis*360;
        Date exp = new Date(expMillis);
        builder.setExpiration(exp);

        // -- Builds the JWT and serializes it to a compact, URL-safe string -- //
        return builder.compact();
    }

    // -- Method that validates and read the JWT (return true if the toke is valid and false if not) -- //
    public boolean parseJWT(String jwt) {

        try {
            // -- Parsing the token to retrieve claims -- //
            Claims claims = Jwts.parser()
                    .setSigningKey(DatatypeConverter.parseBase64Binary(apiKey))
                    .parseClaimsJws(jwt).getBody();

            Session session = getSessionFactory().openSession();
            String resultQuery = "from Customer c where c.username = :username";
            Customer customer = (Customer)session.createQuery(resultQuery)
                    .setParameter("username", claims.getSubject())
                    .uniqueResult();

            // -- The customer set in the token is not valid -- //
            if (customer == null || !(ConnexionManager.isConnected(customer))) {
                return false;
            }

            // -- Verify the expiration fo the token -- //
            long nowMillis = System.currentTimeMillis();
            Date now = new Date(nowMillis);
            if (claims.getExpiration().before(now)) {
                return false;
            }

            return true;
        } catch (Exception e) {
            System.out.println("ERROR in parseJWT: " + e.toString());

            // -- If an exception is raised then the token is not valid -- //
            return false;
        }
    }
}
