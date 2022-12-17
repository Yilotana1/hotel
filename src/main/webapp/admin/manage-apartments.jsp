<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
    <style>
        <jsp:include page="../styles/style.css"/>
    </style>
</head>
<body>
<fmt:setLocale value="${sessionScope.lang.getLanguage()}"/>
<fmt:setBundle basename="message"/>
<jsp:include page="../locale_buttons.jsp"/>
<form action="${pageContext.request.contextPath}/admin/show-apartment-management">
    <fmt:message key="resident_login"/>: <input type="text" name="resident_login"/>
    <fmt:message key="search" var="search"/>
    <input type="submit" value="${search}"/>
</form>
<jsp:include page="../lists/apartments/apartment-management_list.jsp"/>
<c:if test="${!(requestScope.error == null)}">
    <span style="color:red"><fmt:message key="${requestScope.error}"/></span>
</c:if>
<form action="${pageContext.request.contextPath}/profile">
    <fmt:message key="back" var="back"/>
    <input type="submit" value="${back}"/>
</form>
</body>
</html>