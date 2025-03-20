package com.study.infra.aop;

import com.study.api.auth.constant.UserRole;
import com.study.api.auth.model.UserModel;
import com.study.entity.base.AbstractAuditableEntity;
import com.study.infra.common.exception.BusinessError;
import com.study.infra.common.exception.BusinessException;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.lang.reflect.Parameter;
import java.util.Objects;

@Aspect
@Component
@RequiredArgsConstructor
class CheckPermissionAspect {

    private final EntityManager entityManager;

    @Before("@annotation(CheckPermission)")
    void logExecutionTime(JoinPoint joinPoint) throws Throwable {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            return;
        }

        if (!(authentication.getDetails() instanceof UserModel userModel)) {
            return;
        }

        Signature signature = joinPoint.getSignature();
        if (!(signature instanceof MethodSignature ms)) {
            return;
        }

        Parameter[] parameters = ms.getMethod().getParameters();
        Object[] args = joinPoint.getArgs();

        Class<?> entityType = null;
        Object entityId = null;
        for (int i = 0; i < parameters.length; i++) {
            Parameter parameter = parameters[i];
            Object arg = args[i];

            EntityId annotation = AnnotationUtils.findAnnotation(parameter, EntityId.class);
            if (annotation != null) {
                entityType = annotation.value();
                entityId = arg;
                break;
            }
        }

        if (entityType == null || entityId == null) {
            return;
        }

        Object entity = entityManager.find(entityType, entityId);
        if (!(entity instanceof AbstractAuditableEntity auditable)) {
            return;
        }

        String currentUserName = userModel.getUserName();
        String targetUserName = auditable.getCreatedBy();

        // 동일한 사용자면 통과
        if (Objects.equals(currentUserName, targetUserName)) {
            return;
        }

        if (userModel.getUserRole() == UserRole.ADMIN) {
            return;
        }

        throw new BusinessException(BusinessError.WRONG_PERMISSION);
    }

}
