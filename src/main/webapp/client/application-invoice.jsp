<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
    <style>
        fieldset {
            width: 30%;
        }
    </style>
    <jsp:include page="../parts/common.jsp"/>
</head>
<body>
<fmt:setLocale value="${sessionScope.lang.getLanguage()}"/>
<fmt:setBundle basename="message"/>
<jsp:include page="../locale_buttons.jsp"/>
<div class="mx-3 my-3">
    <fieldset>
        <table class="table table-striped table-bordered table-hover w-75">
            <tr>
                <th><fmt:message key="floor"/>:</th>
                <td>${requestScope.application.apartment.floor}</td>
            </tr>
            <tr>
                <th><fmt:message key="number"/>:</th>
                <td>${requestScope.application.apartment.number}</td>
            </tr>
            <tr>
                <th><fmt:message key="class"/>:</th>
                <td>${requestScope.application.apartment.apartmentClass}</td>
            </tr>
            <tr>
                <th><fmt:message key="price"/>:</th>
                <fmt:message key="currency_koef" var="koef"/>
                <td>${requestScope.application.price * koef} <fmt:message key="currency_sign"/></td>
            </tr>
            <tr>
                <th><fmt:message key="start_date"/>:</th>
                <td>${requestScope.application.startDate.get()}</td>
            </tr>
            <tr>
                <th><fmt:message key="end_date"/>:</th>
                <td>${requestScope.application.endDate.get()}</td>
            </tr>
        </table>
    </fieldset>
    <table>
        <tr>
            <th>
                <form action="${pageContext.request.contextPath}/client/confirm-payment" method="post">
                    <input type="hidden" name="application_id" value="${requestScope.application.id}">
                    <input type="hidden" name="start_date" value="${requestScope.application.startDate.get()}">
                    <input type="hidden" name="end_date" value="${requestScope.application.endDate.get()}">
                    <fmt:message key="confirm" var="confirm"/>
                    <input type="submit" value="${confirm}" class="btn btn-primary text-white"/>
                </form>
            </th>
            <th>
                <form action="${pageContext.request.contextPath}/client/cancel-application" method="post">
                    <input type="hidden" name="application_id" value="${requestScope.application.id}">
                    <fmt:message key="cancel" var="cancel"/>
                    <input type="submit" value="${cancel}" class="btn btn-danger text-white"/>
                </form>
            </th>
        </tr>
    </table>
    <c:if test="${!(sessionScope.get('error/client/application-invoice') == null)}">
        <span class="alert alert-danger my-3" role="alert"><fmt:message
                key="${sessionScope.get('error/client/application-invoice')}"/></span>
    </c:if>
</div>
</body>
</html>
