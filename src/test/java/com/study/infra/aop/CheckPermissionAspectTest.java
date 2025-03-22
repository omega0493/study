package com.study.infra.aop;

import com.study.api.auth.constant.UserRole;
import com.study.entity.base.AbstractAuditableEntity;
import com.study.infra.common.exception.BusinessException;
import jakarta.persistence.EntityManager;
import org.aspectj.lang.JoinPoint;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThatException;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = CheckPermissionAspect.class)
class CheckPermissionAspectTest {

    @Autowired
    private CheckPermissionAspect sut;
    @MockitoBean
    private EntityIdParameterResolver resolver;
    @MockitoBean
    private EntityManager entityManager;

    @Mock
    private JoinPoint joinPoint;

    @Test
    void success1() {
        // given
        AbstractAuditableEntity entity = new AbstractAuditableEntity() {
            @Override
            public String getCreatedBy() {
                return "foo";
            }
        };

        UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                "foo", null, List.of(UserRole.USER));
        SecurityContextHolder.getContext().setAuthentication(auth);

        when(resolver.resolve(any()))
                .thenReturn(new ResolvedEntity(Object.class, 1L));
        when(entityManager.find(any(), any()))
                .thenReturn(entity);

        // when
        sut.checkPermission(joinPoint);
    }

    @Test
    void success2() {
        // given
        AbstractAuditableEntity entity = new AbstractAuditableEntity() {
            @Override
            public String getCreatedBy() {
                return "foo";
            }
        };

        UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                "foo3", null, List.of(UserRole.ADMIN));
        SecurityContextHolder.getContext().setAuthentication(auth);

        when(resolver.resolve(any()))
                .thenReturn(new ResolvedEntity(Object.class, 1L));
        when(entityManager.find(any(), any()))
                .thenReturn(entity);

        // when
        sut.checkPermission(joinPoint);
    }

    @Test
    void fail1() {
        // given
        AbstractAuditableEntity entity = new AbstractAuditableEntity() {
            @Override
            public String getCreatedBy() {
                return "foo";
            }
        };

        UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                "foo2", null, List.of(UserRole.USER));
        SecurityContextHolder.getContext().setAuthentication(auth);

        when(resolver.resolve(any()))
                .thenReturn(new ResolvedEntity(Object.class, 1L));
        when(entityManager.find(any(), any()))
                .thenReturn(entity);

        // when
        assertThatException()
                .isThrownBy(() -> sut.checkPermission(joinPoint))
                .isInstanceOf(BusinessException.class);
    }

}
