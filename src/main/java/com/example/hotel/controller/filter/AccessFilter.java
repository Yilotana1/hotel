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

import static com.example.hotel.controller.Path.ADMIN_EDIT_USER;
import static com.example.hotel.controller.Path.ADMIN_MANAGE_USERS;
import static com.example.hotel.controller.Path.APARTMENT_NOT_AVAILABLE_PAGE;
import static com.example.hotel.controller.Path.APPLICATION_CANCELED;
import static com.example.hotel.controller.Path.APPLICATION_INVOICE;
import static com.example.hotel.controller.Path.APPLY_FOR_CLIENT;
import static com.example.hotel.controller.Path.CANCEL_APPLICATION;
import static com.example.hotel.controller.Path.CLIENT_APPLY;
import static com.example.hotel.controller.Path.CLIENT_HAS_APPLICATION_PAGE;
import static com.example.hotel.controller.Path.CLIENT_HAS_ASSIGNED_APPLICATION_PAGE;
import static com.example.hotel.controller.Path.CONFIRM_PAYMENT;
import static com.example.hotel.controller.Path.EDIT_APARTMENT;
import static com.example.hotel.controller.Path.EDIT_PROFILE;
import static com.example.hotel.controller.Path.ERROR_404_PAGE;
import static com.example.hotel.controller.Path.ERROR_503_PAGE;
import static com.example.hotel.controller.Path.LOGIN;
import static com.example.hotel.controller.Path.LOGIN_PAGE;
import static com.example.hotel.controller.Path.LOGOUT;
import static com.example.hotel.controller.Path.MAIN;
import static com.example.hotel.controller.Path.MAIN_PAGE;
import static com.example.hotel.controller.Path.MAKE_TEMPORARY_APPLICATION;
import static com.example.hotel.controller.Path.MAKE_TEMPORARY_APPLICATION_PAGE;
import static com.example.hotel.controller.Path.MANAGER_LIST_USERS;
import static com.example.hotel.controller.Path.MONEY_VALUE_IS_INCORRECT;
import static com.example.hotel.controller.Path.PREFERRED_APARTMENTS_PAGE;
import static com.example.hotel.controller.Path.PROFILE;
import static com.example.hotel.controller.Path.SHOW_APARTMENTS;
import static com.example.hotel.controller.Path.SHOW_APARTMENTS_MANAGEMENT;
import static com.example.hotel.controller.Path.SHOW_APPLY_PAGE;
import static com.example.hotel.controller.Path.SHOW_PREFERRED_APARTMENTS;
import static com.example.hotel.controller.Path.SHOW_TEMPORARY_APPLICATIONS;
import static com.example.hotel.controller.Path.SIGNUP;
import static com.example.hotel.controller.Path.SIGNUP_PAGE;
import static com.example.hotel.controller.Path.SUCCESSFUL_APPLICATION_ASSIGNMENT_PAGE;
import static com.example.hotel.controller.Path.SUCCESS_APPLY_PAGE;
import static com.example.hotel.controller.Path.UPDATE_MONEY_ACCOUNT;
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

    public static final Logger log = Logger.getLogger(AccessFilter.class);

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
                ERROR_503_PAGE,
                PROFILE,
                EDIT_PROFILE,
                LOGOUT);

        addPermissionsToUndefinedUser(
                LOGIN,
                LOGIN_PAGE,
                MAIN,
                SIGNUP,
                SIGNUP_PAGE,
                ERROR_404_PAGE,
                ERROR_503_PAGE);

        addPermissionsTo(Role.CLIENT,
                MAIN,
                ERROR_404_PAGE,
                ERROR_503_PAGE,
                LOGOUT,
                PROFILE,
                EDIT_PROFILE,
                UPDATE_MONEY_ACCOUNT,
                CLIENT_APPLY,
                SHOW_APPLY_PAGE,
                SUCCESS_APPLY_PAGE,
                APPLICATION_INVOICE,
                APARTMENT_NOT_AVAILABLE_PAGE,
                CLIENT_HAS_APPLICATION_PAGE,
                CONFIRM_PAYMENT,
                MONEY_VALUE_IS_INCORRECT,
                CANCEL_APPLICATION,
                APPLICATION_CANCELED,
                MAKE_TEMPORARY_APPLICATION,
                MAKE_TEMPORARY_APPLICATION_PAGE);

        addPermissionsTo(Role.MANAGER,
                MAIN,
                ERROR_404_PAGE,
                ERROR_503_PAGE,
                LOGOUT,
                PROFILE,
                EDIT_PROFILE,
                MANAGER_LIST_USERS,
                SHOW_PREFERRED_APARTMENTS,
                PREFERRED_APARTMENTS_PAGE,
                APPLY_FOR_CLIENT,
                CLIENT_HAS_ASSIGNED_APPLICATION_PAGE,
                SUCCESSFUL_APPLICATION_ASSIGNMENT_PAGE,
                SHOW_TEMPORARY_APPLICATIONS,
                SHOW_APARTMENTS);

        addPermissionsTo(Role.ADMIN,
                MAIN,
                ERROR_404_PAGE,
                ERROR_503_PAGE,
                LOGOUT,
                PROFILE,
                EDIT_PROFILE,
                ADMIN_MANAGE_USERS,
                ADMIN_EDIT_USER,
                SHOW_APARTMENTS_MANAGEMENT,
                EDIT_APARTMENT);
        log.debug("Filter initialized");
    }

    @Override
    public void doFilter(final ServletRequest servletRequest,
                         final ServletResponse servletResponse,
                         final FilterChain filterChain) throws IOException, ServletException {
        log.debug("doFilter started");
        final var httpRequest = (HttpServletRequest) servletRequest;
        final var httpResponse = (HttpServletResponse) servletResponse;

        final var roles = getRolesFromSession(httpRequest.getSession());
        final var login = getLoginFromSession(httpRequest.getSession());
        final var userStatus = getUserStatusFromSession(httpRequest.getSession());
        log.trace("login = " + login + ", roles = " + roles + ", userStatus = " + userStatus);
        final var URI = httpRequest.getRequestURI();
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

    private boolean actionIsForbidden(final UserStatus userStatus,
                                      final Collection<Role> roles,
                                      final String URI) {
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
