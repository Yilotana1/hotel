<%--
  Created by IntelliJ IDEA.
  User: Anatoliy
  Date: 26.11.2022
  Time: 22:58
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Sign up</title>
    <style>
        <jsp:include page="styles/style.css"/>
    </style>
</head>
<body>
<form action="signup" method="get">
    <table>
        <tr>
            <th>Login: </th>
            <th><input type="text" name="login"/></th>
        </tr>
        <tr>
            <th>Firstname: </th>
            <th><input type="text" name="firstname"/></th>
        </tr>
        <tr>
            <th>Lastname: </th>
            <th><input type="text" name="lastname"/></th>
        </tr>
        <tr>
            <th>Email: </th>
            <th><input type="text" name="email"/></th>
        </tr>
        <tr>
            <th>Phone: </th>
            <th><input type="text" name="phone"/></th>
        </tr>
        <tr>
            <th>Password: </th>
            <th><input type="password" name="password"/></th>
        </tr>
    </table>
    <br/>
    <input type="submit"/>
    <br/>
    <c:if test="${!(requestScope.error == null)}">
        <span style="color:red"><fmt:message key="${requestScope.error}"/></span>
    </c:if>
</form>
</body>
</html>
