package com.simo333.driver.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.firewall.RequestRejectedException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import javax.validation.ConstraintViolationException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@RestControllerAdvice
@Slf4j
public class ApiExceptionHandler {

    @ExceptionHandler(value = AccessDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ApiExceptionResponse handleAccessDeniedException(WebRequest request) {
        String message = "Access denied.";
        return new ApiExceptionResponse(
                HttpStatus.FORBIDDEN,
                message,
                request.getDescription(false));
    }

    @ExceptionHandler(value = HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiExceptionResponse handleHttpMessageNotReadableException(HttpMessageNotReadableException ex, WebRequest request) {
        String message = "Request body is missing or some of inputs are incorrect.";
        log.error(ex.getLocalizedMessage());
        return new ApiExceptionResponse(
                HttpStatus.BAD_REQUEST,
                message,
                request.getDescription(false));
    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        Map<String, String> errorsMap = new HashMap<>();
        e.getBindingResult().getFieldErrors().forEach(error -> errorsMap.put(error.getField(), error.getDefaultMessage()));
        return new ResponseEntity<>(errorsMap, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = ConstraintViolationException.class)
    public ResponseEntity<Object> handleConstraintViolationException(ConstraintViolationException e) {
        Map<String, String> errorMap = new HashMap<>();
        e.getConstraintViolations().forEach(error -> errorMap.put(error.getPropertyPath().toString(), error.getMessage()));
        return new ResponseEntity<>(errorMap, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiExceptionResponse handleResourceNotFoundException(ResourceNotFoundException ex, WebRequest request) {
        return new ApiExceptionResponse(
                HttpStatus.NOT_FOUND,
                ex.getMessage(),
                request.getDescription(false));
    }

    @ExceptionHandler(value = SQLIntegrityConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiExceptionResponse handleSQLIntegrityConstraintViolationException(WebRequest request, SQLIntegrityConstraintViolationException e) {
        String message = "Cannot delete or update a parent row: a foreign key constraint fails.";
        return new ApiExceptionResponse(
                HttpStatus.BAD_REQUEST,
                message,
                request.getDescription(false));
    }

    @ExceptionHandler(value = HttpRequestMethodNotSupportedException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiExceptionResponse handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException ex, WebRequest request) {
        String message = String.format("'%s' request method not supported.", ex.getMethod());
        return new ApiExceptionResponse(
                HttpStatus.BAD_REQUEST,
                message,
                request.getDescription(false));
    }

    @ExceptionHandler(value = HttpMediaTypeNotSupportedException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiExceptionResponse handleHttpMediaTypeNotSupportedException(HttpMediaTypeNotSupportedException ex, WebRequest request) {
        return new ApiExceptionResponse(
                HttpStatus.BAD_REQUEST,
                ex.getMessage(),
                request.getDescription(false));
    }

    @ExceptionHandler(value = MethodArgumentTypeMismatchException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiExceptionResponse handleHttpRequestMethodNotSupportedException(MethodArgumentTypeMismatchException ex, WebRequest request) {
        String message = String.format("The parameter '%s' of value '%s' could not be converted to type '%s'",
                ex.getName(), ex.getValue(), Objects.requireNonNull(ex.getRequiredType()).getSimpleName());
        return new ApiExceptionResponse(
                HttpStatus.BAD_REQUEST,
                message,
                request.getDescription(false));
    }

    @ExceptionHandler(value = RequestRejectedException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiExceptionResponse handleRequestRejectedException(RequestRejectedException ex, WebRequest request) {
        return new ApiExceptionResponse(
                HttpStatus.BAD_REQUEST,
                ex.getMessage(),
                request.getDescription(false));
    }

    @ExceptionHandler(value = IllegalQuestionStateException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiExceptionResponse handleInvalidAnswersException(IllegalQuestionStateException ex, WebRequest request) {
        return new ApiExceptionResponse(
                HttpStatus.BAD_REQUEST,
                ex.getMessage(),
                request.getDescription(false));
    }

    @ExceptionHandler(value = UniqueViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiExceptionResponse handleInvalidAnswersException(UniqueViolationException ex, WebRequest request) {
        return new ApiExceptionResponse(
                HttpStatus.BAD_REQUEST,
                ex.getMessage(),
                request.getDescription(false));
    }

}
