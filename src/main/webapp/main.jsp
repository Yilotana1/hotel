<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <title>Hotel-main</title>
</head>
<body>
<h1><%= "Hotel" %>
</h1>
<br/>
<c:if test="${sessionScope.roles == null}">
    <a href="signup.jsp">Sign up!</a><br/>
    <a href="login.jsp">Sign in!</a><br/>
</c:if>
<c:if test="${sessionScope.roles != null}">
    <a href="logout">Sign out!</a>
</c:if>
</body>
</html>