package net.contextfw.demo.web.components;

import net.contextfw.web.application.component.Attribute;
import net.contextfw.web.commons.async.AsyncRunnable;
import net.contextfw.web.commons.async.Function;

public class Job extends AsyncRunnable<ProgressIndicator> {

    private final long started;
    private final long duration;
    
    public Job(ProgressIndicator component, long started, long duration) {
        super(component);
        this.started = started;
        this.duration = duration;
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
    public void run() {
        final float progress[] = new float[1];
        do {
            progress[0] = getProgress();
            executeScoped(new Function<ProgressIndicator, Void>() { public Void apply(ProgressIndicator in) {
                in.setProgress(progress[0]);
                return null;
            }});
            requestRefresh();
            if (progress[0] < 1) {
                try {
                    Thread.sleep(1500);
                } catch (InterruptedException e) {
                }
            }
        } while (progress[0] < 1);
    }
}
