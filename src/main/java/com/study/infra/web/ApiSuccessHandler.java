package com.study.infra.web;

import com.study.infra.common.dto.ResponseDto;
import org.springframework.core.MethodParameter;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

@ControllerAdvice
public class ApiSuccessHandler implements ResponseBodyAdvice<Object> {

    @Override
    public boolean supports(MethodParameter returnType, Class converterType) {
        // 모든 API 응답을 처리하도록 true 반환
        return true;
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType,
                                  org.springframework.http.MediaType selectedContentType,
                                  Class selectedConverterType,
                                  org.springframework.http.server.ServerHttpRequest request,
                                  org.springframework.http.server.ServerHttpResponse response) {

        // 이미 ResponseEntity 형태이면 변경하지 않고 그대로 반환
        if (body instanceof ResponseEntity) {
            return body;
        }

        return new ResponseDto("200", "요청이 성공적으로 처리되었습니다.", body);
    }
}
