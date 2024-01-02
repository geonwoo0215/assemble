package com.geonwoo.assemble.global.aws.s3.controller;

import com.geonwoo.assemble.global.aws.s3.service.S3Service;
import com.geonwoo.assemble.global.dto.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class S3Controller {

    private final S3Service service;

    @PostMapping("/file/multiparty-files")
    public ResponseEntity<ApiResponse<List<String>>> multipleFilesUpload
            (
                    @RequestPart List<MultipartFile> multipartFiles
            ) {

        List<String> uploadUrls = service.putObject(multipartFiles);
        return new ResponseEntity<>(new ApiResponse<>(uploadUrls), HttpStatus.CREATED);
    }
}
