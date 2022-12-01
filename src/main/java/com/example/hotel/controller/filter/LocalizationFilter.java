//package com.example.hotel.controller.filters;
//
//
//import org.apache.log4j.Logger;
//
//import javax.servlet.*;
//import javax.servlet.http.HttpServletRequest;
//import java.io.IOException;
//
//
///**
// * Set locale from request. If it's not presented, set locale from session
// */
//public class LocalizationFilter implements Filter {
//
//    public final static Logger log = Logger.getLogger(LocalizationFilter.class);
//
//
//    @Override
//    public void init(FilterConfig filterConfig) {
//        log.debug("Filter initialized");
//    }
//
//    @Override
//    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
//
//        HttpServletRequest request = (HttpServletRequest) servletRequest;
//        String localeS = (String) request.getSession().getAttribute("locale");
//
//        log.trace("get locale string from session: " + localeS);
//
//        String localeR = request.getParameter("locale");
//
//        log.trace("get locale string from request: " + localeR);
//        if (localeR != null) {
//            request.getSession().setAttribute("locale", localeR);
//            log.trace("locale from request not null, set request locale to session: " + localeR);
//        } else {
//            if (localeS == null) {
//                request.getSession().setAttribute("locale", "en");
//                log.trace("locale from request null, set default locale(en) to session: ");
//            }
//        }
//
//        filterChain.doFilter(servletRequest, servletResponse);
//    }
//
//    @Override
//    public void destroy() {
//        log.debug("Filter destroyed");
//
//    }
//}
