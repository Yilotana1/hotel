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
 * Set locale from request. If it's not presented, set locale from session.
 * If neither request nor session locale is presented set default locale
 * Locale language is presented as {@link  LANG}
 */
public class LocalizationFilter implements Filter {

    public final static Logger log = Logger.getLogger(LocalizationFilter.class);
    public static final String LANGUAGE = "lang";


    @Override
    public void init(FilterConfig filterConfig) {
        log.debug("Filter initialized");
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        final var request = (HttpServletRequest) servletRequest;
        final var langStoredInSession = (LANG) request.getSession().getAttribute(LANGUAGE);

        log.trace("get locale language from session: " + langStoredInSession);

        final var langFromRequest = getLangFromRequest(request);
        log.trace("get locale language from request: " + langFromRequest);

        if (langFromRequest != null) {
            request.getSession().setAttribute(LANGUAGE, langFromRequest);
            log.trace("locale language from request not null, set request locale language to session: " + langFromRequest);
        } else {
            if (langStoredInSession == null) {
                request.getSession().setAttribute(LANGUAGE, LANG.getDefault());
                log.trace("locale language from request null, set default locale(en) to session: ");
            }
        }

        filterChain.doFilter(servletRequest, servletResponse);
    }


    @Override
    public void destroy() {
        log.debug("Filter destroyed");

    }

    public enum LANG {
        EN, UA;
        public static LANG getDefault() {
            return EN;
        }
    }

    private LANG getLangFromRequest(HttpServletRequest request) {
        LANG langFromRequest = null;
        if (request.getParameter(LANGUAGE) != null){
            langFromRequest = LANG.valueOf(request.getParameter(LANGUAGE));
        }
        return langFromRequest;
    }
}
