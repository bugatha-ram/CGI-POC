/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cgi.poc.dw.exception.mapper;

import com.cgi.poc.dw.exception.ErrorInfo;
import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

/**
 * @author dawna.floyd
 */
@Provider
@Produces(MediaType.APPLICATION_JSON)
public class CustomConstraintViolationExceptionMapper implements
    ExceptionMapper<ConstraintViolationException> {

  @Override
  public Response toResponse(ConstraintViolationException exception) {
    Response response;
    ErrorInfo errRet = new ErrorInfo();
    Set<ConstraintViolation<?>> constraintViolations = exception.getConstraintViolations();
    for (ConstraintViolation violation : constraintViolations) {
      String message = violation.getMessage();
      errRet.addError(Integer.toString(Response.Status.BAD_REQUEST.getStatusCode()), message);
    }
    response = Response.noContent().status(Response.Status.BAD_REQUEST).entity(errRet).build();

    return response;
  }

}
