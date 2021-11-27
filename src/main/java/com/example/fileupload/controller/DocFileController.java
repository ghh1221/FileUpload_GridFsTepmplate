package com.example.fileupload.controller;


import com.example.fileupload.entity.DocFile;
import com.example.fileupload.enums.FileTypeEnum;
import com.example.fileupload.service.DocFileService;
import com.example.fileupload.util.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import java.util.List;

/**
 * @author 郭恒恒23677
 * @date 2021/11/27
 * @description
 */
@Controller
@RequestMapping("/file")
public class DocFileController {

    @Autowired
    private DocFileService docFileService;

    @Value("${file.maxSize}")
    private Double fileMaxSize;

    @ResponseBody
    @PostMapping("/upload")
    public String uploadFile(MultipartFile multipartFile){
        // 允许上传的文件类型
        List<String> fileTypeList = FileTypeEnum.getFileTypeList();
        FileUtils.checkFile(multipartFile,fileMaxSize,fileTypeList);
        String fileId = docFileService.upload(new DocFile(multipartFile),multipartFile);
        return fileId;
    }


    @GetMapping("/download/{id}")
    public ResponseEntity<StreamingResponseBody> get(@PathVariable("id") String id) {
        return docFileService.getById(id);
    }

}
