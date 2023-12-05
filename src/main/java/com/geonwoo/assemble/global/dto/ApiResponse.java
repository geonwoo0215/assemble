package com.geonwoo.assemble.global.dto;

import lombok.Getter;

@Getter
public class ApiResponse<T> {
    T data;

    public ApiResponse(T data) {
        this.data = data;
    }
}
