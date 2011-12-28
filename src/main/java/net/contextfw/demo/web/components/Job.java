package net.contextfw.demo.web.components;

import net.contextfw.web.application.component.Attribute;
import net.contextfw.web.application.component.ComponentRegister;
import net.contextfw.web.application.scope.Execution;
import net.contextfw.web.application.scope.PageScopedExecutor;

import com.google.inject.Provider;

public class Job implements Execution {

    private final Provider<ComponentRegister> componentRegister;
    
    private final long started;
    private final long duration;
    private final String id;
    
    public Job(String id, 
               long started, 
               long duration, 
               Provider<ComponentRegister> componentRegister) {
        
        this.started = started;
        this.duration = duration;
        this.id = id;
        this.componentRegister = componentRegister;
    }
    
    @Attribute
    public float getProgress() {
        long now = System.currentTimeMillis();
        if (now == started) {
            return 0;
        } else {
            float progress = (float)(now-started) / (float)(duration);
            return progress >= 1 ? 1 : progress;
        }
    }

    @Override
    public void execute(PageScopedExecutor executor) {
        final float progress[] = new float[1];
        do {
            progress[0] = getProgress();
            executor.execute(new Runnable() {
                public void run() {
                    componentRegister.get()
                        .findComponent(ProgressIndicator.class, id)
                        .setProgress(progress[0]);
                }
            });
            if (progress[0] < 1) {
                try {
                    Thread.sleep(1500);
                } catch (InterruptedException e) {
                }
            }
        } while (progress[0] < 1);
    }
}
