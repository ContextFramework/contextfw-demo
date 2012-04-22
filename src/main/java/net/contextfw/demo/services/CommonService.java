package net.contextfw.demo.services;

import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import net.contextfw.web.commons.async.AsyncRunnable;
import net.contextfw.web.commons.async.AsyncService;

import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class CommonService {

    private ScheduledThreadPoolExecutor pool = new ScheduledThreadPoolExecutor(20);
    
    private final AsyncService asyncService;

    @Inject
    public CommonService(AsyncService asyncService) {
        this.asyncService = asyncService;
    }
    
    public void executeAsync(AsyncRunnable<?> runnable, long delay) {
        if (delay == 0) {
            pool.execute(asyncService.prepare(runnable));
        } else {
            pool.schedule(asyncService.prepare(runnable), delay, TimeUnit.MILLISECONDS);
        }
    }
    
}
