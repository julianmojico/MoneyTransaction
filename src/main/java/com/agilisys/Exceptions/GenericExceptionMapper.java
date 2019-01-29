package com.agilisys.Exceptions;

import com.agilisys.Models.Error;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class GenericExceptionMapper implements ExceptionMapper<WebApplicationException> {

    @Override
    public Response toResponse(WebApplicationException e) {
        Response r = e.getResponse();
        Error error = null;
        Response.ResponseBuilder response = Response.fromResponse(r);

        if (r.getStatus() == 405) {
            error = new Error(Response.Status.METHOD_NOT_ALLOWED.toString(), e.getMessage());
            response.status(Response.Status.METHOD_NOT_ALLOWED);
        }

        if (r.getStatus() == 404) {
            error = new Error(Response.Status.NOT_FOUND.toString(), e.getMessage());
            response.status(Response.Status.NOT_FOUND);
        }

        return response.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
                .entity(error)
                .build();
    }
}
