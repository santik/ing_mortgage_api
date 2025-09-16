package com.ing.mortgage.filter;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import org.apache.catalina.connector.RequestFacade;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.UUID;

@WebFilter
@Component
public class TraceIdFilter implements Filter {

    public static final String X_TRACE_ID = "x-trace-id";

    /**
     * Adds or generates a trace ID for each incoming request and stores it in the MDC for logging.
     * Removes the trace ID after the request is processed.
     *
     * @param servletRequest the incoming servlet request
     * @param servletResponse the outgoing servlet response
     * @param filterChain the filter chain to continue processing
     * @throws IOException if an I/O error occurs during filtering
     * @throws ServletException if a servlet error occurs during filtering
     */
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        var traceId = ((RequestFacade) servletRequest).getHeader(X_TRACE_ID);
        MDC.put(X_TRACE_ID, traceId != null ? traceId : UUID.randomUUID().toString());
        filterChain.doFilter(servletRequest, servletResponse);
        MDC.remove(X_TRACE_ID);
    }
}
