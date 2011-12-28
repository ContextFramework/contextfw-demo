package net.contextfw.demo;

import static net.contextfw.web.application.configuration.Configuration.CLASS_RELOADING_ENABLED;
import static net.contextfw.web.application.configuration.Configuration.DEVELOPMENT_MODE;
import static net.contextfw.web.application.configuration.Configuration.HOST;
import static net.contextfw.web.application.configuration.Configuration.LIFECYCLE_LISTENER;
import static net.contextfw.web.application.configuration.Configuration.LOG_XML;
import static net.contextfw.web.application.configuration.Configuration.RELOADABLE_CLASSES;
import static net.contextfw.web.application.configuration.Configuration.RESOURCE_PATH;
import static net.contextfw.web.application.configuration.Configuration.VERSION;
import static net.contextfw.web.application.configuration.Configuration.VIEW_COMPONENT_ROOT_PACKAGE;
import static net.contextfw.web.application.configuration.Configuration.XML_PARAM_NAME;
import static net.contextfw.web.application.configuration.Configuration.XML_RESPONSE_LOGGER;
import static net.contextfw.web.application.configuration.Configuration.XSL_POST_PROCESSOR;
import static net.contextfw.web.application.configuration.Configuration.NAMESPACE;

import java.io.IOException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.Locale;
import java.util.Properties;

import net.contextfw.web.application.WebApplicationException;
import net.contextfw.web.application.WebApplicationModule;
import net.contextfw.web.application.configuration.Configuration;
import net.contextfw.web.commons.cloud.binding.CloudDatabase;
import net.contextfw.web.commons.cloud.session.CloudSession;
import net.contextfw.web.commons.cloud.session.MongoCloudSession;
import net.contextfw.web.commons.cloud.storage.MongoWebApplicationStorage;
import net.contextfw.web.commons.i18n.LocaleConf;
import net.contextfw.web.commons.i18n.LocaleModule;
import net.contextfw.web.commons.minifier.MinifierConf;
import net.contextfw.web.commons.minifier.MinifierModule;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.mongodb.DB;
import com.mongodb.Mongo;
import com.mongodb.MongoException;
import com.mongodb.ServerAddress;
import com.mycila.inject.jsr250.Jsr250;

public class DemoApplicationModule extends AbstractModule {

    private static final String PROP_DEVELOPMENT_MODE = "developmentMode";

    private final boolean developmentMode;
    
    private final Properties properties;
    
    public DemoApplicationModule() {
        properties = loadProperties();
        developmentMode = Boolean.parseBoolean(
                properties.getProperty(PROP_DEVELOPMENT_MODE, "false"));
    }
    
    @Override
    protected void configure() {
        
        Configuration conf = Configuration.getDefaults()
          .add(RESOURCE_PATH, "net.contextfw.demo")
          .add(VIEW_COMPONENT_ROOT_PACKAGE, "net.contextfw.demo.web.views")
          .add(RELOADABLE_CLASSES.includedPackage("net.contextfw.demo.web"))
          .set(CLASS_RELOADING_ENABLED, true)
          .set(DEVELOPMENT_MODE, developmentMode)
          .set(XML_PARAM_NAME, "xml")
          .set(LOG_XML, developmentMode)
          .add(NAMESPACE.as("widget", DemoPostProcessor.WIDGET_NS))
          .set(HOST, properties.getProperty("host"))
          .set(VERSION, properties.getProperty("version"))
          .set(LIFECYCLE_LISTENER.as(DemoLifecycleListener.class))
          .set(XSL_POST_PROCESSOR.as(DemoPostProcessor.class))
          .set(XML_RESPONSE_LOGGER.as(ResponseLogger.class));
          
        conf = configureMinifier(conf);
        conf = configureI18n(conf);
        conf = configureCloud(conf);

        install(Jsr250.newJsr250Module());
        install(new MinifierModule(conf));
        install(new LocaleModule(conf));
        install(new WebApplicationModule(conf));
    }
    
    private Configuration configureMinifier(Configuration configuration) {
        String path = "/minified-<version>.";
        return configuration
                .set(MinifierConf.CSS_PATH, path+"css")
                .set(MinifierConf.JS_PATH, path+"js")
                .set(MinifierConf.CSS_FILTER, MinifierConf.NO_JQUERY)
                .set(MinifierConf.JS_FILTER, MinifierConf.NO_JQUERY);
    }
    
    private Configuration configureI18n(Configuration configuration) {
        return LocaleConf.applyConfiguration(configuration)
                .set(LocaleConf.DEFAULT_LOCALE, Locale.ENGLISH)
                .add(LocaleConf.SUPPORTED_LOCALE, Locale.ENGLISH)
                .add(LocaleConf.SUPPORTED_LOCALE, new Locale("FI", "fi"))
                .set(LocaleConf.STRICT_VALIDATION, true)
                .set(LocaleConf.BASE_NAME, "net/contextfw/demo/i18n/messages");
    }
    
    private Configuration configureCloud(Configuration conf) {
        bind(CloudSession.class).to(MongoCloudSession.class);
        return conf.set(Configuration.WEB_APPLICATION_STORAGE
                .as(MongoWebApplicationStorage.class))
                .set(CloudSession.MAX_INACTIVITY.inMinutes(20));
    }
    
    private Properties loadProperties() {
        return loadProperties("config.properties");
    }
    
    private Properties loadProperties(String resource) {
        try {
            Properties properties = new Properties();
            ClassLoader loader = getClass().getClassLoader();
            URL url = loader.getResource(resource);
            properties.load(url.openStream());
            return properties;
        } catch (IOException e) {
            throw new WebApplicationException(e);
        }
    }
    
    @Provides
    @Singleton
    @CloudDatabase
    public DB provideDatabase(Mongo mongo) {
        try {
            DB db = mongo.getDB(properties.getProperty("mongodb.database"));
            if (!developmentMode) {
                db.authenticate(properties.getProperty("mongodb.userName"), 
                                properties.getProperty("mongodb.password").toCharArray());
            }
            return db;
        } catch (MongoException e) {
            throw new WebApplicationException(e);
        }
    }
    
    @Provides
    @Singleton
    public Mongo provideMongo() {
        try {
            ServerAddress address = new ServerAddress(
                    properties.getProperty("mongodb.host"), 
                    Integer.parseInt(
                         properties.getProperty("mongodb.port")));
            
            return new Mongo(address);
            
        } catch (UnknownHostException e) {
            throw new WebApplicationException(e);
        } catch (MongoException e) {
            throw new WebApplicationException(e);
        }
    }
}