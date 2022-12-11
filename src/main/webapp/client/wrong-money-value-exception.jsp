<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%--
  Created by IntelliJ IDEA.
  User: Anatoliy
  Date: 11.12.2022
  Time: 00:14
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
<jsp:include page="../locale_buttons.jsp"/>
<h2><fmt:message key="wrong_money_value"/></h2>
<fmt:message key="profile" var="profile"/>
<form action="${pageContext.request.contextPath}/profile">
    <input type="submit" value="${profile}">
</form>
</body>
</html>
