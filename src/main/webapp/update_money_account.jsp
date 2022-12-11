<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%--
  Created by IntelliJ IDEA.
  User: Anatoliy
  Date: 06.12.2022
  Time: 20:57
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
<fmt:setLocale value="${sessionScope.lang.getLanguage()}"/>
<fmt:setBundle basename="message"/>
<form action="${pageContext.request.contextPath}/client/update-money">
    <fmt:message key="currency_koef" var="koef"/>
    <h4><fmt:message key="current_balance"/>: ${requestScope.user.money * koef}<fmt:message key="currency_sign"/></h4>
    <fmt:message key="update_money"/>: <input type="text" name="money" value="0"/>
    <fmt:message key="submit" var="submit"/>
    <input type="submit" value="${submit}">
</form>
</body>
</html>
