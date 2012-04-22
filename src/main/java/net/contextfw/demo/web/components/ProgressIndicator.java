package net.contextfw.demo.web.components;

import net.contextfw.web.application.component.Attribute;

public class ProgressIndicator extends JsComponent {
    @Attribute
    private float progress = 0;
    
    @Attribute
    private final String name;

    public ProgressIndicator(String name) {
        this.name = name;
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
    
    public void refresh() {
        jsUpdate("updateProgress", progress);
        super.refresh();
    }
    
    public JsInit jsInit() {
        return new JsInit(this, progress);
    }
}
