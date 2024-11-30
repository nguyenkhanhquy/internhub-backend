package com.internhub.backend.controller;

import com.internhub.backend.dto.response.SuccessResponse;
import com.internhub.backend.service.CloudinaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/files")
public class CloudinaryController {

    private final CloudinaryService cloudinaryService;

    @Autowired
    public CloudinaryController(CloudinaryService cloudinaryService) {
        this.cloudinaryService = cloudinaryService;
    }

    @PostMapping("/upload/image")
    public ResponseEntity<SuccessResponse<Object>> uploadImage(@RequestParam("file") MultipartFile file,
                                                               @RequestParam("folder") String folderName) throws IOException {
        SuccessResponse<Object> successResponse = SuccessResponse.builder()
                .message("Upload ảnh thành công")
                .result(cloudinaryService.uploadImage(file, folderName))
                .build();

        return ResponseEntity.ok(successResponse);
    }

    @PostMapping("/upload/raw")
    public ResponseEntity<SuccessResponse<Object>> uploadRaw(@RequestParam("file") MultipartFile file,
                                                             @RequestParam("folder") String folderName) throws IOException {
        SuccessResponse<Object> successResponse = SuccessResponse.builder()
                .message("Upload tệp thành công")
                .result(cloudinaryService.uploadRaw(file, folderName))
                .build();

        return ResponseEntity.ok(successResponse);
    }
}
