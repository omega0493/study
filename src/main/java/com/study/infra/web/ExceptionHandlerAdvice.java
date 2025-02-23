package com.study.infra.web;

import com.study.infra.common.dto.ResponseDto;
import com.study.infra.common.exception.BusinessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionHandlerAdvice {

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ResponseDto> handle(BusinessException e) {
        ResponseDto body = new ResponseDto(e.getBusinessError().getCode(), e.getBusinessError().getMessage(), null);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(body);
    }

    @ExceptionHandler
    public ResponseEntity<ResponseDto> handle(Throwable t) {
        ResponseDto body = new ResponseDto("internal_server_error", t.getMessage(), null);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(body);
    }

}
