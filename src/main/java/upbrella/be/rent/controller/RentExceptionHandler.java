package upbrella.be.rent.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import upbrella.be.rent.exception.*;
import upbrella.be.util.CustomErrorResponse;

@RestControllerAdvice
public class RentExceptionHandler {

    @ExceptionHandler(NonExistingUmbrellaForRentException.class)
    public ResponseEntity<CustomErrorResponse> nonExistingUmbrellaForRent(NonExistingUmbrellaForRentException e) {

        return ResponseEntity
                .badRequest()
                .body(new CustomErrorResponse(
                        "fail",
                        400,
                        e.getMessage()
                ));
    }

    @ExceptionHandler(NonExistingHistoryException.class)
    public ResponseEntity<CustomErrorResponse> nonExistingHistory(NonExistingHistoryException e) {

        return ResponseEntity
                .badRequest()
                .body(new CustomErrorResponse(
                        "fail",
                        400,
                        e.getMessage()
                ));
    }

    @ExceptionHandler(ExistingUmbrellaForRentException.class)
    public ResponseEntity<CustomErrorResponse> existingUmbrellaForRent(ExistingUmbrellaForRentException e) {

        return ResponseEntity
                .badRequest()
                .body(new CustomErrorResponse(
                        "fail",
                        400,
                        e.getMessage()
                ));
    }

    @ExceptionHandler(NotRefundedException.class)
    public ResponseEntity<CustomErrorResponse> notRefundedException(NotRefundedException e) {

        return ResponseEntity
                .badRequest()
                .body(new CustomErrorResponse(
                        "fail",
                        400,
                        e.getMessage()
                ));
    }

    @ExceptionHandler(NotAvailableUmbrellaException.class)
    public ResponseEntity<CustomErrorResponse> notAvailableUmbrellaException(NotAvailableUmbrellaException e) {

        return ResponseEntity
                .badRequest()
                .body(new CustomErrorResponse(
                        "fail",
                        400,
                        e.getMessage()
                ));
    }

    @ExceptionHandler(LockerCodeAlreadyIssuedException.class)
    public ResponseEntity<CustomErrorResponse> lockerCodeAlreadyIssuedException(LockerCodeAlreadyIssuedException e) {

        return ResponseEntity
                .status(429)
                .body(new CustomErrorResponse(
                        "Too Many Requests",
                        429,
                        e.getMessage()
                ));
    }
}
