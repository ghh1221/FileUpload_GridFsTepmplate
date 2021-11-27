package com.example.fileupload.entity;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;

/**
 * @author heng
 * @date 2021/11/27
 * @description 上传文件实体类
 */
@Data
@Document(collection = "docFile")
public class DocFile {
    /**
     * 附件id
     */
    private String id;
    /**
     * 附件名称
     */
    private String fileName;
    /**
     * 附件类型
     */
    private String fileType;
    /**
     * 附件上传时间
     */
    private Date uploadTime;
    /**
     * 附件存放在GridFs中，其关联的gridFsId
     */
    private String gridFsId;

    public DocFile(MultipartFile multipartFile){
        this.fileName = multipartFile.getOriginalFilename();
        this.fileType = fileName.substring(fileName.lastIndexOf(".")+1);
        this.uploadTime = new Date();
    }

    public DocFile(){

    }
}
