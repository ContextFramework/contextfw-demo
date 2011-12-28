package net.contextfw.demo.web.components;

import net.contextfw.web.application.component.Component;
import net.contextfw.web.application.component.FunctionCall;

public class JsUpdate extends FunctionCall {
    public JsUpdate(Component component, String function, Object... args) {
        super("c." + component.getId() + "." + function, args);
    }
}