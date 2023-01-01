package com.example.hotel.controller.command.client;

import com.example.hotel.commons.Constants.RequestAttributes;
import com.example.hotel.commons.Constants.RequestParameters;
import com.example.hotel.commons.Path;
import com.example.hotel.controller.command.Command;
import com.example.hotel.model.service.ApplicationService;
import com.example.hotel.model.service.exception.NotEnoughMoneyToConfirmException;
import com.example.hotel.model.service.factory.ServiceFactory;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

public class ConfirmPaymentCommand implements Command {
    public final static Logger log = Logger.getLogger(ConfirmPaymentCommand.class);
    private static final String DATE_PATTERN = "yyyy-MM-dd";
    private static final String ERROR_MESSAGE = "client_has_not_enough_to_confirm";
    private ApplicationService applicationService = ServiceFactory.getInstance().createApplicationService();
    public final String ERROR_ATTRIBUTE = RequestAttributes.ERROR_PREFIX + Path.Get.Client.APPLICATION_INVOICE;

    public ConfirmPaymentCommand() {
    }

    public ConfirmPaymentCommand(final ServiceFactory serviceFactory) {
        applicationService = serviceFactory.createApplicationService();
    }

    @Override
    public void execute(final HttpServletRequest request, final HttpServletResponse response) throws ServletException, IOException {
        try {
            final var applicationId = Long.parseLong(request.getParameter(RequestParameters.ID));
            applicationService.confirmPayment(
                    applicationId,
                    getLocalDateFromString(request.getParameter(RequestParameters.START_DATE)),
                    getLocalDateFromString(request.getParameter(RequestParameters.END_DATE)));
            log.trace("Payment is confirmed");
            response.sendRedirect(request.getContextPath() + Path.Get.User.PROFILE);
        } catch (final NotEnoughMoneyToConfirmException e) {
            log.error(e.getMessage(), e);
            request.getSession().setAttribute(ERROR_ATTRIBUTE, ERROR_MESSAGE);
            response.sendRedirect(request.getContextPath() + Path.Get.Client.APPLICATION_INVOICE);
        } catch (final Exception e) {
            log.error(e.getMessage(), e);
            response.sendRedirect(request.getContextPath() + Path.Get.Error.ERROR_503);
        }
    }

    private LocalDate getLocalDateFromString(final String dateStr) throws ParseException {
        final var format = new SimpleDateFormat(DATE_PATTERN);
        return getLocalDate(format.parse(dateStr));
    }

    private static LocalDate getLocalDate(final Date date) {
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }

}
