package com.example.guidecoffee.modules.filestorage.strategy;

import com.example.guidecoffee.modules.filestorage.dto.UploadedImage;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface FileStorageStrategy {

    UploadedImage upload(MultipartFile file) throws IOException;
}
