package com.example.guidecoffee.modules.filestorage.strategy;

import com.cloudinary.Cloudinary;
import com.cloudinary.Uploader;
import com.example.guidecoffee.modules.filestorage.dto.UploadedImage;
import com.example.guidecoffee.modules.filestorage.strategy.impl.ImageStorageStrategy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;

import java.io.IOException;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ImageStorageStrategyTest {

    @Mock
    private Cloudinary cloudinary;

    @Mock
    private Uploader uploader;

    private ImageStorageStrategy strategy;

    @BeforeEach
    void setUp() {
        strategy = new ImageStorageStrategy(cloudinary);
    }

    @Test
    void shouldUploadImageAndReturnCloudinaryMetadata() throws Exception {
        var file = multipartFile();
        var uploadedImage = new UploadedImage(
                "https://res.cloudinary.com/demo/image/upload/coffee.jpg",
                "guide-coffee/reviews/coffee"
        );
        when(cloudinary.uploader()).thenReturn(uploader);
        when(uploader.upload(any(byte[].class), anyMap())).thenReturn(Map.of(
                "secure_url", uploadedImage.secureUrl(),
                "public_id", uploadedImage.publicId()
        ));

        UploadedImage result = strategy.upload(file);

        assertThat(result).isEqualTo(uploadedImage);

        ArgumentCaptor<byte[]> fileCaptor = ArgumentCaptor.forClass(byte[].class);
        verify(uploader).upload(fileCaptor.capture(), anyMap());
        assertThat(fileCaptor.getValue()).isEqualTo("image".getBytes());
    }

    @Test
    void shouldUploadImageToTheReviewsFolder() throws Exception {
        when(cloudinary.uploader()).thenReturn(uploader);
        when(uploader.upload(any(byte[].class), anyMap())).thenReturn(Map.of(
                "secure_url", "https://example.com/coffee.jpg",
                "public_id", "guide-coffee/reviews/coffee"
        ));

        strategy.upload(multipartFile());

        ArgumentCaptor<Map<String, Object>> options = ArgumentCaptor.forClass(Map.class);
        verify(uploader).upload(any(byte[].class), options.capture());
        assertThat(options.getValue())
                .containsEntry("resource_type", "image")
                .containsEntry("folder", "guide-coffee/reviews");
    }

    @Test
    void shouldPropagateCloudinaryIOException() throws Exception {
        when(cloudinary.uploader()).thenReturn(uploader);
        when(uploader.upload(any(byte[].class), anyMap())).thenThrow(new IOException("Cloudinary unavailable"));

        assertThatThrownBy(() -> strategy.upload(multipartFile()))
                .isInstanceOf(IOException.class)
                .hasMessage("Cloudinary unavailable");
    }

    private MockMultipartFile multipartFile() {
        return new MockMultipartFile(
                "file",
                "coffee.jpg",
                "image/jpeg",
                "image".getBytes()
        );
    }
}
