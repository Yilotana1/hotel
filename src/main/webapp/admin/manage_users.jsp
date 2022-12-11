<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: Anatoliy
  Date: 05.12.2022
  Time: 15:17
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

<h2><fmt:message key="users_management_page"/></h2>

<form action="${pageContext.request.contextPath}/admin/manage-users">
    <fmt:message key="login"/>: <input type="text" name="login"/>
    <input type="hidden" name="by_login" value="yes"/>
    <fmt:message key="search" var="search"/>
    <input type="submit" value="${search}"/>
</form>
<form action="${pageContext.request.contextPath}/admin/manage-users">
    <fmt:message key="list_all" var="list_all"/>
    <input type="submit" value="${list_all}"/>
</form>
<jsp:include page="../lists/users/users_management_list.jsp"/>
<form action="${pageContext.request.contextPath}/profile">
    <fmt:message key="back" var="back"/>
    <input type="submit" value="${back}"/>
</form>
</body>
</html>
