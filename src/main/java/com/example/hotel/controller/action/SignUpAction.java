package com.example.hotel.controller.action;

import com.example.hotel.controller.commons.Constants.RequestAttributes;
import com.example.hotel.controller.commons.Constants.SessionAttributes;
import com.example.hotel.controller.commons.Path;
import com.example.hotel.controller.dto.UserDTO;
import com.example.hotel.controller.exception.InvalidDataException;
import com.example.hotel.controller.factory.mapper.MapperFactory;
import com.example.hotel.controller.factory.validator.ValidatorFactory;
import com.example.hotel.controller.mapper.Mapper;
import com.example.hotel.controller.validator.Validator;
import com.example.hotel.model.service.UserService;
import com.example.hotel.model.service.exception.LoginIsNotUniqueException;
import com.example.hotel.model.service.exception.ServiceException;
import com.example.hotel.model.service.factory.ServiceFactory;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;

import java.io.IOException;

public class SignUpAction implements Action {
  public static final Logger log = Logger.getLogger(SignUpAction.class);
  public static final String ERROR_ATTRIBUTE = RequestAttributes.ERROR_PREFIX + Path.Get.User.SIGNUP_PAGE;
  public static final String USER_EXISTS_MESSAGE_PROPERTY = "user_with_such_login_exists";
  public static final String GENERAL_ERROR_MESSAGE_PROPERTY = "data_could_not_be_saved";
  private final UserService userService;
  private final Mapper<UserDTO> userDtoMapper = MapperFactory.getInstance().createUserMapper();
  private final Validator userValidator = ValidatorFactory.getInstance().createUserValidator();

  public SignUpAction() {
    userService = ServiceFactory.getInstance().createUserService();
  }

  public SignUpAction(final ServiceFactory serviceFactory) {
    userService = serviceFactory.createUserService();
  }


  @Override
  public void execute(final HttpServletRequest request, final HttpServletResponse response) throws ServletException, IOException {
    try {
      userValidator.throwIfNotValid(request);
      final var userDto = userDtoMapper.map(request);
      log.info("User validation passed successfully");

      userService.signUp(userDto);
      log.info("User has been signed up");

      response.sendRedirect(
              request.getContextPath() + Path.Get.User.MAIN);

    } catch (final InvalidDataException e) {
      log.error(e.getMessage());
      setValidationErrorMessage(request, e);
      response.sendRedirect(request.getContextPath() + Path.Get.User.SIGNUP_PAGE);
    } catch (final LoginIsNotUniqueException e) {
      log.error(e.getMessage());
      request.getSession().setAttribute(ERROR_ATTRIBUTE, USER_EXISTS_MESSAGE_PROPERTY);
      response.sendRedirect(request.getContextPath() + Path.Get.User.SIGNUP_PAGE);
    } catch (final ServiceException e) {
      log.error(e.getMessage(), e);
      request.getSession().setAttribute(ERROR_ATTRIBUTE, GENERAL_ERROR_MESSAGE_PROPERTY);
      response.sendRedirect(request.getContextPath() + Path.Get.User.SIGNUP_PAGE);
    } catch (final Exception e) {
      log.error(e.getMessage(), e);
      response.sendRedirect(request.getContextPath() + Path.Get.Error.ERROR_503);
    }
  }

  private static void setValidationErrorMessage(final HttpServletRequest request,
                                                final InvalidDataException e) {
    final var errorMessage = e.getInvalidField() + SessionAttributes.INVALID_SUFFIX;
    request
            .getSession()
            .setAttribute(ERROR_ATTRIBUTE, errorMessage);
  }
}
