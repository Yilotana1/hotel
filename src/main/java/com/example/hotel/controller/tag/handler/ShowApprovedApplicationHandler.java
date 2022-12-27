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
    public static final Logger log = Logger.getLogger(ShowApprovedApplicationHandler.class);
    private String modalStart;
    private String modalEnd;
    private final ApplicationService applicationService = ServiceFactory.getInstance().createApplicationService();

    @Override
    public int doStartTag() {
        final var resourceBundle = getResourceBundle();
        modalStart = getModalStart(resourceBundle);
        modalEnd = getModalEnd(resourceBundle);
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

    private static String getModalEnd(final ResourceBundle resourceBundle) {
        return "</div>\n" +
                "            <div class=\"modal-footer\">\n" +
                "                <button type=\"submit\" class=\"btn btn-secondary w-25\" data-bs-dismiss=\"modal\">"
                + resourceBundle.getString("close") + "</button>\n" +
                "            </div>\n" +
                "        </div>\n" +
                "    </div>\n" +
                "</div>";
    }

    private static String getModalStart(final ResourceBundle resourceBundle) {
        return "<button type=\"button\" class=\"btn btn-success my-3\" data-bs-toggle=\"modal\" data-bs-target=\"#exampleModal\">\n" +
                resourceBundle.getString("client_has_approved_application") + "</button>\n" +
                "\n" +
                "<div class=\"modal fade\" id=\"exampleModal\" tabindex=\"-1\" aria-labelledby=\"exampleModalLabel\" aria-hidden=\"true\">\n" +
                "    <div class=\"modal-dialog\">\n" +
                "        <div class=\"modal-content\">\n" +
                "            <div class=\"modal-header\">\n" +
                "                <h1 class=\"modal-title fs-5\" style=\"color:green\" id=\"exampleModalLabel\">\n" +
                "                    " + resourceBundle.getString("confirmed_application") + "</h1>\n" +
                "                <button type=\"button\" class=\"btn-close\" data-bs-dismiss=\"modal\" aria-label=\"Close\"></button>\n" +
                "            </div>\n" +
                "            <div class=\"modal-body\">";
    }

    private void writeApplication(final Application application, final JspWriter out) {
        final var resourceBundle = getResourceBundle();
        final var numberProperty = resourceBundle.getString("number");
        final var startDateProperty = resourceBundle.getString("start_date");
        final var endDateProperty = resourceBundle.getString("end_date");
        try {
            out.print(modalStart);
            out.print("<b>" + numberProperty + ":</b>" + application.getApartment().getNumber() + "<br/>");
            out.print("<b>" + startDateProperty + ": </b>" + application.getStartDate().get() + "<br/>");
            out.print("<b>" + endDateProperty + ": </b>" + application.getEndDate().get() + "<br/>");
            out.print(modalEnd);
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
