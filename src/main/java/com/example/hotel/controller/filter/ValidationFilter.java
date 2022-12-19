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

public class ValidationFilter implements Filter {
    public static final Logger log = Logger.getLogger(ValidationFilter.class);
    public static final String ERROR_ATTRIBUTE_BASE = "error";

    @Override
    public void init(final FilterConfig filterConfig) throws ServletException {
        Filter.super.init(filterConfig);
    }

    @Override
    public void doFilter(final ServletRequest servletRequest,
                         final ServletResponse servletResponse,
                         final FilterChain filterChain) throws IOException, ServletException {

        final var request = (HttpServletRequest) servletRequest;
        final var session = request.getSession();

        final String requestUrl = getRequestUrl(request);
        final var attributesIterator = session.getAttributeNames().asIterator();
        while (attributesIterator.hasNext()) {
            final var attributeName = attributesIterator.next();
            if (attributeName.contains(ERROR_ATTRIBUTE_BASE)) {
                final var errorPageUrl = attributeName.substring(ERROR_ATTRIBUTE_BASE.length());
                if (!errorPageUrl.equals(requestUrl)) {
                    session.removeAttribute(attributeName);
                    log.trace("Validation error attribute (" + attributeName + ") removed");
                }
            }
        }
        filterChain.doFilter(servletRequest, servletResponse);
    }

    private static String getRequestUrl(final HttpServletRequest request) {
        final var baseUrl = request.getContextPath();
        final var fullRequestUrl = request.getRequestURL().toString();
        return fullRequestUrl
                .substring(fullRequestUrl.indexOf(baseUrl) + baseUrl.length());
    }

    @Override
    public void destroy() {
        Filter.super.destroy();
    }
}
