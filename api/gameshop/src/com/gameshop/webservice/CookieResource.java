package com.gameshop.webservice;

import javax.ws.rs.CookieParam;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.NewCookie;
import javax.ws.rs.core.Response;

@Path("cookie")
public class CookieResource {

    @GET
    public Response getCookie(@CookieParam("A-Cookie") String cookie) {
        Response response = null;
        if (cookie == null) {
            response = Response.ok("A-Cookie: Cookie #1")
                    .cookie(new NewCookie("A-Cookie", "Cookie #1"))
                    .build();
            return response;
        } else {
            String cookieNum = cookie.substring(cookie.indexOf("#") + 1);
            int number = Integer.parseInt(cookieNum);
            number++;
            String updatedCookie = "Cookie #" + number;
            response = Response.ok("A-Cookie: " + updatedCookie)
                    .cookie(new NewCookie("A-Cookie", updatedCookie))
                    .build();
            return response;
        }
    }
}

