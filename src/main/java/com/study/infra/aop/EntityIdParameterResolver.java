package com.study.infra.aop;

import jakarta.annotation.Nullable;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Component;

import java.lang.reflect.Parameter;

@Component
class EntityIdParameterResolver {

    @Nullable
    public ResolvedEntity resolve(JoinPoint joinPoint) {
        Signature signature = joinPoint.getSignature();
        if (!(signature instanceof MethodSignature ms)) {
            return null;
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
            return null;
        }

        return new ResolvedEntity(entityType, entityId);
    }

}
