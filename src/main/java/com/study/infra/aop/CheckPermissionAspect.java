package com.study.infra.aop;

import com.study.api.auth.constant.UserRole;
import com.study.entity.base.AbstractAuditableEntity;
import com.study.infra.common.exception.BusinessError;
import com.study.infra.common.exception.BusinessException;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Objects;

@Aspect
@Component
@RequiredArgsConstructor
class CheckPermissionAspect {

    private final EntityIdParameterResolver resolver;
    private final EntityManager entityManager;

    @Before("@annotation(CheckPermission)")
    void checkPermission(JoinPoint joinPoint) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            return;
        }

        ResolvedEntity resolvedEntity = resolver.resolve(joinPoint);
        if (resolvedEntity == null) {
            return;
        }

        Object entity = entityManager.find(resolvedEntity.type(), resolvedEntity.id());
        if (!(entity instanceof AbstractAuditableEntity auditable)) {
            return;
        }

        String currentUserName = authentication.getName();
        String targetUserName = auditable.getCreatedBy();

        // 동일한 사용자면 통과
        if (Objects.equals(currentUserName, targetUserName)) {
            return;
        }

        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        if (authorities.contains(UserRole.ADMIN)) {
            return;
        }

        throw new BusinessException(BusinessError.WRONG_PERMISSION);
    }

}
