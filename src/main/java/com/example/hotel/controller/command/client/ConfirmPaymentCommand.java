package com.example.hotel.controller.command.client;

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

import static com.example.hotel.controller.Path.APPLICATION_INVOICE;
import static com.example.hotel.controller.Path.ERROR_503_PAGE;
import static com.example.hotel.controller.Path.PROFILE;
import static java.lang.String.format;

public class ConfirmPaymentCommand implements Command {
    public final static Logger log = Logger.getLogger(ConfirmPaymentCommand.class);
    private static final String DATE_PATTERN = "yyyy-MM-dd";
    private static final String CLIENT_HAS_NOT_ENOUGH_TO_CONFIRM = "Client has not enough to confirm application: application price = %f, User money account = %f";
    private ApplicationService applicationService = ServiceFactory.getInstance().createApplicationService();

    public ConfirmPaymentCommand() {
    }

    public ConfirmPaymentCommand(final ServiceFactory serviceFactory) {
        applicationService = serviceFactory.createApplicationService();
    }

    @Override
    public void execute(final HttpServletRequest request, final HttpServletResponse response) throws ServletException, IOException {
        try {
            final var applicationId = Long.parseLong(request.getParameter("application_id"));
            applicationService.confirmPayment(
                    applicationId,
                    getLocalDateFromString(request.getParameter("start_date")),
                    getLocalDateFromString(request.getParameter("end_date")));
            log.trace("Payment is confirmed");
            response.sendRedirect(request.getContextPath() + PROFILE);

        } catch (NotEnoughMoneyToConfirmException e) {
            final var clientsMoney = e.getClient().getMoney();
            final var price = e.getApplication().getPrice();
            log.error(format(CLIENT_HAS_NOT_ENOUGH_TO_CONFIRM, clientsMoney, price));
            request.setAttribute("error", "client_has_not_enough_to_confirm");
            request.getRequestDispatcher(APPLICATION_INVOICE).forward(request, response);
        } catch (Exception e) {
            log.error(e.getMessage());
            response.sendRedirect(request.getContextPath() + ERROR_503_PAGE);
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
