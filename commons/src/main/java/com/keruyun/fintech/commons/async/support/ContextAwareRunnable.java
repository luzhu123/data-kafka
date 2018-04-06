package com.keruyun.fintech.commons.async.support;

import org.slf4j.MDC;

import java.util.Map;

/**
 * @author shuw
 * @version 1.0
 * @date 2017/2/24 10:50
 */
public class ContextAwareRunnable implements Runnable {
    private Runnable command;
    private Map<String, String> callerContext;//请求执行任务的线程MDC内容

    public ContextAwareRunnable(Runnable command) {
        this.command = command;
        callerContext = MDC.getCopyOfContextMap();
    }

    @Override
    public void run() {
        MDC.clear();
        if (callerContext != null) {
            // set the desired context that was present at point of calling execute
            MDC.setContextMap(callerContext);
        }
        // execute the command
        command.run();

        MDC.clear();
    }
}
