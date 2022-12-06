package com.example.hotel.controller.filter;

import com.example.hotel.model.entity.enums.Role;
import com.example.hotel.model.entity.enums.UserStatus;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static com.example.hotel.controller.Path.ADMIN_MANAGE_USERS;
import static com.example.hotel.controller.Path.EDIT_PROFILE;
import static com.example.hotel.controller.Path.ADMIN_EDIT_USER;
import static com.example.hotel.controller.Path.ERROR_404_PAGE;
import static com.example.hotel.controller.Path.LOGIN;
import static com.example.hotel.controller.Path.LOGIN_PAGE;
import static com.example.hotel.controller.Path.LOGOUT;
import static com.example.hotel.controller.Path.MAIN;
import static com.example.hotel.controller.Path.MAIN_PAGE;
import static com.example.hotel.controller.Path.MANAGER_LIST_USERS;
import static com.example.hotel.controller.Path.PROFILE;
import static com.example.hotel.controller.Path.SIGNUP;
import static com.example.hotel.controller.Path.SIGNUP_PAGE;
import static com.example.hotel.model.dao.Tools.userIsLogged;
import static com.example.hotel.model.entity.enums.UserStatus.BLOCKED;
import static com.example.hotel.model.entity.enums.UserStatus.NON_BLOCKED;


/**
 * Access filter is used for authorization. It retrieves user role from the session and checks if this role has permissions
 * to go on a request page. If it doesn't, makes redirect at error page.
 * Also checks if user is presented in login cache. Otherwise, delete user from session and makes redirect to main page.
 *
 * @author Anatoliy Zhilko
 */
public class AccessFilter implements Filter {

    private final Map<Role, HashSet<String>> accessMap = new HashMap<>();
    private final Set<String> undefinedUserPermissionsSet = new HashSet<>();
    private final Set<String> blockedUsersPermission = new HashSet<>();
    private String contextPath;

    public final static Logger log = Logger.getLogger(AccessFilter.class);

    /**
     * Defines user roles and pages with permission to get into
     *
     * @param filterConfig
     */
    @Override
    public void init(FilterConfig filterConfig) {
        log.debug("Filter started initializing");
        contextPath = filterConfig.getServletContext().getContextPath();

        addPermissionsForBlockedUsers(
                MAIN,
                ERROR_404_PAGE,
                PROFILE,
                EDIT_PROFILE);

        addPermissionsToUndefinedUser(
                LOGIN,
                LOGIN_PAGE,
                MAIN,
                SIGNUP,
                SIGNUP_PAGE,
                ERROR_404_PAGE);

        addPermissionsTo(Role.CLIENT,
                MAIN,
                ERROR_404_PAGE,
                LOGOUT,
                PROFILE,
                EDIT_PROFILE);

        addPermissionsTo(Role.MANAGER,
                MAIN,
                ERROR_404_PAGE,
                LOGOUT,
                PROFILE,
                EDIT_PROFILE,
                MANAGER_LIST_USERS);

        addPermissionsTo(Role.ADMIN,
                MAIN,
                ERROR_404_PAGE,
                LOGOUT,
                PROFILE,
                EDIT_PROFILE,
                ADMIN_MANAGE_USERS,
                ADMIN_EDIT_USER);


        log.debug("Filter initialized");
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        log.debug("doFilter started");
        var httpRequest = (HttpServletRequest) servletRequest;
        var httpResponse = (HttpServletResponse) servletResponse;

        var roles = getRolesFromSession(httpRequest.getSession());
        var login = getLoginFromSession(httpRequest.getSession());
        var userStatus = getUserStatusFromSession(httpRequest.getSession());
        log.trace("login = " + login + ", roles = " + roles + ", userStatus = " + userStatus);
        var URI = httpRequest.getRequestURI();
        if (actionIsForbidden(userStatus, roles, URI)) {
            log.trace("actionIsForbidden URI = " + URI);
            httpResponse.sendRedirect(contextPath + ERROR_404_PAGE);
            return;
        }

        //Check whether user has been removed from LoginCache by manager or admin
        if (loginIsRemovedFromLoginCache(login, httpRequest)) {
            httpRequest.getSession().invalidate();
            httpResponse.sendRedirect(contextPath + MAIN_PAGE);
            return;
        }
        log.debug("doFilter finished");
        filterChain.doFilter(servletRequest, servletResponse);
    }


    @Override
    public void destroy() {
        log.debug("Filter destroyed");

    }

    private boolean actionIsForbidden(UserStatus userStatus, Collection<Role> roles, String URI) {
        if (roles == null && undefinedUserPermissionsSet.contains(URI)) {
            return false;
        }
        if ((userStatus != null && userStatus.equals(BLOCKED) && blockedUsersPermission.contains(URI))) {
            return false;
        }
        if (roles != null && userStatus != null && userStatus.equals(NON_BLOCKED)) {
            for (var role : roles) {
                var permissionsSet = accessMap.get(role);
                if (permissionsSet.contains(URI)) {
                    return false;
                }
            }
        }
        return true;
    }

    private static boolean loginIsRemovedFromLoginCache(String login, HttpServletRequest httpRequest) {
        return login != null && !(userIsLogged(login, httpRequest));
    }

    private UserStatus getUserStatusFromSession(HttpSession session) {
        return (UserStatus) session.getAttribute("status");
    }

    private String getLoginFromSession(HttpSession session) {
        return (String) session.getAttribute("login");
    }

    private Collection<Role> getRolesFromSession(HttpSession session) {
        return (Collection<Role>) session.getAttribute("roles");
    }

    private void addPermissionsToUndefinedUser(String... permissions) {
        for (var permission : permissions) {
            undefinedUserPermissionsSet.add(contextPath + permission);
        }
    }

    private void addPermissionsForBlockedUsers(String... permissions) {
        for (var permission : permissions) {
            blockedUsersPermission.add(contextPath + permission);
        }
    }

    private void addPermissionsTo(Role role, String... permissions) {
        accessMap.putIfAbsent(role, new HashSet<>());
        var permissionSet = accessMap.get(role);
        for (var permission : permissions) {
            permissionSet.add(contextPath + permission);
        }
    }
}
