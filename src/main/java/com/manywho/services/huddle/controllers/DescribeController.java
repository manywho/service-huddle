package com.manywho.services.huddle.controllers;

import com.manywho.sdk.entities.describe.DescribeServiceResponse;
import com.manywho.sdk.entities.run.elements.config.ServiceRequest;
import com.manywho.sdk.entities.translate.Culture;
import com.manywho.sdk.services.describe.DescribeServiceBuilder;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

@Path("/")
@Consumes("application/json")
@Produces("application/json")
public class DescribeController {
    @Path("/metadata")
    @POST
    public DescribeServiceResponse describe(ServiceRequest serviceRequest) throws Exception {
        return new DescribeServiceBuilder()
                .setProvidesIdentity(true)
                .setProvidesLogic(true)
                .setCulture(new Culture("EN", "US"))
                .createDescribeService()
                .createResponse();
    }
}
