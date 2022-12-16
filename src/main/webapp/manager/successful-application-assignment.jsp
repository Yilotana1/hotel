<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
<fmt:setLocale value="${sessionScope.lang.getLanguage()}"/>
<fmt:setBundle basename="message"/>
<jsp:include page="../locale_buttons.jsp"/>
<fmt:message key="successful_application_assignment"/>
<c:if test="${sessionScope.roles != null}">
    <br/>
    <a href="profile"><fmt:message key="profile"/></a>
</c:if>
</body>
</html>
