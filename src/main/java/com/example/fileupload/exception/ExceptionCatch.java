package com.example.fileupload.exception;

import com.example.fileupload.result.ResponseResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author 郭恒恒23677
 * @date 2021/11/27
 * @description
 */
@Slf4j
@ControllerAdvice
public class ExceptionCatch {

    @ResponseBody
    @ExceptionHandler(value = CommonException.class)
    public ResponseResult handlerException(CommonException e){
        //捕获异常后进行日志打印
        log.error("catch exception:{}",e.getMessage());
        //取出错误信息和错误代码
        return ResponseResult.error();
    }
}
