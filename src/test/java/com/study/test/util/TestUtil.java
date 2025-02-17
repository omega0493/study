package com.study.test.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.json.JsonMapper;

public class TestUtil {

    public static String toJson(Object object) throws JsonProcessingException {
        JsonMapper jsonMapper = JsonMapper.builder().build();
        return jsonMapper.writeValueAsString(object);
    }
}
