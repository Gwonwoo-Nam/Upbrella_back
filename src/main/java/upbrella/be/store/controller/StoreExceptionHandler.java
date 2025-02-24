package upbrella.be.store.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import upbrella.be.store.exception.*;
import upbrella.be.util.CustomErrorResponse;

@RestControllerAdvice
public class StoreExceptionHandler {

    @ExceptionHandler(NonExistingStoreDetailException.class)
    public ResponseEntity<CustomErrorResponse> nonExistingStoreDetail(NonExistingStoreDetailException ex) {

        return ResponseEntity
                .badRequest()
                .body(new CustomErrorResponse(
                        "not found",
                        404,
                        ex.getMessage()));
    }

    @ExceptionHandler(DeletedStoreDetailException.class)
    public ResponseEntity<CustomErrorResponse> deletedStoreDetail(DeletedStoreDetailException ex) {

        return ResponseEntity
                .badRequest()
                .body(new CustomErrorResponse(
                        "not found",
                        404,
                        ex.getMessage()));
    }

    @ExceptionHandler(NonExistingStoreMetaException.class)
    public ResponseEntity<CustomErrorResponse> nonExistingStoreMeta(NonExistingStoreMetaException ex) {

        return ResponseEntity
                .badRequest()
                .body(new CustomErrorResponse(
                        "not found",
                        404,
                        ex.getMessage()));
    }

    @ExceptionHandler(NonExistingStoreImageException.class)
    public ResponseEntity<CustomErrorResponse> nonExistingStoreImage(NonExistingStoreImageException ex) {

        return ResponseEntity
                .badRequest()
                .body(new CustomErrorResponse(
                        "not found",
                        404,
                        ex.getMessage()));
    }

    @ExceptionHandler(IncorrectClassificationException.class)
    public ResponseEntity<CustomErrorResponse> incorrectClassification(IncorrectClassificationException ex) {

        return ResponseEntity
                .badRequest()
                .body(new CustomErrorResponse(
                        "bad request",
                        400,
                        ex.getMessage()));
    }

    @ExceptionHandler(AssignedClassificationException.class)
    public ResponseEntity<CustomErrorResponse> assignedClassification(AssignedClassificationException ex) {

        return ResponseEntity
                .badRequest()
                .body(new CustomErrorResponse(
                        "bad request",
                        400,
                        ex.getMessage()));
    }

    @ExceptionHandler(NonExistingClassificationException.class)
    public ResponseEntity<CustomErrorResponse> nonExistingClassification(NonExistingClassificationException ex) {

        return ResponseEntity
                .badRequest()
                .body(new CustomErrorResponse(
                        "not found",
                        404,
                        ex.getMessage()));
    }
}
