<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
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
<fmt:setLocale value="${sessionScope.lang.name()}"/>
<fmt:setBundle basename="message"/>
<jsp:include page="locale_buttons.jsp"/>

<c:if test="${requestScope.user.status.equals(UserStatus.BLOCKED)}">
    <b style="color: red"><fmt:message key="you_are_blocked"/></b>
</c:if>
<h3><fmt:message key="information_about_you"/>:</h3>
<form action="${pageContext.request.contextPath}/edit_profile">
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
    <c:if test="${!(requestScope.error == null)}">
        <span style="color:red"><fmt:message key="${requestScope.error}"/></span>
    </c:if>
</form>
</body>
</html>
