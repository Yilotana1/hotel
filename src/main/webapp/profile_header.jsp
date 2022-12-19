<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="custom" uri="custom" %>
<%@ page import="com.example.hotel.model.entity.enums.UserStatus" %>
<%--
  Created by IntelliJ IDEA.
  User: Anatoliy
  Date: 03.12.2022
  Time: 20:49
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
<fmt:setLocale value="${sessionScope.lang.getLanguage()}"/>
<fmt:setBundle basename="message"/>
<jsp:include page="locale_buttons.jsp"/>
<c:if test="${requestScope.user.status.equals(UserStatus.BLOCKED)}">
    <b style="color: red"><fmt:message key="you_are_blocked"/></b>
</c:if>
<br/>
<custom:approvedApplication/>

<c:if test="${requestScope.application != null}">
    <fmt:message key="application_to_confirm"/><br/>
    <a href="${pageContext.request.contextPath}/client/application-invoice">See</a>
</c:if>
<br/>
<h3><fmt:message key="information_about_you"/>:</h3>
<form action="${pageContext.request.contextPath}/edit-profile" method="post">
    <table>
        <input type="hidden" name="status" value="${requestScope.user.status.id}"/>
        <tr>
            <th><fmt:message key="firstname"/>:</th>
            <th><input name="firstname" value="${requestScope.user.firstname}"/></th>
        </tr>
        <tr>
            <th><fmt:message key="lastname"/>:</th>
            <th><input name="lastname" value="${requestScope.user.lastname}"/></th>
        </tr>
        <tr>
            <th><fmt:message key="phone"/>:</th>
            <th><input name="phone" value="${requestScope.user.phone}"/></th>
        </tr>
        <tr>
            <th><fmt:message key="email"/>:</th>
            <th><input name="email" value="${requestScope.user.email}"/></th>
        </tr>
        <tr>
            <th><fmt:message key="login"/>:</th>
            <th><input name="login" value="${requestScope.user.login}"/></th>
        </tr>
        <tr>
            <th><fmt:message key="password"/>:</th>
            <th><input type="password" name="password" value="${requestScope.user.password}"/></th>
        </tr>
    </table>
    <fmt:message key="edit" var="edit"/>
    <input type="submit" value="${edit}">
    <c:if test="${!(sessionScope.get('error/profile') == null)}">
        <span style="color:red"><fmt:message key="${sessionScope.get('error/profile')}"/></span>
    </c:if>
</form>
</body>
</html>
