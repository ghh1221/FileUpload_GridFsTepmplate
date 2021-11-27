package com.example.fileupload.exception;

import com.example.fileupload.enums.BizCodeEnum;
import lombok.Getter;

/**
 * @author 郭恒恒23677
 * @date 2021/11/27
 * @description
 */
@Getter
public class CommonException extends RuntimeException{

    private int code;
    private String message;

    public CommonException(BizCodeEnum bizCodeEnum){
        this.code = bizCodeEnum.getCode();
        this.message = bizCodeEnum.getMessage();
    }
}
