package com.cyber.infrastructure.exception;

import com.cyber.domain.constant.HttpResultCode;
import com.cyber.domain.entity.Response;
import com.cyber.domain.exception.BusinessException;
import com.cyber.domain.exception.SystemException;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MultipartException;
import org.yaml.snakeyaml.constructor.DuplicateKeyException;

import javax.validation.UnexpectedTypeException;
import java.sql.SQLException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger LOGGING = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(value = {MissingServletRequestParameterException.class})
    public Response missingServletRequestParameterException(MissingServletRequestParameterException exception) {
        LOGGING.error("missing servlet request parameter exception {} ...", exception);
        return Response.fail(HttpResultCode.PARAM_ERROR);
    }

    @ExceptionHandler(value = {MethodArgumentNotValidException.class})
    public Response argumentNotValidException(MethodArgumentNotValidException exception) {
        LOGGING.error("method argument not valid exception {} ...", exception);
        return Response.fail(HttpResultCode.VALIDATE_ERROR.getCode(), exception.getBindingResult().getFieldError().getDefaultMessage());
    }

    @ExceptionHandler(value = {BindException.class})
    public Response bindException(BindException exception) {
        LOGGING.error("bind exception {} ...", exception);
        return Response.fail(HttpResultCode.VALIDATE_ERROR.getCode(), exception.getBindingResult().getFieldError().getDefaultMessage());
    }

    @ExceptionHandler(value = {UnexpectedTypeException.class})
    public Response unexpectedTypeException(UnexpectedTypeException exception) {
        LOGGING.error("unexpected type exception {} ...", exception);
        return Response.fail(HttpResultCode.VALIDATE_ERROR);
    }

    @ExceptionHandler(value = {HttpRequestMethodNotSupportedException.class})
    public Response methodNotSupportedException(HttpRequestMethodNotSupportedException exception) {
        LOGGING.error("http request method not supported exception {} ...", exception);
        return Response.fail(HttpResultCode.REST_METHOD_NOT_SUPPORT);
    }

    @ExceptionHandler(value = {MultipartException.class})
    public Response uploadFileLimitException(MultipartException exception) {
        LOGGING.error("upload file limit exception {} ...", exception);
        return Response.fail(HttpResultCode.FILE_UPLOAD_ERROR);
    }

    @ExceptionHandler(value = {DuplicateKeyException.class})
    public Response duplicateKeyException(DuplicateKeyException exception) {
        LOGGING.error("duplicate key exception {} ...", exception);
        return Response.fail(HttpResultCode.BAD_SQL_ERROR);
    }

    @ExceptionHandler(value = {SQLException.class})
    public Response sqlException(DuplicateKeyException exception) {
        LOGGING.error("sql exception {} ... ", exception);
        return Response.fail(HttpResultCode.BAD_SQL_ERROR);
    }

    @ExceptionHandler(value = {BusinessException.class})
    public Response businessException(BusinessException exception) {
        LOGGING.error("business exception {} ... ", exception);
        return Response.fail(exception.getCode() == 0 ? HttpResultCode.SERVER_ERROR.getCode() : exception.getCode(),
                StringUtils.isBlank(exception.getMessage()) ? HttpResultCode.SERVER_ERROR.getMessage() : exception.getMessage());
    }

    @ExceptionHandler(value = {SystemException.class})
    public Response systemException(SystemException exception) {
        LOGGING.error("system exception {} ... ", exception);
        return Response.fail(HttpResultCode.SERVER_ERROR);
    }

    @ExceptionHandler(value = {Exception.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Response exception(Exception exception) {
        LOGGING.error("exception {} ... ", exception);
        return Response.fail(HttpResultCode.SERVER_ERROR);
    }

    @ExceptionHandler(value = {Throwable.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Response throwable(Throwable throwable) {
        LOGGING.error("throwable {} ... ", throwable);
        return Response.fail(HttpResultCode.SERVER_ERROR);
    }
}