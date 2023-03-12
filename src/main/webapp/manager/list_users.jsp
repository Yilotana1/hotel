<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <title>Title</title>
    <style>
        <jsp:include page="../styles/style.css"/>
    </style>
    <jsp:include page="../parts/common.jsp"/>
</head>
<body>
<fmt:setLocale value="${sessionScope.lang.getLanguage()}"/>
<fmt:setBundle basename="message"/>
<jsp:include page="../locale_buttons.jsp"/>
<div class="mx-3 my-3">
    <form action="${pageContext.request.contextPath}/manager/list-users">
        <fmt:message key="login"/>: <input type="text" name="login"/>
        <input type="hidden" name="by_login" value="yes"/>
        <fmt:message key="search" var="search"/>
        <input type="submit" value="${search}" class="btn btn-primary text-white"/>
    </form>
    <form action="${pageContext.request.contextPath}/manager/list-users">
        <fmt:message key="list_all" var="list_all"/>
        <input type="submit" value="${list_all}" class="btn btn-primary text-white"/>
    </form>
    <jsp:include page="../lists/users/users_list.jsp"/>
    <form action="${pageContext.request.contextPath}/profile">
        <fmt:message key="back" var="back"/>
        <input type="submit" value="${back}" class="btn btn-primary text-white"/>
    </form>
</div>
</body>
</html>
