package launch;

import java.io.File;

import org.apache.catalina.WebResourceRoot;
import org.apache.catalina.core.StandardContext;
import org.apache.catalina.startup.Tomcat;
import org.apache.catalina.webresources.DirResourceSet;
import org.apache.catalina.webresources.StandardRoot;

public class Main {

    public static void main(String[] args) throws Exception {

        String webappDirLocation = "src/main/webapp/";
        if (!new File(webappDirLocation).exists()){
            webappDirLocation = "./webapp/";
            if(!new File(webappDirLocation).exists()){
                webappDirLocation = "../src/main/webapp/";
                if(!new File(webappDirLocation).exists()){
                    System.err.println("Can't find webapp folder");
                    System.exit(-1);
                }
            }
        }
        System.out.println("Web content founded: " + new File("" + webappDirLocation).getAbsolutePath());

        Tomcat tomcat = new Tomcat();

        //The port that we should run on can be set into an environment variable
        //Look for that variable and default to 8080 if it isn't there.
        String webPort = System.getenv("PORT");
        if(webPort == null || webPort.equals("")) {
            webPort = "8086";
        }

        tomcat.setPort(Integer.valueOf(webPort));

        StandardContext ctx = (StandardContext) tomcat.addWebapp("/", new File(webappDirLocation).getAbsolutePath());
        System.out.println("configuring app with basedir: " + new File("" + webappDirLocation).getAbsolutePath());

        // Declare an alternative location for your "WEB-INF/classes" dir
        // Servlet 3.0 annotation will work
        File additionWebInfClasses = new File("./");
        WebResourceRoot resources = new StandardRoot(ctx);
        resources.addPreResources(new DirResourceSet(resources, "/WEB-INF/classes",
                additionWebInfClasses.getAbsolutePath(),
                "/"));

        ctx.setResources(resources);
        tomcat.start();
        tomcat.getServer().await();
    }
}
