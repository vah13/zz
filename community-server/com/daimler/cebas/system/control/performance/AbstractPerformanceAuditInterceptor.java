/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.daimler.cebas.system.control.performance.PerformanceAuditRequest
 *  org.aspectj.lang.JoinPoint
 *  org.aspectj.lang.reflect.CodeSignature
 *  org.aspectj.lang.reflect.MethodSignature
 *  org.springframework.beans.factory.annotation.Autowired
 *  org.springframework.beans.factory.annotation.Value
 */
package com.daimler.cebas.system.control.performance;

import com.daimler.cebas.system.control.performance.PerformanceAuditRequest;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.reflect.CodeSignature;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

public abstract class AbstractPerformanceAuditInterceptor {
    @Value(value="${PERSIST_PERFORMANCE_AUDIT_ENTRY}")
    private String enablePerformanceAuditEntriesPersistance;
    @Autowired
    private PerformanceAuditRequest performanceAuditRequest;

    public void start(JoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature)joinPoint.getSignature();
        Method method = signature.getMethod();
        HashMap<String, Object> parametersMap = new HashMap<String, Object>();
        Object[] requestArguments = joinPoint.getArgs();
        String[] paramenterName = ((CodeSignature)joinPoint.getSignature()).getParameterNames();
        int i = 0;
        while (true) {
            if (i >= Arrays.asList(requestArguments).size()) {
                this.performanceAuditRequest.setCorrelationId(String.valueOf(parametersMap.get("correlationid")));
                parametersMap.clear();
                this.performanceAuditRequest.setClassName(joinPoint.getTarget().getClass().getSimpleName());
                this.performanceAuditRequest.setMethodName(method.getName());
                this.performanceAuditRequest.setStartTime(new Date());
                return;
            }
            parametersMap.put(Arrays.asList(paramenterName).get(i), Arrays.asList(requestArguments).get(i));
            ++i;
        }
    }

    public void end(JoinPoint joinPoint) {
        this.performanceAuditRequest.setEndTime(new Date());
        if (!Boolean.TRUE.toString().equalsIgnoreCase(this.enablePerformanceAuditEntriesPersistance)) return;
        this.performanceAuditRequest.add();
    }
}
