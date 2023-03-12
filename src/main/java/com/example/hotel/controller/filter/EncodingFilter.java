package com.example.hotel.controller.filter;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.log4j.Logger;

import java.io.IOException;

/**
 * Set content type and encoding
 */
public class EncodingFilter implements Filter {
    public final static Logger log = Logger.getLogger(EncodingFilter.class);

    @Override
    public void init(FilterConfig filterConfig) {
        log.debug("Filter initialized");
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        log.debug("doFilter started; request URI = " + ((HttpServletRequest)servletRequest).getRequestURI());
        servletResponse.setContentType("text/html");
        servletResponse.setCharacterEncoding("UTF-8");
        servletRequest.setCharacterEncoding("UTF-8");
        log.trace("doFilter finished");
        filterChain.doFilter(servletRequest, servletResponse);
    }

    @Override
    public void destroy() {
        log.debug("Filter destroyed");
    }
}
