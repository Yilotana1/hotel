<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.example.hotel.model.entity.enums.ApartmentClass" %>
<!DOCTYPE html>
<html>
<head>
    <title>Title</title>
    <jsp:include page="../parts/common.jsp"/>
</head>
<body>
<fmt:setLocale value="${sessionScope.lang.getLanguage()}"/>
<fmt:setBundle basename="message"/>
<jsp:include page="../locale_buttons.jsp"/>
<div class="my-5 w-25 d-flex flex-column align-items-center mx-auto">
    <form action="${pageContext.request.contextPath}/client/make-temporary-application" method="post">
        <table>
            <tr>
                <th><fmt:message key="stay_length"/>:</th>
                <th><input type="number" name="stay_length"/></th>
            </tr>
            <tr>
                <th><fmt:message key="number_of_people"/>:</th>
                <th><input type="number" name="number_of_people"/></th>
            </tr>
            <tr>
                <th><fmt:message key="class"/>:</th>
                <th>
                    <div class="input-group mx-3 my-2">
                        <select class="custom-select" name="apartment_class_id" id="apartment_class_id">
                            <option value="${ApartmentClass.STANDARD.id}">
                                Standard
                            </option>
                            <option value="${ApartmentClass.STUDIO.id}">
                                Studio
                            </option>
                            <option value="${ApartmentClass.SUITE.id}">
                                Suite
                            </option>
                            <option value="${ApartmentClass.SUPERIOR.id}">
                                Superior
                            </option>
                        </select>
                    </div>
                </th>
            </tr>
        </table>
        <br/>
        <fmt:message key="apply" var="apply"/>
        <input class="btn btn-primary text-white" type="submit" value="${apply}"/>
    </form>
    <br/>
    <c:if test="${sessionScope.get('error/client/make-temporary-application.jsp') != null}">
        <span class="alert alert-danger my-3" role="alert"><fmt:message key="${sessionScope.get('error/client/make-temporary-application.jsp')}"/></span>
    </c:if>
</div>
</body>
</html>
