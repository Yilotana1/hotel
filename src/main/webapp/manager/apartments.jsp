<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
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
<jsp:include page="../lists/apartments/apartments-details.jsp"/>
<form action="${pageContext.request.contextPath}/profile">
    <fmt:message key="back" var="back"/>
    <input type="submit" value="${back}"/>
</form>
</body>
</html>
