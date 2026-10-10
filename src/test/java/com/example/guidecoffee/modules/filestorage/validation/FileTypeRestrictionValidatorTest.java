package com.example.guidecoffee.modules.filestorage.validation;

import com.example.guidecoffee.modules.filestorage.annotation.FileTypeRestriction;
import jakarta.validation.ConstraintValidatorContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FileTypeRestrictionValidatorTest {

    private static final byte[] PNG_BYTES = java.util.Base64.getDecoder().decode(
            "iVBORw0KGgoAAAANSUhEUgAAAAEAAAABCAQAAAC1HAwCAAAAC0lEQVR42mNk+A8AAQUBAScY42YAAAAASUVORK5CYII="
    );

    @Mock
    private ConstraintValidatorContext context;

    @Mock
    private ConstraintValidatorContext.ConstraintViolationBuilder violationBuilder;

    private FileTypeRestrictionValidator validator;

    @BeforeEach
    void setUp() {
        var restriction = mock(FileTypeRestriction.class);
        when(restriction.acceptedTypes()).thenReturn(new String[]{"image/png"});
        when(restriction.message()).thenReturn("Unsupported file type");
        validator = new FileTypeRestrictionValidator();
        validator.initialize(restriction);
    }

    @Test
    void shouldAcceptNullValue() {
        assertThat(validator.isValid(null, context)).isTrue();
    }

    @Test
    void shouldAcceptValidMultipartFile() {
        assertThat(validator.isValid(pngFile(), context)).isTrue();
    }

    @Test
    void shouldAcceptArrayAndCollectionOfValidFiles() {
        var file = pngFile();

        assertThat(validator.isValid(new MultipartFile[]{file}, context)).isTrue();
        assertThat(validator.isValid(List.of(file), context)).isTrue();
    }

    @Test
    void shouldRejectEmptyFile() {
        stubViolationBuilder();
        var file = new MockMultipartFile("file", "empty.png", "image/png", new byte[0]);

        assertThat(validator.isValid(file, context)).isFalse();
        verify(context).disableDefaultConstraintViolation();
        verify(context).buildConstraintViolationWithTemplate("File is empty or null");
    }

    @Test
    void shouldRejectUnsupportedContent() {
        stubViolationBuilder();
        var file = new MockMultipartFile(
                "file",
                "notes.txt",
                "text/plain",
                "not an image".getBytes()
        );

        assertThat(validator.isValid(file, context)).isFalse();
        verify(context).buildConstraintViolationWithTemplate(
                "Unsupported file type: notes.txt. Accepted types: [image/png]"
        );
    }

    @Test
    void shouldRejectUnsupportedValue() {
        assertThat(validator.isValid("file", context)).isFalse();
    }

    @Test
    void shouldRejectWhenFileCannotBeRead() throws IOException {
        stubViolationBuilder();
        MultipartFile file = mock(MultipartFile.class);
        when(file.isEmpty()).thenReturn(false);
        when(file.getInputStream()).thenThrow(new IOException("read error"));

        assertThat(validator.isValid(file, context)).isFalse();
        verify(context).buildConstraintViolationWithTemplate("Unable to validate file content");
    }

    private MultipartFile pngFile() {
        return new MockMultipartFile("file", "coffee.png", "image/png", PNG_BYTES);
    }

    private void stubViolationBuilder() {
        when(context.buildConstraintViolationWithTemplate(anyString())).thenReturn(violationBuilder);
    }
}
