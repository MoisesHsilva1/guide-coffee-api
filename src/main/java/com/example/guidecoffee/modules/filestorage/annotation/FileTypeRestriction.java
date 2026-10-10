package com.example.guidecoffee.modules.filestorage.annotation;

import com.example.guidecoffee.modules.filestorage.validation.FileTypeRestrictionValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Constraint(validatedBy = FileTypeRestrictionValidator.class)
@Target({
        ElementType.FIELD,
        ElementType.PARAMETER,
        ElementType.ANNOTATION_TYPE
})
@Retention(RetentionPolicy.RUNTIME)
public @interface FileTypeRestriction {

    String[] acceptedTypes();

    String message() default "File is not allowed";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
