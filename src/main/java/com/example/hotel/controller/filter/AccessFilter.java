package com.example.hotel.controller.filter;

import com.example.hotel.controller.commons.Path;
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

import static com.example.hotel.controller.commons.Tools.userIsLogged;
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
  public void init(final FilterConfig filterConfig) {
    log.debug("Filter started initializing");
    contextPath = filterConfig.getServletContext().getContextPath();
    try {

      addPermissionsToUndefinedUser(
              Path.Post.User.SIGNUP,
              Path.Get.User.LOGIN,
              Path.Get.User.LOGIN_PAGE,
              Path.Get.User.MAIN,
              Path.Get.User.SIGNUP_PAGE,
              Path.Get.Error.ERROR_404,
              Path.Get.Error.ERROR_503);

      addPermissionsTo(Role.CLIENT,
              Path.Post.Client.UPDATE_MONEY_ACCOUNT,
              Path.Post.Client.CLIENT_APPLY,
              Path.Post.Client.CONFIRM_PAYMENT,
              Path.Post.Client.CANCEL_APPLICATION,
              Path.Post.Client.MAKE_TEMPORARY_APPLICATION,
              Path.Get.Client.SHOW_APPLY_PAGE,
              Path.Get.Client.SUCCESS_APPLY_PAGE,
              Path.Get.Client.APPLICATION_INVOICE,
              Path.Get.Client.APARTMENT_NOT_AVAILABLE_PAGE,
              Path.Get.Client.CLIENT_HAS_APPLICATION_PAGE,
              Path.Get.Client.MONEY_VALUE_IS_INCORRECT,
              Path.Get.Client.APPLICATION_CANCELED,
              Path.Get.Client.MAKE_TEMPORARY_APPLICATION_PAGE,
              Path.Get.Client.WRONG_STAY_LENGTH);

      addPermissionsTo(Role.MANAGER,
              Path.Post.Manager.APPLY_FOR_CLIENT,
              Path.Get.Manager.MANAGER_LIST_USERS,
              Path.Get.Manager.SHOW_PREFERRED_APARTMENTS,
              Path.Get.Manager.PREFERRED_APARTMENTS_PAGE,
              Path.Get.Manager.CLIENT_HAS_ASSIGNED_APPLICATION_PAGE,
              Path.Get.Manager.SUCCESSFUL_APPLICATION_ASSIGNMENT_PAGE,
              Path.Get.Manager.SHOW_TEMPORARY_APPLICATIONS,
              Path.Get.Manager.SHOW_APARTMENTS);

      addPermissionsTo(Role.ADMIN,
              Path.Post.Admin.ADMIN_EDIT_USER,
              Path.Post.Admin.EDIT_APARTMENT,
              Path.Get.Admin.ADMIN_MANAGE_USERS,
              Path.Get.Admin.SHOW_APARTMENTS_MANAGEMENT
      );

      addPermissionsToAll(
              Path.Post.User.EDIT_PROFILE,
              Path.Get.User.MAIN,
              Path.Get.Error.ERROR_404,
              Path.Get.Error.ERROR_503,
              Path.Get.User.LOGOUT,
              Path.Get.User.PROFILE
      );
    } catch (final Exception e) {
      log.error(e.getMessage(), e);
    }

    log.debug("Filter initialized");
  }

  @Override
  public void doFilter(final ServletRequest servletRequest,
                       final ServletResponse servletResponse,
                       final FilterChain filterChain) throws IOException, ServletException {
    final var httpRequest = (HttpServletRequest) servletRequest;
    final var httpResponse = (HttpServletResponse) servletResponse;

    log.debug("doFilter started; request URI = " + httpRequest.getRequestURI());

    final var roles = getRolesFromSession(httpRequest.getSession());
    final var login = getLoginFromSession(httpRequest.getSession());
    final var userStatus = getUserStatusFromSession(httpRequest.getSession());
    final var URI = httpRequest.getRequestURI();
    if (actionIsForbidden(userStatus, roles, URI)) {
      log.trace("actionIsForbidden URI = " + URI);
      httpResponse.sendRedirect(contextPath + Path.Get.Error.ERROR_404);
      return;
    }

    //Check whether user has been removed from LoginCache by manager or admin
    if (loginIsRemovedFromLoginCache(login, httpRequest)) {
      httpRequest.getSession().invalidate();
      httpResponse.sendRedirect(contextPath + Path.Get.User.MAIN);
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
      for (final var role : roles) {
        final var permissionsSet = accessMap.get(role);
        if (permissionsSet.contains(URI)) {
          return false;
        }
      }
    }
    return true;
  }

  private boolean loginIsRemovedFromLoginCache(final String login,
                                                      final HttpServletRequest httpRequest) {
    return login != null && !(userIsLogged(login, httpRequest));
  }

  private UserStatus getUserStatusFromSession(final HttpSession session) {
    return (UserStatus) session.getAttribute("status");
  }

  private String getLoginFromSession(final HttpSession session) {
    return (String) session.getAttribute("login");
  }

  private Collection<Role> getRolesFromSession(final HttpSession session) {
    return (Collection<Role>) session.getAttribute("roles");
  }

  private void addPermissionsToUndefinedUser(final String... permissions) {
    for (final var permission : permissions) {
      undefinedUserPermissionsSet.add(contextPath + permission);
    }
  }

  private void addPermissionsToAll(final String... permissions) {
    for (final var entry : accessMap.entrySet()) {
      for (final var permission : permissions) {
        entry.getValue().add(contextPath + permission);
        blockedUsersPermission.add(contextPath + permission);
      }
    }
  }

  private void addPermissionsTo(final Role role, final String... permissions) {
    accessMap.putIfAbsent(role, new HashSet<>());
    final var permissionSet = accessMap.get(role);
    for (final var permission : permissions) {
      permissionSet.add(contextPath + permission);
    }
  }
}
