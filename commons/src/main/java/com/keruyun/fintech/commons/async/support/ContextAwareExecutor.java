package com.keruyun.fintech.commons.async.support;

import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.RejectedExecutionException;

/**
 * 支持MDC的线程池任务执行器ThreadPoolTaskExecutor
 * @author shuw
 * @version 1.0
 * @date 2017/2/24 10:36
 */
public class ContextAwareExecutor extends ThreadPoolTaskExecutor {

    /**
     * Executes the given command at some time in the future.  The command
     * may execute in a new thread, in a pooled thread, or in the calling
     * thread, at the discretion of the {@code Executor} implementation.
     *
     * @param command the runnable task
     * @throws RejectedExecutionException if this task cannot be
     *                                    accepted for execution
     * @throws NullPointerException       if command is null
     */
    @Override
    public void execute(Runnable command) {
        Runnable task = new ContextAwareRunnable(command);
        super.execute(task);
    }
    @Override
    public <T> Future<T> submit(Callable<T> command) {
        ContextAwareCallable task = new ContextAwareCallable(command);
        return super.submit(task);
    }

    @Override
    public Future<?> submit(Runnable command) {
        Runnable task = new ContextAwareRunnable(command);
        return super.submit(task);
    }
}
