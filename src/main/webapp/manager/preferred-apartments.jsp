<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: Anatoliy
  Date: 15.12.2022
  Time: 16:48
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
<table>
    <tr>
        <th><fmt:message key="number"/></th>
        <th><fmt:message key="floor"/></th>
        <th><fmt:message key="status"/></th>
        <th><fmt:message key="class"/></th>
        <th><fmt:message key="number_of_people"/></th>
        <th><fmt:message key="price"/></th>
        <th><fmt:message key="demand"/></th>
    </tr>
    <c:forEach var="apartment" items="${requestScope.apartments}">
        <tr>
            <th>${apartment.number}</th>
            <th>${apartment.floor}</th>
            <th>${apartment.status}</th>
            <th>${apartment.apartmentClass}</th>
            <th>${apartment.numberOfPeople}</th>
            <fmt:message key="currency_koef" var="koef"/>
            <th>${apartment.price * koef} <fmt:message key="currency_sign"/></th>
            <th>${apartment.demand}</th>
            <th>
                <form action="${pageContext.request.contextPath}/manager/apply-for-client">
                    <fmt:message key="choose" var="choose"/>
                    <input type="hidden" name="number" value="${apartment.number}">
                    <input type="hidden" name="client_login" value="${requestScope.client_login}">
                    <input type="hidden" name="stay_length" value="${requestScope.stay_length}">
                    <input type="submit" value="${choose}">
                </form>
            </th>
        </tr>
    </c:forEach>
</table>
<br/>
<br/>
<br/>
<jsp:include page="../lists/pages.jsp"/>
<form action="${pageContext.request.contextPath}/main">
    <fmt:message key="main" var="main"/>
    <input type="submit" value="${main}">
</form>
<form action="${pageContext.request.contextPath}/profile">
    <fmt:message key="profile" var="profile"/>
    <input type="submit" value="${profile}">
</form>
</body>
</html>
