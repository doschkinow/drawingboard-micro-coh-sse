package com.mycompany.drawingboard.light;

import com.mycompany.drawingboard.light.coherence.CacheService;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;

import java.io.IOException;
import java.net.URI;
import java.util.Optional;
import org.glassfish.grizzly.http.server.CLStaticHttpHandler;
import org.glassfish.jersey.media.sse.SseFeature;
import org.glassfish.jersey.moxy.json.MoxyJsonFeature;

/**
 * Main class.
 *
 */
public class Main {

    public static final Optional<String> host = Optional.ofNullable(System.getenv("HOSTNAME"));
    public static final Optional<String> port = Optional.ofNullable(System.getenv("PORT"));
    // Base URI the Grizzly HTTP server will listen on
    public static final String BASE_URI = "http://" + host.orElse("localhost")
            + ":" + port.orElse("8080") + "/api/";

    /**
     * Creates and configures a Grizzly HTTP server as a Jersey container,
     * exposing JAX-RS resources defined in this application.
     *
     * @return Grizzly HTTP server.
     */
    public static HttpServer getJerseyContainer() {
        // create a resource config that scans for JAX-RS resources and providers
        // in com.mycompany.drawingboard.light package
        final ResourceConfig rc = new ResourceConfig()
                .register(DrawingsResource.class)
                .register(SseFeature.class)
                .register(MoxyJsonFeature.class)
                .register(CORSFilter.class);

        // create and start a new instance of grizzly http server
        // exposing the Jersey application at BASE_URI
        HttpServer server = GrizzlyHttpServerFactory.createHttpServer(URI.create(BASE_URI), rc, false);
        server.getServerConfiguration().addHttpHandler(new CLStaticHttpHandler(Main.class.getClassLoader()), "/");
        //com.sun.net.httpserver.HttpServer server = JdkHttpServerFactory.createHttpServer(URI.create(BASE_URI), rc, false);

        return server;
    }

    /**
     * Main method.
     *
     * @param args
     * @throws IOException
     */
    public static void main(String[] args) throws Exception {

        CacheService.initializeAll();

        final HttpServer jcontainer = getJerseyContainer();
        jcontainer.start();
        System.out.println(String.format("drawingboard-micro-sse started at %s"  + 
             "\nHit enter to stop it...",  BASE_URI.substring(0, BASE_URI.indexOf("api"))));
        //uncomment the 2 rows below to start this as java app
        //System.in.read(); 
        //jcontainer.shutdownNow();
        
        // for deployment with docker container use an indefinite loop
        while (true) {
            Thread.sleep(1000);
        } 
        

    }
}
