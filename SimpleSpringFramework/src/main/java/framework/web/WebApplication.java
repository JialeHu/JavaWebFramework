package framework.web;

import framework.boot.BaseApplication;
import framework.util.EnvVarUtil;
import framework.util.ValidationUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.WebResourceRoot;
import org.apache.catalina.Wrapper;
import org.apache.catalina.connector.Connector;
import org.apache.catalina.core.StandardContext;
import org.apache.catalina.startup.Tomcat;
import org.apache.catalina.valves.ErrorReportValve;
import org.apache.catalina.webresources.DirResourceSet;
import org.apache.catalina.webresources.StandardRoot;
import org.apache.jasper.servlet.JspServlet;
import org.apache.log4j.Level;
import org.apache.log4j.LogManager;

import java.io.File;
import java.util.*;

/**
 * Web Application with Embedded Tomcat
 */
@Slf4j
public class WebApplication extends BaseApplication {

    public static final String DEFAULT_WEB_APP_DIR = "src/main/webapp/";
    public static final String DEFAULT_HOST = "localhost";
    public static final String DEFAULT_PORT = "8080";

    public static final String DEFAULT_SERVLET = "default";
    public static final String JSP_SERVLET = "jsp";
    public static final String DISPATCHER_SERVLET = "dispatcherServlet";

    public static String JSP_RESOURCE_PREFIX = "/templates/";
    public static String STATIC_RESOURCE_PREFIX = "/static/";

    public static final List<String> ENV_VARS = Arrays.asList(
            "HOST",
            "PORT",
            "WEB_APP_DIR",
            "STATIC_DIR",
            "JSP_DIR",
            "DEBUG"
    );

    private Tomcat tomcat;

    @Override
    protected void init() throws Exception {
        super.init();

        Map<String, String> envVars = EnvVarUtil.getEnvVars(ENV_VARS);
        log.info("Setting up Tomcat with Env Vars: " + envVars);

        boolean debugMode = ValidationUtil.isEmpty(envVars.get("DEBUG")) ||
                !envVars.get("DEBUG").equalsIgnoreCase("false");

        // Change log level
        if (debugMode) {
            //LogManager.getRootLogger().setLevel(Level.DEBUG);
            LogManager.getLogger(rootPackage).setLevel(Level.DEBUG);
            LogManager.getLogger("framework").setLevel(Level.DEBUG);
            log.debug("Change log level to DEBUG");
        }

        // Setup web server
        tomcat = new Tomcat();

        // Set host and port
        String webPort = envVars.get("PORT");
        if (ValidationUtil.isEmpty(webPort)) {
            webPort = DEFAULT_PORT;
        }
        tomcat.setPort(Integer.parseInt(webPort));

        String webHost = envVars.get("HOST");
        if (ValidationUtil.isEmpty(webHost)) {
            webHost = DEFAULT_HOST;
        }
        tomcat.setHostname(webHost);
        Connector connector = tomcat.getConnector();
        connector.setProperty("address", webHost);

        // Set web app dir
        String webappDirLocation = envVars.get("WEB_APP_DIR");
        if (ValidationUtil.isEmpty(webappDirLocation)) {
            webappDirLocation = DEFAULT_WEB_APP_DIR;
        }
        if (!ValidationUtil.isEmpty(envVars.get("STATIC_DIR"))) {
            STATIC_RESOURCE_PREFIX = envVars.get("STATIC_DIR");
        }
        if (!ValidationUtil.isEmpty(envVars.get("JSP_DIR"))) {
            JSP_RESOURCE_PREFIX = envVars.get("JSP_DIR");
        }

        StandardContext ctx = (StandardContext) tomcat.addWebapp("", new File(webappDirLocation).getAbsolutePath());
        //StandardContext ctx = (StandardContext) tomcat.addContext("", new File(webappDirLocation).getAbsolutePath());
        log.info("Configuring app with basedir: " + new File(webappDirLocation).getAbsolutePath());

        // Declare an alternative location for your "WEB-INF/classes" dir
        // Servlet 3.0 annotation will work
        File additionWebInfClasses = new File("target/classes");
        WebResourceRoot resources = new StandardRoot(ctx);
        resources.addPreResources(new DirResourceSet(resources, "/WEB-INF/classes",
                additionWebInfClasses.getAbsolutePath(), "/"));
        ctx.setResources(resources);

        // Set Valve
        if (!debugMode) {
            // Disable error report and server info
            ErrorReportValve errorReportValve = new ErrorReportValve();
            errorReportValve.setShowReport(false);
            errorReportValve.setShowServerInfo(false);
            ctx.getPipeline().addValve(errorReportValve);
        }
        log.info("Context Valves: " + Arrays.deepToString(ctx.getPipeline().getValves()));

        // Add Servlet
        Wrapper servlet;

        servlet = Tomcat.addServlet(ctx, DISPATCHER_SERVLET, new DispatcherServlet());
        servlet.setLoadOnStartup(2);
        ctx.addServletMapping("/*", DISPATCHER_SERVLET);

        servlet = Tomcat.addServlet(ctx, JSP_SERVLET, new JspServlet());
        servlet.setLoadOnStartup(3);

        // Start server
        tomcat.init();
        tomcat.start();
        log.info("Server listening on: {}:{} ({}) ... \n",
                connector.getProperty("address"),
                connector.getLocalPort(),
                connector.getProtocol()
        );

        // Start await thread (wait until a proper shutdown command is received)
        Thread awaitThread = new Thread("await-thread") {
            @Override
            public void run() {
                WebApplication.this.tomcat.getServer().await();
                log.info("Server Shut Down");
            }
        };
        awaitThread.setContextClassLoader(getClass().getClassLoader());
        awaitThread.setDaemon(false);
        awaitThread.start();

    }

    @Override
    protected int cleanup() throws Exception {
        this.tomcat.stop();
        this.tomcat.destroy();
        return 0;
    }

}
