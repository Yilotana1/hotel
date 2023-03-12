package com.example.hotel.controller.filter;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;

import java.io.IOException;

/**
 * Reject page caching.
 * Uses for not allowing users to get on page after logout
 */
public class NoCacheFilter implements Filter {
    public static final Logger log = Logger.getLogger(NoCacheFilter.class);


    @Override
    public void doFilter(final ServletRequest servletRequest,
                         final ServletResponse servletResponse,
                         final FilterChain filterChain) throws IOException, ServletException {
        log.debug("doFilter started; request URI = " + ((HttpServletRequest)servletRequest).getRequestURI());
        final var res = (HttpServletResponse) servletResponse;
        res.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
        log.trace("set header: cache-control");
        filterChain.doFilter(servletRequest, servletResponse);
    }

    @Override
    public void init(FilterConfig filterConfig) {
        log.debug("Filter initialized");
    }

    @Override
    public void destroy() {
        log.debug("Filter destroyed");
    }
}
