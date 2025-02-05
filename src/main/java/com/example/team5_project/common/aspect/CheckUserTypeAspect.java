package com.example.team5_project.common.aspect;

import com.example.team5_project.common.exception.MemberException;
import com.example.team5_project.common.exception.errorcode.MemberErrorCode;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Arrays;

@Slf4j
@Component
@Aspect
@RequiredArgsConstructor
public class CheckUserTypeAspect {

    private final HttpServletRequest servletRequest;

    @Around("@annotation(com.example.team5_project.common.aspect.AuthCheck)")
    public Object checkUserType(ProceedingJoinPoint joinPoint) throws Throwable{

        String userType = (String) servletRequest.getAttribute("user_type");
        Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();
        AuthCheck authCheck = method.getAnnotation(AuthCheck.class);

        if (userType == null) {
            throw new MemberException(MemberErrorCode.USER_TYPE_MISSING);
        }

        if (Arrays.stream(authCheck.value()).noneMatch(userType::equals)) {
            throw new MemberException(MemberErrorCode.ACCESS_DENIED);
        }

        log.info("권한 확인 완료. 요청 권한: {}, 필요 권한: {}", userType, authCheck.value());

        return joinPoint.proceed();
    }
}
