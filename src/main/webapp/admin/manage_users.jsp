<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
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
    <h2><fmt:message key="users_management_page"/></h2>
    <form action="${pageContext.request.contextPath}/admin/manage-users">
        <fmt:message key="login"/>: <input type="text" name="login"/>
        <fmt:message key="search" var="search"/>
        <input type="submit" value="${search}" class="btn btn-primary text-white"/>
    </form>
    <form action="${pageContext.request.contextPath}/admin/manage-users">
        <fmt:message key="list_all" var="list_all"/>
        <input type="submit" value="${list_all}" class="btn btn-primary text-white"/>
    </form>
    <jsp:include page="../lists/users/users_management_list.jsp"/>
</div>
</body>
</html>
