package net.contextfw.demo;

import java.lang.reflect.Method;
import java.util.Locale;

import org.apache.commons.lang.StringUtils;

import net.contextfw.web.application.PageContext;
import net.contextfw.web.application.component.Component;
import net.contextfw.web.commons.cloud.session.CloudSession;
import net.contextfw.web.commons.cloud.session.CloudSessionLifecycleListener;
import net.contextfw.web.commons.cloud.session.NoSessionException;
import net.contextfw.web.commons.cloud.session.OpenMode;
import net.contextfw.web.commons.i18n.LocaleService;

import com.google.inject.Inject;
import com.google.inject.Provider;

public class DemoLifecycleListener extends CloudSessionLifecycleListener {
    
    public static final Locale EN = new Locale("en");
    public static final Locale FI = new Locale("fi");

    private final LocaleService localeService;
    private final CloudSession session;
    
    private final Provider<PageContext> httpContext;
    
    @Override
    public void beforeInitialize() {
        super.beforeInitialize();
        assignLocale();
        localeService.setCurrentLocale(httpContext.get().getLocale());
    }
    
    private void assignLocale() {
        String localeStr = StringUtils.trimToNull(httpContext.get()
                .getRequest().getParameter("lang"));
        
        if ("fi".equals(localeStr)) {
            httpContext.get().setLocale(FI);
        } else {
            httpContext.get().setLocale(EN);
        }
    }

    @Override
    public boolean beforeUpdate(Component component, Method method, Object[] args) {
        localeService.setCurrentLocale(httpContext.get().getLocale());
        try {
            return super.beforeUpdate(component, method, args);
        } catch (NoSessionException nse) {
            session.openSession(OpenMode.LAZY);
            return true;
        }
    }

    @Inject
    public DemoLifecycleListener(CloudSession session, 
                                 LocaleService localeService,
                                 Provider<PageContext> httpContext) {
        super(session);
        this.localeService = localeService;
        this.httpContext = httpContext;
        this.session = session;
    }
}
