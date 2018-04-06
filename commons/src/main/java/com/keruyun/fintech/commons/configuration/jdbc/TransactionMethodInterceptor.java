package com.keruyun.fintech.commons.configuration.jdbc;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Method;

/**
 * 读写分离方法拦截
 *
 * @author tietang
 *         Created by tietang on 2015/6/16 13:43.
 */
public class TransactionMethodInterceptor implements MethodInterceptor {
    private static final Logger logger = LoggerFactory.getLogger(TransactionMethodInterceptor.class);

    @Override
    public Object invoke(MethodInvocation methodInvocation) throws Throwable {
        Object bean = methodInvocation.getThis();
        Method method = methodInvocation.getMethod();
        Transactional beanTransactional = bean.getClass().getAnnotation(Transactional.class);
        Transactional methodTransactional = method.getAnnotation(Transactional.class);
        boolean isRead = false;
        if (methodTransactional != null) {
            isRead = methodTransactional.readOnly();
        } else  if (beanTransactional != null) {
            isRead = beanTransactional.readOnly();
        }
        //如果是只读
        if (isRead) {
            JdbcContextHolder.setReadOnly();
            logger.debug("设置{}.{}为从库", bean.getClass(), method.getName());
        } else {
            JdbcContextHolder.setWriteRead();
            if (beanTransactional != null || methodTransactional != null) {
                logger.debug("设置{}.{}为主库", bean.getClass(), method.getName());
            }
        }

        try {
            return methodInvocation.proceed();
        } finally {
            JdbcContextHolder.reset();
        }
    }
}
