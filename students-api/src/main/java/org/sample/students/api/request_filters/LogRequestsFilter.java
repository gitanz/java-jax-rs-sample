package org.sample.students.api.request_filters;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.UriInfo;
import jakarta.ws.rs.ext.Provider;
import org.sample.students.api.model.log.ApiRequestLogModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

@Provider
public class LogRequestsFilter implements ContainerRequestFilter {
    private static final Logger LOGGER = LoggerFactory.getLogger("api-logger");
    @Context
    private UriInfo uriInfo;
    @Context
    HttpServletRequest request;

    @Override
    public void filter(ContainerRequestContext containerRequestContext) throws IOException {
        ApiRequestLogModel apiRequestLogModel = new ApiRequestLogModel(
            request.getRequestedSessionId(),
            request.getRequestURI()
        );
        LOGGER.info(String.valueOf(apiRequestLogModel));
    }
}
