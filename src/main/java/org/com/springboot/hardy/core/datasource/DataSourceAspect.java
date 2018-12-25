package org.com.springboot.hardy.core.datasource;

import java.lang.reflect.Method;

import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * @ClassName DataSourceAspect
 * @Description AOP实现数据源动态代理
 * @author Hardy
 * @Date 2018年12月20日 下午3:26:56
 * @version 1.0.0
 */
@Component
@Aspect
public class DataSourceAspect {
    
    private static final Logger logger = LoggerFactory.getLogger(DataSourceAspect.class);
    
    //切换放在mapper接口的方法上，所以这里要配置AOP切面的切入点
    @Pointcut("execution( * org.com.springboot.hardy.core.mapper.*.*(..))")
    public void dataSourcePointCut() {
        
    }

    @Before("dataSourcePointCut()")
    public void before(JoinPoint joinPoint) {
        Object target = joinPoint.getTarget();
        String method = joinPoint.getSignature().getName();
        Class<?>[] clazz = target.getClass().getInterfaces();
        Class<?>[] parameterTypes = ((MethodSignature) joinPoint.getSignature()).getMethod().getParameterTypes();
        try {
            Method m = clazz[0].getMethod(method, parameterTypes);
            //如果方法上存在切换数据源的注解，则根据注解内容进行数据源切换
            if (m != null && m.isAnnotationPresent(DataSource.class)) {
                DataSource data = m.getAnnotation(DataSource.class);
                if(data != null){
                    String dataSource = data.value().getType();
                    logger.info("Charege datasource is 【{}】,user method > {}",dataSource,method);
                    if(StringUtils.isNoneBlank(dataSource)){
                        DynamicDataSource.setDataSource(dataSource);
                    }
                }
            }else{
                logger.info("Charege datasource fail,User default dataSource");
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("current thread " + Thread.currentThread().getName() + " add data to ThreadLocal error", e);
        }
    }
    
    //执行完切面后，将线程共享中的数据源名称清空
    @After("dataSourcePointCut()")
    public void after(JoinPoint joinPoint){
        DynamicDataSource.clearDataSource();
    }
    
    
    
}
