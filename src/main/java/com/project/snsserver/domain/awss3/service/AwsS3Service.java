package com.project.snsserver.domain.awss3.service;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface AwsS3Service {

    /**
     * aws s3에 이미지 업로드
     */
    String uploadFile (MultipartFile file, String dir);

    /**
     * aws s3에 복수의 이미지 업로드
     */
    List<String> uploadFiles(List<MultipartFile> files, String dir);

    /**
     * aws s3에 업로드 된 이미지 삭제
     */
    void deleteFile (String imgUrl, String dir);
}
