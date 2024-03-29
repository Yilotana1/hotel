package com.example.hotel.controller.action;


import com.example.hotel.controller.commons.Constants.RequestAttributes;
import com.example.hotel.controller.commons.Constants.RequestAttributes.PaginationAttributes;
import com.example.hotel.controller.commons.Constants.RequestParameters;
import com.example.hotel.controller.commons.Path;
import com.example.hotel.model.entity.Apartment;
import com.example.hotel.model.service.ApartmentService;
import com.example.hotel.model.service.factory.ServiceFactory;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;

import static com.example.hotel.controller.commons.Tools.getPageId;
import static java.util.Objects.requireNonNullElse;

/**
 * Prepares data to show on the main page and make redirect to main.jsp
 */
public class MainAction implements Action {


  public static final Logger log = Logger.getLogger(MainAction.class);
  private static final String DEFAULT_SORTING = "price";
  private static final int PAGE_SIZE = 8;
  private final ApartmentService apartmentService = ServiceFactory.getInstance().createApartmentService();
  private final Map<String, BiFunction<Integer, Integer, Collection<Apartment>>> sortingMethods =
          new HashMap<>() {{
            put("price", apartmentService::getApartmentsSortedByPrice);
            put("number_of_people", apartmentService::getApartmentsSortedByPeople);
            put("class", apartmentService::getApartmentsSortedByClass);
            put("status", apartmentService::getApartmentsSortedByStatus);
          }};

  public MainAction() {
  }
  @Override
  public void execute(final HttpServletRequest request, final HttpServletResponse response) throws ServletException, IOException {
    try {
      final var pageNumber = getPageId(request);
      final var skip = (pageNumber - 1) * PAGE_SIZE;

      final var requestedSorting = request.getParameter(RequestParameters.SORTED_BY);
      final var sortingMethod = sortingMethods.get(
              requireNonNullElse(requestedSorting, DEFAULT_SORTING));

      final var apartments = sortingMethod.apply(skip, PAGE_SIZE);
      request.setAttribute(RequestAttributes.APARTMENTS, apartments);

      final var totalPagesNumber = apartmentService.count() / PAGE_SIZE;

      setAttributesForPaging(request, requestedSorting, pageNumber, totalPagesNumber);
      request.getRequestDispatcher(Path.Get.User.MAIN_PAGE).forward(request, response);
      log.debug("Forward to main.jsp");
    } catch (final Exception e) {
      log.error(e.getMessage(), e);
      response.sendRedirect(request.getContextPath() + Path.Get.Error.ERROR_503);
    }

  }


  private void setAttributesForPaging(final HttpServletRequest request,
                                      final String sorted_by,
                                      final int pageNumber,
                                      final int totalPagesNumber) {
    request.setAttribute(PaginationAttributes.TOTAL_PAGES_NUMBER, totalPagesNumber);
    request.setAttribute(PaginationAttributes.PAGE, pageNumber);
    request.setAttribute(PaginationAttributes.LIST_OF_SORTING_OPTIONS, sortingMethods.keySet());
    request.setAttribute(PaginationAttributes.SORTED_BY, sorted_by);
    request.setAttribute(PaginationAttributes.DEFAULT_SORTING, DEFAULT_SORTING);

  }

}
