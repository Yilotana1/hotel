<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: Anatoliy
  Date: 09.12.2022
  Time: 00:34
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
    <style>
        fieldset{
            width: 30%;
        }
    </style>
</head>
<body>
<fmt:setLocale value="${sessionScope.lang.getLanguage()}"/>
<fmt:setBundle basename="message"/>
<jsp:include page="../locale_buttons.jsp"/>
<fieldset>
    <table>
        <tr>
            <th><b><fmt:message key="floor"/>: </b></th>
            <th>${requestScope.application.apartment.floor}</th>
        </tr>
        <tr>
            <th><b><fmt:message key="number"/>: </b></th>
            <th>${requestScope.application.apartment.number}</th>
        </tr>
        <tr>
            <th><b><fmt:message key="class"/>: </b></th>
            <th>${requestScope.application.apartment.apartmentClass}</th>
        </tr>
        <tr>
            <th><b><fmt:message key="price"/>: </b></th>
            <fmt:message key="currency_koef" var="koef"/>
            <th>${requestScope.application.price * koef} <fmt:message key="currency_sign"/></th>
        </tr>
        <tr>
            <th><b><fmt:message key="start_date"/>: </b></th>
            <th>${requestScope.application.startDate.get()}</th>
        </tr>
        <tr>
            <th><b><fmt:message key="end_date"/>: </b></th>
            <th>${requestScope.application.endDate.get()}</th>
        </tr>
    </table>
</fieldset>
<br/>
<table>
    <tr>
        <th>
            <form action="${pageContext.request.contextPath}/client/confirm-payment" method="post">
                <input type="hidden" name="application_id" value="${requestScope.application.id}">
                <input type="hidden" name="start_date" value="${requestScope.application.startDate.get()}">
                <input type="hidden" name="end_date" value="${requestScope.application.endDate.get()}">
                <fmt:message key="confirm" var="confirm"/>
                <input type="submit" value="${confirm}"/>
            </form>
        </th>
        <th>
            <form action="${pageContext.request.contextPath}/client/cancel-application" method="post">
                <input type="hidden" name="application_id" value="${requestScope.application.id}">
                <fmt:message key="cancel" var="cancel"/>
                <input type="submit" value="${cancel}"/>
            </form>
        </th>
    </tr>
</table>
<c:if test="${!(requestScope.error == null)}">
    <span style="color:red"><fmt:message key="${requestScope.error}"/></span>
</c:if>
<br/>
<fmt:message key="main" var="main"/>
<form action="${pageContext.request.contextPath}/main">
    <input type="submit" value="${main}">
</form>
</body>
</html>
