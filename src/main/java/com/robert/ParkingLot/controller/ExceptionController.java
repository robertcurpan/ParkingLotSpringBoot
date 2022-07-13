package com.robert.ParkingLot.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.robert.ParkingLot.exceptions.ParkingLotGeneralException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class ExceptionController extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = { ParkingLotGeneralException.class })
    protected ResponseEntity<Object> handleConflict(ParkingLotGeneralException ex, WebRequest request) {
        // In functie de mesajul exceptiei, vom construi un JSON ca body al raspunsului
        String exceptionMessage = ex.toString();
        ObjectNode bodyOfResponse = new ObjectMapper().createObjectNode();
        bodyOfResponse.put("message", exceptionMessage);

        return handleExceptionInternal(ex, bodyOfResponse, new HttpHeaders(), HttpStatus.CONFLICT, request);
    }
}
