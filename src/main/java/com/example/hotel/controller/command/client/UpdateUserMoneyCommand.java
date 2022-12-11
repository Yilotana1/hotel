package com.example.hotel.controller.command.client;

import com.example.hotel.controller.command.Command;
import com.example.hotel.controller.exception.MoneyValueIsNotValidException;
import com.example.hotel.model.service.UserService;
import com.example.hotel.model.service.exception.Messages;
import com.example.hotel.model.service.factory.ServiceFactory;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.math.BigDecimal;

import static com.example.hotel.controller.Path.ERROR_503_PAGE;
import static com.example.hotel.controller.Path.MONEY_VALUE_IS_INCORRECT;
import static com.example.hotel.controller.Path.PROFILE;
import static com.example.hotel.model.dao.Tools.getLoginFromSession;

public class UpdateUserMoneyCommand implements Command {
    public final static Logger log = Logger.getLogger(UpdateUserMoneyCommand.class);
    public static final String MONEY = "money";
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
            final var moneyInput = request.getParameter(MONEY);
            if (moneyInputIsIncorrect(moneyInput)) {
                throw new MoneyValueIsNotValidException();
            }
            final var moneyToAdd = new BigDecimal(moneyInput);
            userService.updateMoneyAccount(login, moneyToAdd);
            response.sendRedirect(request.getContextPath() + PROFILE);
        } catch (final MoneyValueIsNotValidException e) {
            log.error("User specified wrong money value");
            response.sendRedirect(request.getContextPath() + MONEY_VALUE_IS_INCORRECT);
        } catch (final Exception e) {
            log.error(Messages.UPDATING_USERS_MONEY_FAILED, e);
            response.sendRedirect(request.getContextPath() + ERROR_503_PAGE);
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
