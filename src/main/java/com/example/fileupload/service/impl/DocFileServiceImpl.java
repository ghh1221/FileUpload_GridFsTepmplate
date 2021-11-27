package com.example.fileupload.service.impl;

import com.example.fileupload.entity.DocFile;
import com.example.fileupload.enums.BizCodeEnum;
import com.example.fileupload.exception.CommonException;
import com.example.fileupload.service.DocFileService;
import com.mongodb.client.gridfs.GridFSBucket;
import com.mongodb.client.gridfs.GridFSDownloadStream;
import com.mongodb.client.gridfs.model.GridFSFile;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsResource;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;
import java.io.IOException;
import java.util.Optional;

/**
 * @author 郭恒恒23677
 * @date 2021/11/27
 * @description
 */
@Service
@Slf4j
public class DocFileServiceImpl implements DocFileService {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private GridFsTemplate gridFsTemplate;

    @Autowired
    private GridFSBucket gridFSBucket;

    /**
     * 文件上传 
     */
    @Override
    public String upload(DocFile docFile,MultipartFile multipartFile) {
        try {
            String gridFsId = gridFsTemplate.store(multipartFile.getInputStream(),
                    multipartFile.getOriginalFilename(), multipartFile.getContentType()).toHexString();
            docFile.setGridFsId(gridFsId);
            mongoTemplate.save(docFile);
        } catch (IOException e) {
            log.error("上传文件失败:{},message:{}",e,e.getMessage());
            throw new CommonException(BizCodeEnum.FILE_UPLOAD_ERROR);
        }
        return docFile.getId();
    }

    /**
     * 文件下载
     */
    @Override
    public ResponseEntity<StreamingResponseBody> getById(String id) {
        DocFile docFile = mongoTemplate.findOne(new Query(Criteria.where("id").is(id)), DocFile.class);
        Optional.ofNullable(docFile).orElseThrow(() -> {
            return new CommonException(BizCodeEnum.FILE_DOWNLOAD_IS_NOT_EXIST);
        });

        GridFSFile gridFSFile = gridFsTemplate.findOne(new Query(Criteria.where("_id").is(docFile.getGridFsId())));
        Optional.ofNullable(gridFSFile).orElseThrow(()->{
            return new CommonException(BizCodeEnum.FILE_DOWNLOAD_IS_NOT_EXIST);
        });

        //打开下载流对象
        GridFSDownloadStream gridFS = gridFSBucket.openDownloadStream(gridFSFile.getObjectId());
        //创建gridFsSource，用于获取流对象
        GridFsResource resource = new GridFsResource(gridFSFile,gridFS);
        //获取流中的数据
        StreamingResponseBody streamingResponseBody = outputStream -> {
            IOUtils.copy(resource.getInputStream(), outputStream);
        };

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename="+gridFSFile.getFilename())
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(streamingResponseBody);
    }
}

