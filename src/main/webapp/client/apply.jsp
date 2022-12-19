<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: Anatoliy
  Date: 07.12.2022
  Time: 02:12
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
    <style>
        <jsp:include page="../styles/style.css"/>
    </style>
</head>
<body>
<fmt:setLocale value="${sessionScope.lang.getLanguage()}"/>
<fmt:setBundle basename="message"/>
<jsp:include page="../locale_buttons.jsp"/>
<h3><fmt:message key="confirm_application"/></h3>
<table>
    <tr>
        <th><fmt:message key="floor"/></th>
        <th><fmt:message key="class"/></th>
        <th><fmt:message key="number_of_people"/></th>
        <th><fmt:message key="price"/></th>
        <th><fmt:message key="demand"/></th>
    </tr>
    <tr>
        <th>${requestScope.apartment.floor}</th>
        <th>${requestScope.apartment.apartmentClass}</th>
        <th>${requestScope.apartment.numberOfPeople}</th>
        <fmt:message key="currency_koef" var="koef"/>
        <th>${requestScope.apartment.price * koef} <fmt:message key="currency_sign"/></th>
        <th>${requestScope.apartment.demand}</th>
    </tr>
</table>
<br/>

<form action="${pageContext.request.contextPath}/client/apply" method="post">
    <input type="hidden" name="number" value="${requestScope.apartment.number}">
    <table style="{border: none}">
        <tr>
            <th><fmt:message key="stay_length"/>:</th>
            <th><input type="text" name="stay_length"/></th>
        </tr>
    </table>
    <fmt:message key="apply" var="apply"/>
    <input type="submit" value="${apply}">
</form>
<c:if test="${sessionScope.get('error/client/show-application-page') != null}">
    <span style="color:red"><fmt:message key="${sessionScope.get('error/client/show-application-page')}"/></span>
</c:if>
<fmt:message key="main" var="main"/>
<br/>
<form action="${pageContext.request.contextPath}/main">
    <input type="submit" value="${main}">
</form>
</body>
</html>
