<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="com.example.hotel.model.entity.enums.Role" %>
<html>
<head>
    <style>
        .th {
            text-align: center;
        }
    </style>
</head>
<body>
<fmt:setLocale value="${sessionScope.lang.getLanguage()}"/>
<fmt:setBundle basename="message"/>
<jsp:include page="selection.jsp"/>
<table class="table table-striped table-bordered table-hover w-75 mx-3">
    <thead>
    <tr>
        <th scope="col" class="th"><fmt:message key="floor"/></th>
        <th scope="col" class="th"><fmt:message key="status"/></th>
        <th scope="col" class="th"><fmt:message key="class"/></th>
        <th scope="col" class="th"><fmt:message key="number_of_people"/></th>
        <th scope="col" class="th"><fmt:message key="price"/></th>
        <th scope="col" class="th"><fmt:message key="demand"/></th>
    </tr>
    </thead>
    <tbody>
    <c:forEach var="apartment" items="${requestScope.apartments}">
        <tr>
            <td class="th">${apartment.floor}</td>
            <td class="th">${apartment.status}</td>
            <td class="th">${apartment.apartmentClass}</td>
            <td class="th">${apartment.numberOfPeople}</td>
            <fmt:message key="currency_koef" var="koef"/>
            <td class="th">${apartment.price * koef} <fmt:message key="currency_sign"/></td>
            <td class="th">${apartment.demand}</td>
            <c:if test="${sessionScope.roles.contains(Role.CLIENT)}">
                <td class="th">
                    <button type="button" class="btn btn-primary" data-bs-toggle="modal" data-bs-target="#exampleModal">
                        <fmt:message key="apply"/>
                    </button>
                    <form action="${pageContext.request.contextPath}/client/apply" method="post">
                        <div class="modal fade" id="exampleModal" tabindex="-1" aria-labelledby="exampleModalLabel"
                             aria-hidden="true">
                            <div class="modal-dialog">
                                <div class="modal-content">
                                    <div class="modal-header">
                                        <h1 class="modal-title fs-5" id="exampleModalLabel">
                                            <fmt:message key="confirm_application"/></h1>
                                        <button type="button" class="btn-close" data-bs-dismiss="modal"
                                                aria-label="Close"></button>
                                    </div>
                                    <div class="modal-body">
                                        <table class="w-100">
                                            <tr>
                                                <th><fmt:message key="floor"/></th>
                                                <th><fmt:message key="class"/></th>
                                                <th><fmt:message key="number_of_people"/></th>
                                                <th><fmt:message key="price"/></th>
                                                <th><fmt:message key="demand"/></th>
                                            </tr>
                                            <tr>
                                                <td>${apartment.floor}</td>
                                                <td>${apartment.apartmentClass}</td>
                                                <td>${apartment.numberOfPeople}</td>
                                                <fmt:message key="currency_koef" var="koef"/>
                                                <td>${apartment.price * koef} <fmt:message
                                                        key="currency_sign"/></td>
                                                <td>${apartment.demand}</td>
                                            </tr>
                                        </table>
                                        <input type="hidden" name="number" value="${apartment.number}">
                                        <br/>
                                        <fmt:message key="stay_length"/>:
                                        <input type="number" name="stay_length" class="my-1"/>
                                    </div>
                                    <div class="modal-footer">
                                            <fmt:message key="apply" var="apply"/>
                                        <input type="submit" class="btn btn-primary" value="${apply}">
                    </form>
                </td>
            </c:if>
        </tr>
    </c:forEach>
    </tbody>
</table>
<div class="mx-3">
    <jsp:include page="../pages.jsp"/>
</div>
</body>
</html>
