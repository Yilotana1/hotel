<%--
  Created by IntelliJ IDEA.
  User: Anatoliy
  Date: 28.11.2022
  Time: 21:43
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Login</title>
</head>
<body>
<form action="login" method="get">
    <table>
        <tr>
            <th>Login: </th>
            <th><input type="text" name="login"/></th>
        </tr>
        <tr>
            <th>Password: </th>
            <th><input type="text" name="password"/></th>
        </tr>
    </table>
    <input type="submit"/>
    <c:if test="${!(requestScope.error == null)}">
        <span style="color:red">${requestScope.error}</span>
    </c:if>
</form>
</body>
</html>
