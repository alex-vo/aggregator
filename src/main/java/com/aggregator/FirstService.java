package com.aggregator;

import com.aggregator.auth.Secured;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

@Secured
@Path("/hello")
public class FirstService {
    @GET
    public Response hello() {
        return Response.status(200).entity("trololo").build();
    }
}
