package com.internhub.backend.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

public interface CloudinaryService {

    Map uploadImage(MultipartFile file, String folderName) throws IOException;

    Map uploadRaw(MultipartFile file, String folderName) throws IOException;

    Map deleteRaw(String publicId) throws IOException;
}
