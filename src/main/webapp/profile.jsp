<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="custom" uri="custom" %>
<%@ page import="com.example.hotel.model.entity.enums.Role" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
<body>
<fmt:setLocale value="${sessionScope.lang.getLanguage()}"/>
<fmt:setBundle basename="message"/>
<jsp:include page="profile_header.jsp"/>
<fmt:message key="main" var="main"/>
<form action="${pageContext.request.contextPath}/main">
    <input type="submit" value="${main}">
</form>
<br/>
<c:if test="${sessionScope.roles.contains(Role.CLIENT)}">
    <br/>
    <jsp:include page="update_money_account.jsp"/>
    <br/>
</c:if>
<c:if test="${sessionScope.roles.contains(Role.ADMIN)}">
    <a href="admin/manage-users"><fmt:message key="manage_users"/></a>
    <br/>
    <a href="admin/show-apartment-management"><fmt:message key="manage_apartments"/></a>
    <br/>
</c:if>
<c:if test="${sessionScope.roles.contains(Role.MANAGER) && !sessionScope.roles.contains(Role.ADMIN)}">
    <a href="manager/list-users"><fmt:message key="list_users"/></a>
    <br/>
    <a href="manager/show-apartments"><fmt:message key="list_apartments"/></a>
    <br/>
</c:if>
<c:if test="${sessionScope.roles.contains(Role.MANAGER)}">
    <form action="${pageContext.request.contextPath}/manager/show-temporary-applications">
        <fmt:message key="manage_applications" var="manage_applications"/>
        <input type="submit" value="${manage_applications}"/>
    </form>
    <br/>
</c:if>
<a href="logout"><fmt:message key="signout"/></a>
</body>
</html>
