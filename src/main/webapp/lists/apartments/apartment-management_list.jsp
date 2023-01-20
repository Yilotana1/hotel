<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.example.hotel.model.entity.enums.ApartmentClass" %>
<%@ page import="com.example.hotel.model.entity.enums.ApartmentStatus" %>
<!DOCTYPE html>
<html>
<body>
<fmt:setLocale value="${sessionScope.lang.getLanguage()}"/>
<fmt:setBundle basename="message"/>

<table class="table table-striped table-bordered table-hover w-75">
    <tr>
        <th><fmt:message key="number"/></th>
        <th><fmt:message key="floor"/></th>
        <th><fmt:message key="busy"/></th>
        <th><fmt:message key="class"/></th>
        <th><fmt:message key="number_of_people"/></th>
        <th><fmt:message key="price"/></th>
        <th><fmt:message key="demand"/></th>
        <th><fmt:message key="resident_login"/></th>
    </tr>
    <c:forEach var="apartmentEntry" items="${requestScope.apartments.entrySet()}">
        <form action="${pageContext.request.contextPath}/admin/edit-apartment" method="post">
            <input type="hidden" name="number" value="${apartmentEntry.getKey().number}"/>
            <tr>
                <th>${apartmentEntry.getKey().number}</th>
                <th>${apartmentEntry.getKey().floor}</th>
                <th>
                    <div class="form-check form-switch">
                        <c:if test="${apartmentEntry.getKey().status == ApartmentStatus.BUSY}">
                            <input type="checkbox" class="form-check-input" role="switch" name="busy" value="yes" checked>
                        </c:if>
                        <c:if test="${apartmentEntry.getKey().status != ApartmentStatus.BUSY}">
                            <input type="checkbox" class="form-check-input" role="switch" name="busy" value="yes">
                        </c:if>
                    </div>
                </th>
                <th>
                    <select name="apartment_class_id">
                        <c:forEach var="apartment_class" items="${ApartmentClass.values()}">
                            <c:if test="${apartmentEntry.getKey().apartmentClass == apartment_class}">
                                <option value="${apartment_class.id}" selected="selected">${apartment_class}</option>
                            </c:if>
                            <c:if test="${apartmentEntry.getKey().apartmentClass != apartment_class}">
                                <option value="${apartment_class.id}">${apartment_class}</option>
                            </c:if>
                        </c:forEach>
                    </select>
                </th>
                <th>${apartmentEntry.getKey().numberOfPeople}</th>
                <fmt:message key="currency_koef" var="koef"/>
                <fmt:message key="currency_sign" var="sign"/>
                <th>
                    <input type="text" name="price" value="${apartmentEntry.getKey().price * koef} ${sign}"/>
                </th>
                <th>${apartmentEntry.getKey().demand}</th>
                <th>
                    <c:if test="${apartmentEntry.getValue() != null}">
                        ${apartmentEntry.getValue()}
                    </c:if>
                    <c:if test="${apartmentEntry.getValue() == null}">
                        <fmt:message key="no_resident"/>
                    </c:if>
                </th>
                <th>
                    <fmt:message key="edit" var="edit"/>
                    <input type="submit" value="${edit}" class="btn btn-primary text-white">
                </th>
            </tr>
        </form>
    </c:forEach>
</table>
<jsp:include page="../pages.jsp"/>
</body>
</html>
