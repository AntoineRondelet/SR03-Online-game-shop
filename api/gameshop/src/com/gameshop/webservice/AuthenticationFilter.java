package com.gameshop.webservice;

import javax.ws.rs.NotAuthorizedException;
import javax.ws.rs.Priorities;
import javax.annotation.Priority;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;
import java.io.IOException;

@Secured
@Provider
@Priority(Priorities.AUTHENTICATION)
public class AuthenticationFilter implements ContainerRequestFilter {

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        try {
            // -- Get the HTTP Authorization header from the request -- //
            String authorizationHeader = requestContext.getHeaderString(HttpHeaders.AUTHORIZATION);

            // -- Check if the HTTP Authorization header is present and formatted correctly -- //
            if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
                throw new NotAuthorizedException("Authorization header must be provided");
            }

            // -- Extract the token from the HTTP Authorization header -- //
            String token = authorizationHeader.substring("Bearer".length()).trim();

            System.out.println("DEBUG in Auth filter");
            // -- Validate the token -- //
            validateToken(token);
        } catch (Exception e) {
            // -- If the token validation raised an exception return 401 HTTP code -- //
            requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED)
                    .entity(e.toString()).build());
        }
    }

    private void validateToken(String token) throws Exception {
        System.out.println("DEBUG in validate token");
        AuthManager authManager = new AuthManager();
        boolean validToken = authManager.parseJWT(token);
        if (!validToken) {
            System.out.println("DEBUG in validate token Exception");
            throw new NotAuthorizedException("Invalid Token");
        }
    }
}
