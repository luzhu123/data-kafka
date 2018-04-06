package com.keruyun.fintech.commons.async.support;

import org.slf4j.MDC;

import java.util.Map;
import java.util.concurrent.Callable;

/**
 * @author shuw
 * @version 1.0
 * @date 2017/2/24 11:24
 */
public class ContextAwareCallable<T> implements Callable {
    private Callable<T> command;
    private Map<String, String> callerContext;//请求执行任务的线程MDC内容

    public ContextAwareCallable(Callable<T> command) {
        this.command = command;
        callerContext = MDC.getCopyOfContextMap();
    }

    @Override
    public T call() throws Exception {
        MDC.clear();
        if (callerContext != null) {
            // set the desired context that was present at point of calling execute
            MDC.setContextMap(callerContext);
        }
        // execute the command
        T rs = command.call();
        MDC.clear();

        return rs;
    }
}
