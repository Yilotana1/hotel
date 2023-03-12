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

import static com.example.hotel.controller.commons.Tools.addLoginToCache;

public class EditProfileAction implements Action {

    public final static Logger log = Logger.getLogger(EditProfileAction.class);
    private UserService userService = ServiceFactory.getInstance().createUserService();
    public final String ERROR_ATTRIBUTE = RequestAttributes.ERROR_PREFIX + Path.Get.User.PROFILE;

    private final Mapper<UserDTO> userMapper = MapperFactory.getInstance().createUserMapper();
    private final Validator userValidator = ValidatorFactory.getInstance().createUserValidator();

    public EditProfileAction() {
    }

    public EditProfileAction(final ServiceFactory serviceFactory) {
        userService = serviceFactory.createUserService();
    }

    @Override
    public void execute(final HttpServletRequest request, final HttpServletResponse response) throws ServletException, IOException {
        try {
            userValidator.throwIfNotValid(request);
            final var userDto = userMapper.map(request);
            userService.update(userDto);
            addLoginToCache(userDto.getLogin(), request);
            request.getSession().setAttribute(SessionAttributes.LOGIN, userDto.getLogin());

        } catch (final InvalidDataException e) {
            log.error(e.getMessage(), e);
            setInvalidationErrorMessage(request, e);
        } catch (final LoginIsNotUniqueException e) {
            log.error(e.getMessage(), e);
            request.getSession().setAttribute(ERROR_ATTRIBUTE, "user_with_such_login_exists");
        } catch (final ServiceException e) {
            log.error(e.getMessage(), e);
            request.getSession().setAttribute(ERROR_ATTRIBUTE, "data_could_not_be_saved");
        } catch (final Exception e) {
            log.error(e.getMessage(), e);
            response.sendRedirect(request.getContextPath() + Path.Get.Error.ERROR_503);
        } finally {
            response.sendRedirect(request.getContextPath() + Path.Get.User.PROFILE);
        }
    }

    private void setInvalidationErrorMessage(final HttpServletRequest request, final InvalidDataException e) {
        final var message = e.getInvalidField() + SessionAttributes.INVALID_SUFFIX;
        request
                .getSession()
                .setAttribute(ERROR_ATTRIBUTE, message);
    }
}
