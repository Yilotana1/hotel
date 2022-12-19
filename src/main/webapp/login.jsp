<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
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
<fmt:setLocale value="${sessionScope.lang.getLanguage()}"/>
<fmt:setBundle basename="message"/>
<jsp:include page="locale_buttons.jsp"/>
<form action="login" method="get">
    <table>
        <tr>
            <th><fmt:message key="login"/></th>
            <th><input type="text" name="login"/></th>
        </tr>
        <tr>
            <th><fmt:message key="password"/>
            </th>
            <th><input type="text" name="password"/></th>
        </tr>
    </table>
    <fmt:message key="submit" var="submit"/>
    <input type="submit" value="${submit}"/>
    <c:if test="${!(sessionScope.get('error/login.jsp') == null)}">
        <span style="color:red"><fmt:message key="${sessionScope.get('error/login.jsp')}"/></span>
    </c:if>
</form>
</body>
</html>
