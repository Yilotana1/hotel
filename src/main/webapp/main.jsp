<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
    <title>Hotel-main</title>
</head>
<body>
<fmt:setLocale value="${sessionScope.lang.name()}"/>
<fmt:setBundle basename="message"/>
<jsp:include page="locale_buttons.jsp"/>

<h1><fmt:message key="main_title"/>
</h1>
<br/>
<c:if test="${sessionScope.roles == null}">
    <a href="signup.jsp"><fmt:message key="signup"/></a><br/>
    <a href="login.jsp"><fmt:message key="signin"/></a><br/>
</c:if>
<c:if test="${sessionScope.roles != null}">
    <a href="logout">Sign out!</a>
</c:if>

<br/>
<br/>
<br/>
</body>
</html>