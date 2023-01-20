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
    <form action="${pageContext.request.contextPath}/admin/show-apartment-management">
        <fmt:message key="resident_login"/>: <input type="text" name="resident_login"/>
        <fmt:message key="search" var="search"/>
        <input type="submit" value="${search}" class="btn btn-primary text-white"/>
    </form>
    <jsp:include page="../lists/apartments/apartment-management_list.jsp"/>
    <c:if test="${!(sessionScope.get('error/admin/show-apartment-management') == null)}">
        <span class="alert alert-danger my-3" role="alert"><fmt:message
                key="${sessionScope.get('error/admin/show-apartment-management')}"/></span>
    </c:if>
</div>
</body>
</html>