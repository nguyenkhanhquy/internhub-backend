package com.internhub.backend.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Service
public class CloudinaryServiceImpl implements CloudinaryService {

    private final Cloudinary cloudinary;

    @Autowired
    public CloudinaryServiceImpl(Cloudinary cloudinary) {
        this.cloudinary = cloudinary;
    }

    @Override
    public Map uploadImage(MultipartFile file, String folderName) throws IOException {
        return cloudinary.uploader().upload(file.getBytes(),
                ObjectUtils.asMap(
                        "resource_type", "image",
                        "folder", folderName
                ));
    }

    @Override
    public Map uploadRaw(MultipartFile file, String folderName) throws IOException {
        String originalFilename = file.getOriginalFilename();
        if (originalFilename == null) {
            throw new IOException("Tên tệp không hợp lệ");
        }

        String extension = originalFilename.substring(originalFilename.lastIndexOf('.') + 1);

        return cloudinary.uploader().upload(file.getBytes(), ObjectUtils.asMap(
                "format", extension,
                "resource_type", "raw",
                "folder", folderName,
                "public_id", originalFilename
        ));
    }

    @Override
    public Map deleteRaw(String publicId) throws IOException {
        return cloudinary.uploader().destroy(publicId, ObjectUtils.asMap(
                "resource_type", "raw",
                "invalidate", true
        ));
    }
}
