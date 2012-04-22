package net.contextfw.demo.web.views;

import net.contextfw.demo.DemoApplicationModule;
import net.contextfw.web.application.component.Attribute;
import net.contextfw.web.application.component.Component;
import net.contextfw.web.application.component.Element;
import net.contextfw.web.application.lifecycle.PageScoped;
import net.contextfw.web.application.lifecycle.View;
import net.contextfw.web.application.lifecycle.ViewComponent;
import net.contextfw.web.application.lifecycle.ViewContext;

import com.google.inject.Inject;
import com.google.inject.name.Named;

@View
@PageScoped
public class RootView extends Component implements ViewComponent {

    @Element
    private Component child;
    
    @Attribute
    private final boolean webSocketEnabled;
    
    @Inject
    public RootView(@Named(DemoApplicationModule.WEB_SOCKET_ENABLED)
                    boolean webSocketEnabled) {
        this.webSocketEnabled = webSocketEnabled;
    }
    
    @Override
    public void initialize(ViewContext context) {
        if (context.getChildClass() != null) {
            child = context.initChild();
            registerChild(child);
        }
    }

    public boolean isWebSocketEnabled() {
        return webSocketEnabled;
    }
}