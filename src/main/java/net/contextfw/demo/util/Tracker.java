package net.contextfw.demo.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class Tracker {

    private Tracker() {
    }
    
    private static final Logger LOG = LoggerFactory.getLogger("Tracker");
    
    public static void debug(String msg) {
        if (LOG.isInfoEnabled()) {
            LOG.info(msg + "\n" + getCommon().toString());
        }
    }
    
    public static void debug() {
        if (LOG.isInfoEnabled()) {
            LOG.info(getCommon().toString());
        }
    }
    
    private static StringBuilder getCommon() {
        StringBuilder sb = new StringBuilder();
        StackTraceElement[] trace = Thread.currentThread().getStackTrace();
        for (int i = 3; i < 6; i++) {
            StackTraceElement element = trace[i];
            sb.append("\n        at ");
            sb.append(element.getClassName())
              .append(".")
              .append(element.getMethodName())
              .append("(")
              .append(element.getFileName())
              .append(":")
              .append(element.getLineNumber())
              .append(")");
        }
        return sb;
    }
    
}


