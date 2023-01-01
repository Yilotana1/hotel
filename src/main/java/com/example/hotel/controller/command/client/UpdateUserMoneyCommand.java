package com.example.hotel.controller.command.client;

import com.example.hotel.commons.Constants.RequestParameters;
import com.example.hotel.commons.Path;
import com.example.hotel.controller.command.Command;
import com.example.hotel.controller.exception.MoneyValueIsNotValidException;
import com.example.hotel.model.service.UserService;
import com.example.hotel.model.service.factory.ServiceFactory;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.math.BigDecimal;

import static com.example.hotel.commons.Tools.getLoginFromSession;

public class UpdateUserMoneyCommand implements Command {
    public final static Logger log = Logger.getLogger(UpdateUserMoneyCommand.class);
    private UserService userService = ServiceFactory.getInstance().createUserService();

    public UpdateUserMoneyCommand() {
    }

    public UpdateUserMoneyCommand(final ServiceFactory serviceFactory) {
        userService = serviceFactory.createUserService();
    }

    @Override
    public void execute(final HttpServletRequest request, final HttpServletResponse response) throws ServletException, IOException {
        try {
            final var login = getLoginFromSession(request.getSession());
            final var moneyInput = request.getParameter(RequestParameters.CLIENT_MONEY);
            if (moneyInputIsIncorrect(moneyInput)) {
                throw new MoneyValueIsNotValidException();
            }
            final var moneyToAdd = new BigDecimal(moneyInput);
            userService.updateMoneyAccount(login, moneyToAdd);
            response.sendRedirect(request.getContextPath() + Path.Get.User.PROFILE);
        } catch (final MoneyValueIsNotValidException e) {
            log.error("User specified wrong money value");
            response.sendRedirect(request.getContextPath() + Path.Get.Client.MONEY_VALUE_IS_INCORRECT);
        } catch (final Exception e) {
            log.error(e.getMessage(), e);
            response.sendRedirect(request.getContextPath() + Path.Get.Error.ERROR_503);
        }
    }

    private boolean moneyInputIsIncorrect(final String moneyInput) {
        try {
            final var value = Double.parseDouble(moneyInput);
            if (value < 0) {
                return true;
            }
        } catch (final NumberFormatException e) {
            return true;
        }
        return false;
    }
}
