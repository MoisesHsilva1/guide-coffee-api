package com.example.guidecoffee.modules.filestorage.strategy.impl;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.example.guidecoffee.modules.filestorage.annotation.FileTypeRestriction;
import com.example.guidecoffee.modules.filestorage.dto.UploadedImage;
import com.example.guidecoffee.modules.filestorage.strategy.FileStorageStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ImageStorageStrategy implements FileStorageStrategy {

    private final Cloudinary cloudinary;

    @Override
    public UploadedImage upload(
            @FileTypeRestriction(
                    acceptedTypes = {"image/jpeg", "image/png", "image/webp"}
            ) MultipartFile file
    ) throws IOException {
        Map<String, Object> result = cloudinary.uploader().upload(
                file.getBytes(),
                ObjectUtils.asMap(
                        "resource_type", "image",
                        "folder", "guide-coffee/reviews"
                )
        );

        return new UploadedImage(
                result.get("secure_url").toString(),
                result.get("public_id").toString()
        );
    }
}
