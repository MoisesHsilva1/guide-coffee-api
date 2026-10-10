package com.example.guidecoffee.modules.filestorage.validation;

import com.example.guidecoffee.modules.filestorage.annotation.FileTypeRestriction;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.apache.tika.detect.DefaultDetector;
import org.apache.tika.detect.Detector;
import org.apache.tika.io.TikaInputStream;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.ParseContext;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Set;

public class FileTypeRestrictionValidator implements ConstraintValidator<FileTypeRestriction, Object> {

    private Set<String> acceptedTypes;
    private String messageTemplate;

    private final Detector detector = new DefaultDetector();

    @Override
    public void initialize(FileTypeRestriction annotation) {
        this.acceptedTypes = Set.of(annotation.acceptedTypes());
        this.messageTemplate = annotation.message();
    }

    @Override
    public boolean isValid(
            Object value,
            ConstraintValidatorContext context
    ) {
        return switch (value) {
            case null -> true;
            case MultipartFile file -> validateFile(file, context);
            case MultipartFile[] files -> validateFiles(Arrays.asList(files), context);
            case Collection<?> files -> validateFiles(files, context);
            default -> false;
        };
    }

    private boolean validateFiles(
            Collection<?> files,
            ConstraintValidatorContext context
    ) {
        return files.stream()
                .allMatch(file -> file instanceof MultipartFile multipartFile
                        && validateFile(multipartFile, context));
    }

    private boolean validateFile(
            MultipartFile file,
            ConstraintValidatorContext context
    ) {
        if (file == null || file.isEmpty()) {
            return buildViolation(context, "File is empty or null");
        }

        try {
            String contentType = detectContentType(file);

            if (acceptedTypes.contains(contentType)) {
                return true;
            }

            return buildViolation(
                    context,
                    String.format(
                            "%s: %s. Accepted types: %s",
                            messageTemplate,
                            file.getOriginalFilename(),
                            acceptedTypes
                    )
            );
        } catch (IOException exception) {
            return buildViolation(context, "Unable to validate file content");
        }
    }

    private String detectContentType(MultipartFile file) throws IOException {

        try (TikaInputStream stream = TikaInputStream.get(file.getInputStream())) {
            return detector.detect(stream, new Metadata(), new ParseContext()).toString();
        }
    }

    private boolean buildViolation(
            ConstraintValidatorContext context,
            String message
    ) {
        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate(message).addConstraintViolation();

        return false;
    }
}
