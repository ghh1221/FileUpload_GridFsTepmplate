package com.example.fileupload.enums;

import lombok.Getter;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public enum FileTypeEnum {
    /**
     * 系统允许上传的文件类型
     */
    JPEG("jpeg", "FFD8FF"),
    JPG("jpg", "FFD8FF"),
    PNG("png", "89504E47"),
    BMP("bmp", "424D"),
    RTF("rtf", "7B5C727466"),
    DOC("doc", "D0CF11E0"),
    DOCX("docx", "504B030414"),
    PDF("pdf", "255044462D312E");

    private String fileSuffix;
    private String fileHeadMessage;

    FileTypeEnum(String fileSuffix,String fileHeadMessage){
        this.fileSuffix = fileSuffix;
        this.fileHeadMessage = fileHeadMessage;
    }

    /**
     * 获取允许上传的文件类型集合
     */
    public static List<String> getFileTypeList(){
        FileTypeEnum[] fileTypes = FileTypeEnum.values();
        // 获取允许上传的文件类型
        List<String> fileTypeList = Arrays.asList(fileTypes).stream().map(fileTypeEnum -> {
            return fileTypeEnum.getFileSuffix();
        }).collect(Collectors.toList());
        return fileTypeList;
    }
}
