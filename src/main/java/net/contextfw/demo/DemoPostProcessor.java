package net.contextfw.demo;

import java.util.Iterator;

import net.contextfw.web.application.DocumentProcessor;
import net.contextfw.web.commons.i18n.LocaleService;
import net.contextfw.web.commons.minifier.MinifierService;

import org.dom4j.Document;
import org.dom4j.Element;

import com.google.inject.Inject;

public class DemoPostProcessor implements DocumentProcessor {

    public static final String WIDGET_NS = "http://www.contextfw.net/widget";
    
    private final LocaleService localeService;
    
    //private final JsTemplateService jsService;

    private final MinifierService minifierService;
    
    @Inject
    public DemoPostProcessor(LocaleService localeService,
                             MinifierService minifierService) {
        
        this.localeService = localeService;
    //    this.jsService = jsService;
        this.minifierService = minifierService;
    }
    
    @Override
    public void process(Document document) {
        removeIncludes(document);
        localeService.process(document);
        //  jsService.process(document);
        minifierService.process(document);
        
    }
    
    /**
     * The xsl:include-elements are for development only.
     * 
     * @param document
     */
    private void removeIncludes(Document document) {
        
        @SuppressWarnings("unchecked")
        Iterator<Element> elements = document.selectNodes("//xsl:include").iterator();
        
        while (elements.hasNext()) {
            elements.next().detach();
        }
    }
}