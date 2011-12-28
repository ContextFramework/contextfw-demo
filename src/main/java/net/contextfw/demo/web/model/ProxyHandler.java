package net.contextfw.demo.web.model;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class ProxyHandler implements InvocationHandler {

    private final Map<Class<?>, Set<Object>> listeners = new HashMap<Class<?>, Set<Object>>();
    
    private final Object proxy;
    
    public ProxyHandler(Class<?>... interfaces) {
        proxy = Proxy.newProxyInstance(
                ProxyHandler.class.getClassLoader(), 
                interfaces, 
                this);   
    }
    
    public void addListener(Object listener) {
        Class<?> current = listener.getClass();
        while (current != Object.class) {
            for (Class<?> iface : current.getInterfaces()) {
                addListener(iface, listener);
            }
            current = current.getSuperclass();
        }
    }
    
    private void addListener(Class<?> iface, Object object) {
        if (!listeners.containsKey(iface)) {
            listeners.put(iface, new HashSet<Object>());
        }
        listeners.get(iface).add(object);
    }
    
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Class<?> cl = method.getDeclaringClass(); 
        if (listeners.containsKey(cl)) {
            for (Object listener : listeners.get(cl)) {
                method.invoke(listener, args);
            }
        }
        return null;
    }
    
    @SuppressWarnings("unchecked")
    public <T> T getProxy(Class<T> cl) {
        return (T) proxy;
    }

}
