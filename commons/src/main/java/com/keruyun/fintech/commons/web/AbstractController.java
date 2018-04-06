package com.keruyun.fintech.commons.web;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.keruyun.fintech.commons.Constant;
import com.keruyun.fintech.commons.ErrorCode;
import com.keruyun.fintech.commons.Utils;
import com.keruyun.fintech.commons.exception.BizException;
import com.keruyun.fintech.commons.exception.CommonException;
import com.keruyun.fintech.commons.exception.HandleTCCommonException;
import com.keruyun.fintech.commons.exception.VerifyFailedException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientException;

import java.util.stream.Collectors;

@Slf4j
@RestController
public class AbstractController {
@Autowired
private HandleTCCommonException handleCommonException;

    @ExceptionHandler(BizException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public Response<?> handleException(BizException ex) {
        log.error(Utils.getExceptionInfo(ex, Utils.KERY_PACKAGE_PRE, 1));
        Response response = ex.getResponse();
        return response;
    }

    @ExceptionHandler(DuplicateKeyException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public Response<?> handleException(DuplicateKeyException ex) {
        log.error(Utils.getExceptionInfo(ex, Utils.KERY_PACKAGE_PRE, 1));
        Response<?> response = new Response<>();
        response.setErrorCode(ErrorCode.DATA_EXISTS);
        return response;
    }

    @ExceptionHandler(DataAccessException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public Response<?> handleException(DataAccessException ex) {
        log.error(Utils.getExceptionInfo(ex, Utils.KERY_PACKAGE_PRE, 1));
        Response<?> response = new Response<>();
        response.setErrorCode(ErrorCode.DATA_ERROR);
        return response;
    }

    @ExceptionHandler({HttpMessageNotReadableException.class, JsonMappingException.class})
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public Response<?> handleException(Exception ex) {
        log.error(Utils.getExceptionInfo(ex, Utils.KERY_PACKAGE_PRE, 1));
        Response<?> response = new Response<>();
        response.setErrorCode(ErrorCode.READ_JSON_ERROR);
        return response;
    }

    @ExceptionHandler(ResourceAccessException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public Response<?> handleException(ResourceAccessException ex) {
        log.error(Utils.getExceptionInfo(ex, Utils.KERY_PACKAGE_PRE, 1));
        Response<?> response = new Response<>();
        response.setErrorCode(ErrorCode.THIRD_NET_ERROR);
        return response;
    }

    @ExceptionHandler(RestClientException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public Response<?> handleException(RestClientException ex) {
        log.error(Utils.getExceptionInfo(ex, Utils.KERY_PACKAGE_PRE, 1));
        Response<?> response = new Response<>();
        response.setErrorCode(ErrorCode.THIRD_API_ERROR);
        return response;
    }

    @ExceptionHandler(Throwable.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public Response<?> handleException(Throwable ex) {
        log.error(Utils.getExceptionInfo(ex, Utils.KERY_PACKAGE_PRE, 1));
        Response<?> response = new Response<>();
        response.setErrorCode(ErrorCode.INTERNAL_ERROR);
        return response;
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public Response<?> handleException(MethodArgumentNotValidException ex) {
        log.error(Utils.getExceptionInfo(ex, Utils.KERY_PACKAGE_PRE, 1));
        Response<?> response = new Response<>();
        response.setErrorCode(ErrorCode.VALIDATE_ERROR);
        response.setMessage(ex.getBindingResult().getAllErrors().stream().map(ObjectError::getDefaultMessage).collect(Collectors.joining(Constant.SYMBOL_SEMICOLON)));
        return response;
    }

    @ExceptionHandler(VerifyFailedException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public Response<?> handleException(VerifyFailedException ex) {
        log.error(Utils.getExceptionInfo(ex, Utils.KERY_PACKAGE_PRE, 1));
        Response<?> response = new Response<>();
        response.setErrorCode(ex.getErrorCode());

        return response;
    }


    @ExceptionHandler(CommonException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public Response<?> handleException(CommonException ex) {
        log.error(Utils.getExceptionInfo(ex, Utils.KERY_PACKAGE_PRE, 1));
        Response<?> response = new Response<>();
        response.setErrorCode(handleCommonException.handleException(ex));
        response.setMessage(ex.getErrMessage());
        return response;
    }

}
