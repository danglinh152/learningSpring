package com.phom.onTapSecurity.service.error;


import com.phom.onTapSecurity.domain.DTO.response.RestResponse;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(IdInvalidException.class)
    public ResponseEntity<RestResponse<Object>> idInvalidException(IdInvalidException idInvalidException) {
        RestResponse<Object> res = new RestResponse<>();
        res.setError(idInvalidException.getMessage());
        res.setMessage(idInvalidException.getMessage());
        res.setStatusCode(400);
        return ResponseEntity.status(HttpStatusCode.valueOf(400)).body(res);
    }
}
