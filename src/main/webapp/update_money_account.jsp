<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <title>Title</title>
</head>
<body>
<fmt:setLocale value="${sessionScope.lang.getLanguage()}"/>
<fmt:setBundle basename="message"/>
<form action="${pageContext.request.contextPath}/client/update-money" method="post">
    <fmt:message key="currency_koef" var="koef"/>
    <h4><fmt:message key="current_balance"/>: ${requestScope.user.money * koef}<fmt:message key="currency_sign"/></h4>
    <fmt:message key="update_money"/>: <input type="text" name="money" value="0"/>
    <fmt:message key="submit" var="submit"/>
    <input type="submit" value="${submit}" class="btn btn-primary text-white">
</form>
</body>
</html>
