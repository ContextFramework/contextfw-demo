package net.contextfw.demo.web.components;

import java.util.ArrayList;
import java.util.List;

import net.contextfw.web.application.component.Component;
import net.contextfw.web.application.component.Script;
import net.contextfw.web.application.component.ScriptElement;
import net.contextfw.web.application.lifecycle.AfterBuild;

public abstract class JsComponent extends Component {
    
    @ScriptElement
    private final List<Script> scripts = new ArrayList<Script>();

    protected void jsUpdate(Script update) {
        scripts.add(update);
        partialRefresh("jsUpdate", "scripts");
    }
    
    protected void jsUpdate(String method, Object... args) {
        jsUpdate(new JsUpdate(this, method, args));
    }
    
    protected void addScript(Script script) {
       scripts.add(script);
    }
    
    @AfterBuild
    public void clearJsUpdate() {
        scripts.clear();
    }

    @Override
    public void refresh() {
        super.refresh();
    }

    @ScriptElement(onUpdate=false)
    public Script jsInit() {
        return new JsInit(this);
    }
    
    @ScriptElement(onCreate=false)
    public Script jsBindEvents() {
        return new JsUpdate(this, "bindEvents");
    }
}
