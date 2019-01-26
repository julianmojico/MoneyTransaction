package com.agilisys.Exceptions;

import com.agilisys.Models.Error;

import javax.json.stream.JsonParsingException;
import javax.ws.rs.InternalServerErrorException;
import javax.ws.rs.ProcessingException;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class GenericExceptionMapper implements ExceptionMapper<Throwable> {


    @Override
    public Response toResponse(Throwable ex) {
        Response.StatusType type = getStatusType(ex);

        if (ex instanceof JsonParsingException) {
            Error error = new Error(Response.Status.BAD_REQUEST.toString(), ex.getMessage());
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(error)
                    .build();
        }

        if (ex instanceof ProcessingException) {
            Error error = new Error(Response.Status.BAD_REQUEST.toString(), ex.getMessage());
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(error)
                    .build();
        }

        if (ex instanceof Exception && (ex.getMessage() == "Bad input")) {
            Error error = new Error(Response.Status.BAD_REQUEST.toString(), ex.getMessage());
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(error)
                    .build();
        }

        if (ex instanceof InternalServerErrorException) {

            Error error = new Error(Response.Status.INTERNAL_SERVER_ERROR.toString(), "asdasd");
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(error)
                    .build();
        }

        Error error = new Error(Response.Status.INTERNAL_SERVER_ERROR.toString(), ex.getMessage());
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(error)
                .build();
    }

    private Response.StatusType getStatusType(Throwable ex) {
        if (ex instanceof WebApplicationException) {
            return ((WebApplicationException) ex).getResponse().getStatusInfo();
        } else {
            return Response.Status.INTERNAL_SERVER_ERROR;
        }
    }

    public GenericExceptionMapper() {
    }

}