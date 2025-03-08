package com.study.test.config;

import org.springframework.boot.test.context.TestComponent;
import org.springframework.data.domain.AuditorAware;

import java.util.Optional;

@TestComponent
public class TestAuditorAwareImpl implements AuditorAware<String> {

    @Override
    public Optional<String> getCurrentAuditor() {
        return Optional.of("anonymous");
    }

}
