package org.example.demo2.entity;

import org.springframework.http.HttpStatus;

public class Response<T> {
    private Integer code;
    private String message;
    private T data;

    public Response(Integer code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public static <T> Response<T> success(T data) {
        return new Response<>(HttpStatus.OK.value(),"success!", data);
    }

    public static <T> Response<T> fail(T data) {
        return new Response<>(HttpStatus.INTERNAL_SERVER_ERROR.value(),"failed!", data);
    }

    public Integer getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public T getData() {
        return data;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setData(T data) {
        this.data = data;
    }
}
