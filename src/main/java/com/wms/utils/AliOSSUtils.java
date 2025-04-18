package com.wms.utils;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

@Component
public class AliOSSUtils {

    @Value("${alioss.endpoint}")
    private String endpoint;
    @Value("${alioss.accessKeyId}")
    private String accessKeyId;
    @Value("${alioss.accessKeySecret}")
    private String accessKeySecret;
    @Value("${alioss.bucketName}")
    private String bucketName;

    /**
     * 文件上传
     * @param multipartFile
     * @return 文件url
     */
    public String uploadFile(MultipartFile multipartFile) throws IOException {

        // 获取上传的文件的输入流
        InputStream inputStream = multipartFile.getInputStream();

        // 避免文件覆盖
        String originalFilename = multipartFile.getOriginalFilename();
        String fileName = UUID.randomUUID().toString() + originalFilename.substring(originalFilename.lastIndexOf("."));

        //上传文件到 OSS
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
        ossClient.putObject(bucketName, fileName, inputStream);

        String url = "https://" + bucketName + "." + endpoint + "/" + fileName;

        // 关闭ossClient
        ossClient.shutdown();

        return url;// 把上传到oss的路径返回

    }

    /**
     * 文件上传
     * @param inputStream
     * @param filename
     * @return 文件url
     */
    public String uploadFile(InputStream inputStream, String filename) throws IOException {
        // 避免文件覆盖
        String fileName = UUID.randomUUID().toString() + filename.substring(filename.lastIndexOf("."));

        //上传文件到 OSS
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
        ossClient.putObject(bucketName, fileName, inputStream);

        String url = "https://" + bucketName + "." + endpoint + "/" + fileName;

        // 关闭ossClient
        ossClient.shutdown();

        return url;// 把上传到oss的路径返回
    }
}
