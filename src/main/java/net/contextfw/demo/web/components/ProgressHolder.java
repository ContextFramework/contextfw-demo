package net.contextfw.demo.web.components;

import java.util.ArrayList;
import java.util.List;

import net.contextfw.web.application.component.ComponentRegister;
import net.contextfw.web.application.component.Element;
import net.contextfw.web.application.remote.Remoted;

import com.google.inject.Inject;
import com.google.inject.Provider;

public class ProgressHolder extends JsComponent {

    @Inject
    private Provider<ComponentRegister> componentRegister;
    
    @Inject
    private Updater updater;
    
    @Element
    private List<ProgressIndicator> indicators = new ArrayList<ProgressIndicator>();

    @Remoted
    public Job addProgress(String name, long duration) {
        if (indicators.size() < 10 && duration > 0 && duration <= 30) {
            long now = System.currentTimeMillis();
            ProgressIndicator indicator = registerChild(new ProgressIndicator(name, updater));
            indicators.add(indicator);
            refresh();
            return new Job(indicator.getId(), now, duration*1000, componentRegister);
        } else {
            return null;
        }
    }
    
}
