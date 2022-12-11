<%--
  Created by IntelliJ IDEA.
  User: tolik
  Date: 18.06.2021
  Time: 13:24
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
<fmt:setLocale value="${sessionScope.lang.getLanguage()}"/>
<fmt:setBundle basename="message"/>
<jsp:include page="locale_buttons.jsp"/>
<h1>404</h1>
<form action="${pageContext.request.contextPath}/main">
    <fmt:message key="denied"/>
    <fmt:message key="main" var="main"/>
    <br/>
    <br/>
    <br/>
    <input type="submit" value="${main}">
</form>
</body>
</html>
