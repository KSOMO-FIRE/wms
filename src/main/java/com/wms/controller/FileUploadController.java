package com.wms.controller;

import com.wms.common.Result;
import com.wms.utils.AliOSSUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@Slf4j
public class FileUploadController {

    @Autowired
    private AliOSSUtils aliOSSUtils;

    @PostMapping("/file/upload")
    public Result handfileupload(@RequestParam MultipartFile file){
        try {
            String url = aliOSSUtils.uploadFile(file);
            return Result.success("文件上传成功，URL: " + url);
        } catch (Exception e) {
            log.error(e.getMessage());
            return Result.fail("文件上传失败: " + e.getMessage());
        }
    }
}
