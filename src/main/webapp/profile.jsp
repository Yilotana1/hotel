<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="custom" uri="custom" %>
<%@ page import="com.example.hotel.model.entity.enums.Role" %>
<%--
  Created by IntelliJ IDEA.
  User: Anatoliy
  Date: 03.12.2022
  Time: 20:58
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
<body>
<fmt:setLocale value="${sessionScope.lang.name()}"/>
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
</c:if>
<c:if test="${sessionScope.roles.contains(Role.MANAGER)}">
    <a href="manager/list-users"><fmt:message key="list_users"/></a>
    <br/>
</c:if>

<a href="logout"><fmt:message key="signout"/></a>
</body>
</html>
