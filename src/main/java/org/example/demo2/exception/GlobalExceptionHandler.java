package org.example.demo2.exception;

import io.jsonwebtoken.JwtException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.demo2.entity.Response;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({Exception.class})
    public Response<String> handleException(Exception e, HttpServletRequest request, HttpServletResponse response) {
        return new Response<>(500, "error" + e.getMessage(), null);
    }

    @ExceptionHandler({JwtException.class})
    public Response<String> handleJwtException(JwtException e, HttpServletRequest request, HttpServletResponse response) {
        return new Response<>(500, "Token验证失败" + e.getMessage(), null);
    }
}
