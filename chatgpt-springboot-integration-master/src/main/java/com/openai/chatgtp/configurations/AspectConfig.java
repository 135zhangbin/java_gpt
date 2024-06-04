package com.openai.chatgtp.configurations;

import com.alibaba.fastjson.JSONObject;
//import com.cdfgrouop.uploadposition.utils.IpUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.ExtendedServletRequestDataBinder;

import java.lang.reflect.Parameter;
import java.util.Optional;

/**
 * @ClassName: AspectConfig
 * @Description: aop切面日志打印
 * @Author: s
 * @Date: 2023/5/22 8:44
 */
@Aspect
@Component
@Order(1)
@Slf4j
public class AspectConfig {
    @Around("@within(org.springframework.web.bind.annotation.RestController)" +
            "||@within(org.springframework.stereotype.Controller)")
    public Object after(ProceedingJoinPoint joinPoint) throws Throwable {
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = requestAttributes.getRequest();
        boolean isLog=!joinPoint.getSignature().getName().equals("InitBinder");
        if (isLog){
            Parameter[] parameters = ((MethodSignature) joinPoint.getSignature()).getMethod().getParameters();
            Object[] args = joinPoint.getArgs();
            JSONObject obj = new JSONObject();
            for (int i = 0; i < args.length; i++) {
                if ((args[i] instanceof MultipartFile) || (args[i] instanceof ExtendedServletRequestDataBinder)
                        || (args[i] instanceof HttpServletResponse)
                        || (args[i] instanceof HttpServletRequest)){
                    continue;
                }
                obj.put(parameters[i].getName(),args[i]);
            }
//            String ip = IpUtils.getIp(request);
            String ip = "";
            String url = Optional.ofNullable(request.getRequestURI().toString()).orElse(null);
            try {
                log.info("请求ip:{},请求地址:{},方式:{},类方法:{},类方法参数:{}", ip,
                        url,
                        request.getMethod(),
                        joinPoint.getSignature(),
                        obj.toJSONString());
            }catch (Exception e){
                log.error("请求ip:{},请求地址:{},方式:{},类方法:{},类方法参数:[aop打印失败]", ip,
                        url,
                        request.getMethod(),
                        joinPoint.getSignature());
            }
            long start = System.currentTimeMillis();
            Object result = joinPoint.proceed(joinPoint.getArgs());
            long end = System.currentTimeMillis();
            log.info("执行耗时:{}ms", end - start);
            return result;
        }
        return joinPoint.proceed(joinPoint.getArgs());
    }

}

