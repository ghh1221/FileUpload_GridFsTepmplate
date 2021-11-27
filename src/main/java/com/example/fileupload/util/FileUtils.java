package com.example.fileupload.util;

import com.example.fileupload.enums.BizCodeEnum;
import com.example.fileupload.enums.FileTypeEnum;
import com.example.fileupload.exception.CommonException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * @author 郭恒恒23677
 * @date 2021/11/27
 * @description
 */
@Slf4j
public class FileUtils {
    public static void checkFile(MultipartFile multipartFile,Double fileMaxSize,List<String> fileTypeList){
        // 1、校验文件大小
        checkFileSize(multipartFile, fileMaxSize);

        // 2、根据文件后缀名校验上传文件类型
        String originalFilename = multipartFile.getOriginalFilename();
        if(StringUtils.isBlank(originalFilename)){
            throw new CommonException(BizCodeEnum.FIlE_NAME_IS_EMPTY);
        }
        String fileSuffix = originalFilename.substring(originalFilename.lastIndexOf(".") + 1);
        if(!checkFileSuffix(fileSuffix, fileTypeList)){
            throw new CommonException(BizCodeEnum.FILE_UPLOAD_TYPE_IS_NOT_ALLOW);
        }

        // 3、根据文件头信息校验上传文件类型
        InputStream inputStream = null;
        try {
            inputStream = multipartFile.getInputStream();
            // 3.1 获取文件头信息
            String fileHeaderMessage = getFileHeadMessage(inputStream);
            // 3.2 根据文件头信息获取文件的真实类型
            String fileUploadType = getFileType(fileHeaderMessage);
            if(fileUploadType==null || !checkFileSuffix(fileUploadType,fileTypeList)){
                throw new CommonException(BizCodeEnum.FILE_UPLOAD_TYPE_IS_NOT_ALLOW);
            }
        } catch (IOException e) {
            log.error("上传文件失败:{},message:{}",e,e.getMessage());
            throw new CommonException(BizCodeEnum.FILE_UPLOAD_ERROR);
        }
    }

    /**
     * 根据文件后缀名，判断上传文件的文件类型是否被允许
     * @param fileSuffix 文件后缀名
     * @param fileTypeList 允许上传的文件类型集合
     * @return 是否允许上传
     */
    private static boolean checkFileSuffix(String fileSuffix, List<String> fileTypeList) {
        if(StringUtils.isBlank(fileSuffix) || CollectionUtils.isEmpty(fileTypeList)){
            throw new CommonException(BizCodeEnum.FILE_SUFFIX_OR_ALLOW_FILE_TYPE_IS_EMPTY);
        }
        return fileTypeList.contains(fileSuffix);
    }

    /**
     * 根据文件大小，判断上传文件的文件大小是否被允许
     * @param multipartFile 文件
     * @param fileMaxSize 允许上传文件大小的最大值
     */
    private static void checkFileSize(MultipartFile multipartFile, Double fileMaxSize) {
        long fileBytes = multipartFile.getSize();
        double fileSize = (double) fileBytes / 1048576;
        if (fileSize <= 0) {
            throw new CommonException(BizCodeEnum.FILE_UPLOAD_EMPTY_FILE);
        } else if (fileSize > fileMaxSize) {
            throw new CommonException(BizCodeEnum.ILE_UPLOAD_EXCEED_LIMIT);
        }
    }

    /**
     * 获取文件的头信息
     * @param inputStream 文件流
     * @return 文件头信息
     * @throws IOException 异常
     */
    private static String getFileHeadMessage(InputStream inputStream) throws IOException {
        byte[] content = new byte[28];
        inputStream.read(content, 0, content.length);
        return bytesToHexString(content);
    }

    /**
     * 文件头字节数组转为十六进制编码
     * @param content 文件头字节
     * @return 十六进制位编码
     */
    private static String bytesToHexString(byte[] content) {
        StringBuilder builder = new StringBuilder();
        if (content == null || content.length <= 0) {
            return null;
        }
        String temp;
        for (byte b : content) {
            temp = Integer.toHexString(b & 0xFF).toUpperCase();
            if (temp.length() < 2) {
                builder.append(0);
            }
            builder.append(temp);
        }
        return builder.toString();
    }

    /**
     * 根据文件头信息获取上传文件的真实类型
     * @param fileHeaderMessage 文件头信息
     * @return 上传文件的真实类型
     */
    private static String getFileType(String fileHeaderMessage) {
        if(StringUtils.isBlank(fileHeaderMessage)){
            return null;
        }
        fileHeaderMessage = fileHeaderMessage.toUpperCase();
        FileTypeEnum[] fileTypeEnums = FileTypeEnum.values();
        for(FileTypeEnum fileTypeEnum :fileTypeEnums){
            if(fileHeaderMessage.startsWith(fileTypeEnum.getFileHeadMessage())){
                return fileTypeEnum.getFileSuffix();
            }
        }
        return null;
    }
}
