package com.flavor.trail.common;

import lombok.Getter;

@Getter
public class BusinessException extends RuntimeException {

    private final int code;

    public BusinessException(int code, String message) {
        super(message);
        this.code = code;
    }

    public BusinessException(String message) {
        this(1000, message);
    }

    public static BusinessException unauthorized() {
        return new BusinessException(1002, "未授权");
    }

    public static BusinessException forbidden() {
        return new BusinessException(1003, "禁止访问");
    }

    public static BusinessException notFound() {
        return new BusinessException(1004, "资源不存在");
    }

    public static BusinessException badRequest(String message) {
        return new BusinessException(1001, message);
    }
}