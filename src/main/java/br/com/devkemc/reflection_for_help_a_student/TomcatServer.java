package br.com.devkemc.reflection_for_help_a_student;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.core.StandardContext;
import org.apache.catalina.startup.Tomcat;
import org.apache.catalina.webresources.DirResourceSet;
import org.apache.catalina.webresources.StandardRoot;

import java.io.File;

public class TomcatServer {
    private static final String WEB_APP_DIR = "src/main/webapp/";
    private static final String CLASSES_DIR = "target/classes";
    private static final String WEB_APP_MOUNT = "/WEB-INF/classes";


    public static void init() {

        var tomcat = new Tomcat();
        String webPort = System.getProperty("PORT");
        if (webPort == null || webPort.isEmpty()) {
            webPort = "8080";
        }
        tomcat.setPort(Integer.parseInt(webPort));

        System.out.println("Configuring app with basedir: " + new File("./" + WEB_APP_DIR).getAbsolutePath() + "...");

        var standardContext = (StandardContext) tomcat.addWebapp("", new File(WEB_APP_DIR).getAbsolutePath());
        var additionWebInfClasses = new File(CLASSES_DIR);
        var resources = new StandardRoot(standardContext);
        resources.addPreResources(new DirResourceSet(resources, WEB_APP_MOUNT, additionWebInfClasses.getAbsolutePath(), "/"));
        standardContext.setResources(resources);
        tomcat.enableNaming();
        tomcat.getConnector();

        try {
            System.out.println("Starting Tomcat... in localhost:" + webPort);
            tomcat.start();
        } catch (LifecycleException e) {
            e.printStackTrace();
        }
        tomcat.getServer().await();
    }
}
