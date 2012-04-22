package net.contextfw.demo;

import java.lang.reflect.Method;
import java.util.Locale;

import net.contextfw.web.application.PageContext;
import net.contextfw.web.application.component.Component;
import net.contextfw.web.application.util.Tracker;
import net.contextfw.web.commons.async.AsyncService;
import net.contextfw.web.commons.cloud.session.CloudSession;
import net.contextfw.web.commons.cloud.session.CloudSessionLifecycleListener;
import net.contextfw.web.commons.cloud.session.NoSessionException;
import net.contextfw.web.commons.cloud.session.OpenMode;
import net.contextfw.web.commons.i18n.LocaleService;

import org.apache.commons.lang.StringUtils;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;

@Singleton
public class DemoLifecycleListener extends CloudSessionLifecycleListener {
    
    @Override
    public void afterPageScopeActivation() {
        super.afterPageScopeActivation();
    }

    @Override
    public void beforePageScopeDeactivation() {
        super.beforePageScopeDeactivation();
    }

    public static final Locale EN = new Locale("en");
    public static final Locale FI = new Locale("fi");

    private final LocaleService localeService;
    private final CloudSession session;
    
    private final Provider<PageContext> pageContext;
    
    private AsyncService asyncService;
    
    @Override
    public void beforeInitialize() {
        session.openSession(OpenMode.EAGER);
        Locale locale = getLocale();
        pageContext.get().setLocale(locale);
        localeService.setCurrentLocale(locale);
    }
    
    private Locale getLocale() {
        String localeStr = StringUtils.trimToNull(pageContext.get()
                .getRequest().getParameter("lang"));
        
        if ("fi".equals(localeStr)) {
            return FI;
        } else {
            return EN;
        }
    }

    @Override
    public boolean beforeUpdate(Component component, Method method, Object[] args) {
        // No need to get new locale here, it is stored in pageContext
        localeService.setCurrentLocale(pageContext.get().getLocale());
        try {
            asyncService.update();
            return super.beforeUpdate(component, method, args);
        } catch (NoSessionException nse) {
            session.openSession(pageContext.get().getRequest() == null ?
                    OpenMode.EXISTING : OpenMode.LAZY);
            return true;
        }
    }

    @Inject
    public DemoLifecycleListener(CloudSession session, 
                                 LocaleService localeService,
                                 Provider<PageContext> httpContext) {
        super(session);
        Tracker.initialized(this);
        this.localeService = localeService;
        this.pageContext = httpContext;
        this.session = session;
    }

    @Inject
    public void setAsyncService(AsyncService asyncService) {
        System.out.println("Setting asyncService");
        this.asyncService = asyncService;
    }
}
