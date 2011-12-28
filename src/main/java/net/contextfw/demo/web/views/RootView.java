package net.contextfw.demo.web.views;

import net.contextfw.demo.web.components.Updater;
import net.contextfw.web.application.component.Component;
import net.contextfw.web.application.component.Element;
import net.contextfw.web.application.lifecycle.PageScoped;
import net.contextfw.web.application.lifecycle.View;
import net.contextfw.web.application.lifecycle.ViewComponent;
import net.contextfw.web.application.lifecycle.ViewContext;

import com.google.inject.Inject;

@View
@PageScoped
public class RootView extends Component implements ViewComponent {

    @Element
    private Component child;
    
    @SuppressWarnings("unused")
    @Element
    @Inject
    private Updater updater;
    
    @Override
    public void initialize(ViewContext context) {
        if (context.getChildClass() != null) {
            child = context.initChild();
            registerChild(child);
        }
    }
}