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

import static com.example.hotel.controller.Path.Get.Client.APPLICATION_INVOICE;
import static com.example.hotel.controller.Path.Get.User.ERROR_503_PAGE;
import static com.example.hotel.controller.Path.Get.User.PROFILE;

public class ConfirmPaymentCommand implements Command {
    public final static Logger log = Logger.getLogger(ConfirmPaymentCommand.class);
    private static final String DATE_PATTERN = "yyyy-MM-dd";
    private ApplicationService applicationService = ServiceFactory.getInstance().createApplicationService();
    public static final String ERROR_ATTRIBUTE = "error" + APPLICATION_INVOICE;
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
        } catch (final NotEnoughMoneyToConfirmException e) {
            log.error(e.getMessage(), e);
            request.getSession().setAttribute(ERROR_ATTRIBUTE, "client_has_not_enough_to_confirm");
            response.sendRedirect(request.getContextPath() + APPLICATION_INVOICE);
        } catch (final Exception e) {
            log.error(e.getMessage(), e);
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
