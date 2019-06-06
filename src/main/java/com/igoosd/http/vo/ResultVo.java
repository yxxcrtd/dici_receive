package com.igoosd.http.vo;

import lombok.Data;

/**
 * 2017/7/28.
 * http请求响应
 */
@Data
public class ResultVo {

    private Boolean success;
    private String message;
    private Object data;

}
