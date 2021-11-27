package com.example.fileupload.result;

import com.example.fileupload.enums.BizCodeEnum;

/**
 * @author 郭恒恒23677
 * @date 2021/11/27
 * @description
 */
public class ResponseResult {
    private int code;
    private String message;

    public ResponseResult(BizCodeEnum commonCode){
        this.code = commonCode.getCode();
        this.message = commonCode.getMessage();
    }

    public static ResponseResult ok(){
        return new ResponseResult(BizCodeEnum.SUCCESS);
    }

    public static ResponseResult error(){
        return new ResponseResult(BizCodeEnum.ERROR);
    }
}
