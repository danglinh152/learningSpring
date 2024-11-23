package com.phom.onTapSecurity.domain.DTO.response;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RestResponse<T> {
    private int statusCode;
    private String error;
    private Object message;
    private T data;
}
