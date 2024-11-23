package com.phom.onTapSecurity.util;

import com.phom.onTapSecurity.domain.DTO.response.RestResponse;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

@ControllerAdvice
public class FormatRestResponse implements ResponseBodyAdvice<Object> {
    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        return true;
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType, Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {
//        ServletServerHttpResponse servletServerHttpResponse = (ServletServerHttpResponse) response;
//        HttpServletResponse servletResponse = servletServerHttpResponse.getServletResponse();
        HttpServletResponse servletResponse = ((ServletServerHttpResponse) response).getServletResponse();
        long statusCode = servletResponse.getStatus();

        RestResponse<Object> res = new RestResponse<>();
        res.setStatusCode(Integer.parseInt(statusCode + ""));

        if (statusCode >= 400) {
//            case error
            return body;
        } else {
//            case success
            res.setMessage("CALL API SUCCESS");
            res.setData(body);
        }
        return res;
    }
}
