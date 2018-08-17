package com.aggregator;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

@Path("/hello")
public class FirstService {
    @GET
    public Response hello() {
        return Response.status(200).entity("trololo").build();
    }
}
