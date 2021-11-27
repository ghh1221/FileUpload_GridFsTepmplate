package com.example.fileupload.enums;

import lombok.Getter;

/**
 * @author 郭恒恒23677
 * @date 2021/11/27
 * @description
 */
@Getter
public enum  BizCodeEnum {
    SUCCESS(200,"操作成功"),
    ERROR(400,"操作失败"),
    FILE_UPLOAD_EMPTY_FILE(10001,"上传文件的文件大小小于等于0"),
    ILE_UPLOAD_EXCEED_LIMIT(10002,"上传文件的文件大小超过限制"),
    FIlE_NAME_IS_EMPTY(10003,"文件名为空"),
    FILE_SUFFIX_OR_ALLOW_FILE_TYPE_IS_EMPTY(10004,"文件后缀名为空或者允许上传的文件类型为空"),
    FILE_UPLOAD_TYPE_IS_NOT_ALLOW(10005,"上传文件的类型不被允许"),
    FILE_UPLOAD_ERROR(10006,"上传文件失败"),
    FILE_DOWNLOAD_IS_NOT_EXIST(10007,"下载的文件不存在"),
    FILE_DOWNLOAD_ERROR(10008,"文件下载失败");

    private int code;
    private String message;

    BizCodeEnum(int code,String message){
        this.code = code;
        this.message  = message;
    }
}
