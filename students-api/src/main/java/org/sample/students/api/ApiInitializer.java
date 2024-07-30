package org.sample.students.api;

import jakarta.ws.rs.ext.Provider;
import org.sample.students.appcontext.ApplicationContext;
import org.glassfish.jersey.server.monitoring.ApplicationEvent;
import org.glassfish.jersey.server.monitoring.ApplicationEventListener;
import org.glassfish.jersey.server.monitoring.RequestEvent;
import org.glassfish.jersey.server.monitoring.RequestEventListener;

@Provider
public class ApiInitializer implements ApplicationEventListener {

    @Override
    public void onEvent(ApplicationEvent applicationEvent) {
        if (applicationEvent.getType().name().equals("INITIALIZATION_START")) {
            ApplicationContext.buildApplicationContext();
        }
    }

    @Override
    public RequestEventListener onRequest(RequestEvent requestEvent) {
        return null;
    }
}
