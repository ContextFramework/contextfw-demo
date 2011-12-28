package net.contextfw.demo.web.proxy;

import net.contextfw.demo.web.model.ProxyHandler;

import org.junit.Test;

public class ProxyTest {

    private static class Listener implements I1, I2 {

        private final String name;
        
        public Listener(String name) {
            this.name = name;
        }
        
        @Override
        public void a2(String val) {
            System.out.println(name + ":" + val);
        }

        @Override
        public void a1() {
            System.out.println(name + ": called");
        }

        @Override
        public void shared(boolean boo) {
            System.out.println(name + ":" + boo);
        }
        
    }
    
    private static class L1 implements I1 {

        private final String name;
        
        public L1(String name) {
            this.name = name;
        }
        
        @Override
        public void a1() {
            System.out.println(name + ": called");
        }

        @Override
        public void shared(boolean boo) {
            System.out.println(name + ":" + boo);
        }
        
    }
    
    private static class L2 implements I2 {

        private final String name;
        
        public L2(String name) {
            this.name = name;
        }
        
        @Override
        public void a2(String val) {
            System.out.println(name + ":" + val);
        }
        
        @Override
        public void shared(boolean boo) {
            System.out.println(name + ":" + boo);
        }
        
    }
    
    @Test
    public void test1() {
        
        ProxyHandler handler = new ProxyHandler(I2.class, I1.class);
        handler.addListener(new Listener("A1"));
        handler.addListener(new Listener("A2"));
        handler.addListener(new L1("I1"));
        handler.addListener(new L2("I2"));
        I1 i1 = handler.getProxy(I1.class);
        I2 i2 = handler.getProxy(I2.class);
        i1.a1();
        i2.a2("Foo");
        i1.shared(false);
        i2.shared(true);
    }
    
}
