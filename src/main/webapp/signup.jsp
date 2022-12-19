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
<fmt:setLocale value="${sessionScope.lang.getLanguage()}"/>
<fmt:setBundle basename="message"/>
<jsp:include page="locale_buttons.jsp"/>
<form action="signup" method="post">
    <table>
        <tr>
            <th><fmt:message key="login"/>: </th>
            <th><input type="text" name="login"/></th>
        </tr>
        <tr>
            <th><fmt:message key="firstname"/>: </th>
            <th><input type="text" name="firstname"/></th>
        </tr>
        <tr>
            <th><fmt:message key="lastname"/>: </th>
            <th><input type="text" name="lastname"/></th>
        </tr>
        <tr>
            <th><fmt:message key="email"/>: </th>
            <th><input type="text" name="email"/></th>
        </tr>
        <tr>
            <th><fmt:message key="phone"/>: </th>
            <th><input type="text" name="phone"/></th>
        </tr>
        <tr>
            <th><fmt:message key="password"/>: </th>
            <th><input type="password" name="password"/></th>
        </tr>
    </table>
    <br/>
    <fmt:message key="submit" var="submit"/>
    <input type="submit" value="${submit}"/>
    <br/>
    <c:if test="${!(sessionScope.get('error/signup.jsp') == null)}">
        <span style="color:red"><fmt:message key="${sessionScope.get('error/signup.jsp')}"/></span>
    </c:if>
</form>
<br/>
<br/>
<br/>
<fmt:message key="main" var="main"/>
<form action="${pageContext.request.contextPath}/main">
    <input type="submit" value="${main}">
</form>
</body>
</html>
