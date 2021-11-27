package com.example.fileupload.service;

import com.example.fileupload.entity.DocFile;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

/**
 * @author 郭恒恒23677
 * @date 2021/11/27
 * @description
 */
public interface DocFileService {
    /**
     * 文件上传功能
     * @param docFile 实体类
     * @param multipartFile 上传文件
     * @return
     */
    String upload(DocFile docFile,MultipartFile multipartFile);

    /**
     * 文件流
     * @param id 文件id
     * @return
     */
    ResponseEntity<StreamingResponseBody> getById(String id);
}
