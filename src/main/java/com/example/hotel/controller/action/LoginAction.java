package com.example.hotel.controller.action;

import com.example.hotel.controller.commons.Constants.RequestAttributes;
import com.example.hotel.controller.commons.Constants.RequestParameters;
import com.example.hotel.controller.commons.Path;
import com.example.hotel.model.entity.User;
import com.example.hotel.model.service.UserService;
import com.example.hotel.model.service.factory.ServiceFactory;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;

import java.io.IOException;

import static com.example.hotel.controller.commons.Tools.addLoginToCache;
import static com.example.hotel.controller.commons.Tools.addLoginToSession;
import static com.example.hotel.controller.commons.Tools.addRolesToSession;
import static com.example.hotel.controller.commons.Tools.addUserStatusToSession;
import static com.example.hotel.controller.commons.Tools.userIsLogged;

public class LoginAction implements Action {
  public final static Logger log = Logger.getLogger(LoginAction.class);
  public static final String EMPTY_CREDENTIALS_MESSAGE_PROPERTY = "credentials_not_specified";
  public static final String ERROR_ATTRIBUTE = RequestAttributes.ERROR_PREFIX + Path.Get.User.LOGIN_PAGE;
  public static final String USER_IN_SYSTEM_MESSAGE_PROPERTY = "user_in_system";
  public static final String USER_NOT_FOUND_MESSAGE_PROPERTY = "user_not_found";
  private final UserService userService;


  public LoginAction() {
    userService = ServiceFactory.getInstance().createUserService();
  }

  public LoginAction(final ServiceFactory serviceFactory) {
    userService = serviceFactory.createUserService();
  }

  @Override
  public void execute(final HttpServletRequest request, final HttpServletResponse response) throws IOException {
    try {
      final var login = request.getParameter(RequestParameters.LOGIN);
      final var password = request.getParameter(RequestParameters.PASSWORD);
      if (credentialsNotSpecified(login, password)) {
        processLoginError(request, response, EMPTY_CREDENTIALS_MESSAGE_PROPERTY);
        return;
      }
      if (userIsLogged(login, request)) {
        processLoginError(request, response, USER_IN_SYSTEM_MESSAGE_PROPERTY);
        return;
      }
      final var user = userService.signIn(login, password);
      if (user.isEmpty()) {
        processLoginError(request, response, USER_NOT_FOUND_MESSAGE_PROPERTY);
        return;
      }
      addUsersDataIntoContext(request, login, user.get());
      response.sendRedirect(request.getContextPath() + Path.Get.User.PROFILE);
    } catch (final Exception e) {
      log.error(e.getMessage(), e);
      response.sendRedirect(request.getContextPath() + Path.Get.Error.ERROR_503);
    }
  }

  private boolean credentialsNotSpecified(final String login, final String password) {
    if (login == null || password == null || login.isEmpty() || password.isEmpty()) {
      log.error("Login or password are not specified");
      return true;
    }
    return false;
  }

  private void processLoginError(final HttpServletRequest request,
                                 final HttpServletResponse response,
                                 final String errorMessageProperty) throws IOException {
    log.error("User login-process failed");
    request.getSession().setAttribute(ERROR_ATTRIBUTE, errorMessageProperty);
    response.sendRedirect(request.getContextPath() + Path.Get.User.LOGIN_PAGE);
  }

  private void addUsersDataIntoContext(final HttpServletRequest request,
                                       final String login,
                                       final User user) {
    addLoginToCache(login, request);
    addRolesToSession(user.getRoles(), request);
    addLoginToSession(login, request);
    addUserStatusToSession(user.getStatus(), request);
    log.trace(login + " is logged");
  }
}
