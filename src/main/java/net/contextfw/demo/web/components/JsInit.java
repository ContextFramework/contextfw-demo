package net.contextfw.demo.web.components;

import net.contextfw.web.application.component.Component;
import net.contextfw.web.application.component.FunctionCall;
import net.contextfw.web.application.component.ScriptContext;

public class JsInit extends FunctionCall {

    private final String id;
    private final Class<? extends Component> componentClass;
    
    public JsInit(Component component, Object... args) {
        super(null, toFinalArgs(component.getId(), args));
        this.id = component.getId();
        this.componentClass = component.getClass();
    }
    
    private static Object[] toFinalArgs(String id, Object[] args) {
        Object[] finalArgs = new Object[args == null ? 1 : args.length + 1];
        finalArgs[0] = id;
        if (args != null) {
            for (int i = 0; i < args.length; i++) {
                finalArgs[i+1] = args[i];
            }
        }
        return finalArgs;
    }
    
    @Override
    protected String getFunctionName(ScriptContext scriptContext) {
        return scriptContext.getBuildName(componentClass);
    }

    @Override
    public String getScript(ScriptContext scriptContext) {
        String script = super.getScript(scriptContext, false);
        script = script.substring(0, script.length());
        return "c.set(\""+ id +"\", new " + script + ");\n";
    }
}