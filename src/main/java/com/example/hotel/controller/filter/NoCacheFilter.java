package com.example.hotel.controller.filter;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;

import java.io.IOException;

/**
 * Reject page caching.
 * Uses for not allowing users to get on page after logout
 */
public class NoCacheFilter implements Filter {
    public final static Logger log = Logger.getLogger(NoCacheFilter.class);


    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletResponse res = (HttpServletResponse) servletResponse;
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
