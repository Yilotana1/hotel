package com.example.hotel.controller.command;


import com.example.hotel.model.entity.Apartment;
import com.example.hotel.model.service.ApartmentService;
import com.example.hotel.model.service.exception.ServiceException;
import com.example.hotel.model.service.factory.ServiceFactory;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;

import static com.example.hotel.controller.Path.ERROR_503_PAGE;
import static com.example.hotel.controller.Path.MAIN_PAGE;
import static java.lang.Integer.parseInt;
import static java.util.Objects.requireNonNullElse;

/**
 * Prepares data to show on the main page and make redirect to main.jsp
 */
public class MainCommand implements Command {


    public final static Logger log = Logger.getLogger(MainCommand.class);
    private static final String DEFAULT_SORTING = "price";
    private static final int PAGE_SIZE = 10;
    private static final String SORTED_BY = "sorted_by";
    private static final String APARTMENTS = "apartments";
    private static final String LIST_OF_SORTING_OPTIONS = "sorted_by_list";
    private static final String PAGE_NUMBER_INPUT = "page";
    private static final String TOTAL_PAGES_NUMBER = "count";
    private static final String DEFAULT_PAGE_NUMBER = "1";
    private ApartmentService apartmentService = ServiceFactory.getInstance().createApartmentService();
    private final Map<String, BiFunction<Integer, Integer, List<Apartment>>> sortingMethods = new HashMap<>() {{
        put("price", apartmentService::getApartmentsSortedByPrice);
        put("number_of_people", apartmentService::getApartmentsSortedByPeople);
        put("class", apartmentService::getApartmentsSortedByClass);
        put("status", apartmentService::getApartmentsSortedByStatus);
    }};

    public MainCommand() {
    }

    public MainCommand(ServiceFactory serviceFactory) {
        apartmentService = serviceFactory.createApartmentService();
    }

    @Override
    public void execute(final HttpServletRequest request, final HttpServletResponse response) throws ServletException, IOException {
        log.debug("Command started executing");
        final var pageNumber = parseInt(requireNonNullElse(request.getParameter(PAGE_NUMBER_INPUT), DEFAULT_PAGE_NUMBER));
        final var skip = (pageNumber - 1) * PAGE_SIZE;

        final var requestedSortingOption = request.getParameter(SORTED_BY);
        final var sortingMethod = sortingMethods.get(
                requireNonNullElse(requestedSortingOption, DEFAULT_SORTING));
        try {
            final var apartments = sortingMethod.apply(skip, PAGE_SIZE);
            request.setAttribute(APARTMENTS, apartments);
            final var totalPagesNumber = apartmentService.count() / PAGE_SIZE;
            request.setAttribute(TOTAL_PAGES_NUMBER, totalPagesNumber);
        } catch (ServiceException e) {
            log.error(e.getMessage());
            request.getRequestDispatcher(ERROR_503_PAGE).forward(request, response);
        }
        setAttributesForPaging(request, requestedSortingOption, pageNumber);
        request.getRequestDispatcher(MAIN_PAGE).forward(request, response);
        log.debug("Forward to main.jsp");
    }


    private void setAttributesForPaging(HttpServletRequest request, String sorted_by, int pageNumber) {
        request.setAttribute(PAGE_NUMBER_INPUT, pageNumber);
        request.setAttribute(LIST_OF_SORTING_OPTIONS, sortingMethods.keySet());
        request.setAttribute(SORTED_BY, sorted_by);
    }

}
