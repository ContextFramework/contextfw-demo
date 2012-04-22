package net.contextfw.demo.web.components;

import java.util.ArrayList;
import java.util.List;

import net.contextfw.demo.services.CommonService;
import net.contextfw.web.application.component.Element;
import net.contextfw.web.application.remote.Remoted;
import net.contextfw.web.application.scope.Provided;

import com.google.inject.Inject;

public class ProgressHolder extends JsComponent {

    @Inject
    @Provided
    private CommonService commonService;
    
    @Element
    private List<ProgressIndicator> indicators = new ArrayList<ProgressIndicator>();

    @Remoted
    public void addProgress(String name, long duration) {
        if (indicators.size() < 10 && duration > 0 && duration <= 30) {
            long now = System.currentTimeMillis();
            ProgressIndicator indicator = registerChild(new ProgressIndicator(name));
            indicators.add(indicator);
            commonService.executeAsync(new Job(indicator, now, duration*1000), 0);
            refresh();
        }
    }
    
}
