package com.example.hotel.controller.tag.handler;

import com.example.hotel.model.entity.Application;
import com.example.hotel.model.service.ApplicationService;
import com.example.hotel.model.service.factory.ServiceFactory;
import jakarta.servlet.jsp.JspWriter;
import jakarta.servlet.jsp.tagext.TagSupport;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.util.Locale;
import java.util.ResourceBundle;

import static com.example.hotel.controller.filter.LocalizationFilter.LANGUAGE;
import static com.example.hotel.model.dao.Tools.getLoginFromSession;

public class ShowApprovedApplicationHandler extends TagSupport {
    public final static Logger log = Logger.getLogger(ShowApprovedApplicationHandler.class);
    private final ApplicationService applicationService = ServiceFactory.getInstance().createApplicationService();

    @Override
    public int doStartTag() {
        try {
            final var out = pageContext.getOut();
            final var clientLogin = getLoginFromSession(pageContext.getSession());
            applicationService
                    .getApprovedApplicationByLogin(clientLogin)
                    .ifPresentOrElse(
                            application -> writeApplication(application, out),
                            this::writeNothing);
        } catch (final Exception e) {
            log.error(e.getMessage());
        }
        return SKIP_BODY;
    }

    private void writeApplication(final Application application, final JspWriter out) {
        final var resourceBundle = getResourceBundle();
        final var clientHasAppProperty = resourceBundle.getString("client_has_approved_application");
        final var numberProperty = resourceBundle.getString("number");
        final var startDateProperty = resourceBundle.getString("start_date");
        final var endDateProperty = resourceBundle.getString("end_date");
        try {
            out.print("<fieldset style=\"width: 30%\">");
            out.print("<legend style=\"color:green\">" + clientHasAppProperty + "</legend>");
            out.print("<b>" + numberProperty + ":</b>" + application.getApartment().getNumber() + "<br/>");
            out.print("<b>" + startDateProperty + ": </b>" + application.getStartDate().get() + "<br/>");
            out.print("<b>" + endDateProperty + ": </b>" + application.getEndDate().get() + "<br/>");
            out.print("</fieldset>");
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }


    private ResourceBundle getResourceBundle() {
        final var locale = (Locale) pageContext.getSession().getAttribute(LANGUAGE);
        return ResourceBundle.getBundle("message", locale);
    }

    private void writeNothing() {
    }
}
