package net.contextfw.demo.web.components;

import net.contextfw.web.application.component.Attribute;
import net.contextfw.web.application.scope.Execution;

public class ProgressIndicator extends JsComponent {

    @Attribute
    private float progress = 0;
    
    @Attribute
    private final String name;

    private final Updater updater;
    
    private final UpdaterTask updaterTask = new UpdaterTask() { public Execution run() {
        checkProgress();
        return null;
    }}; 
    
    public ProgressIndicator(String name, Updater updater) {
        this.name = name;
        this.updater = updater;
        updater.addTask(updaterTask);
    }

    public float getProgress() {
        return progress;
    }

    public void setProgress(float progress) {
        this.progress = progress;
    }

    public String getName() {
        return name;
    }
    
    private void checkProgress() {
        jsUpdate("updateProgress", progress);
        if (progress == 1) {
            updater.removeTask(updaterTask);
        }
    }
    
    public JsInit jsInit() {
        return new JsInit(this, progress);
    }
}
