<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
    <title>Hotel-main</title>
    <style>
        <jsp:include page="styles/style.css"/>
    </style>
</head>
<body>
<fmt:setLocale value="${sessionScope.lang.getLanguage()}"/>
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
    <a href="logout"><fmt:message key="signout"/></a>
</c:if>
<c:if test="${sessionScope.roles != null}">
    <a href="profile"><fmt:message key="profile"/></a>
</c:if>
<br/>
<jsp:include page="lists/apartments/apartments_list.jsp"/>
<br/>
<form action="${pageContext.request.contextPath}/client/make-temporary-application.jsp">
    <fmt:message key="specify_preferred" var="specify_preferences"/>
    <input type="submit" value="${specify_preferences}">
</form>
</body>
</html>