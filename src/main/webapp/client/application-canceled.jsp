<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%--
  Created by IntelliJ IDEA.
  User: Anatoliy
  Date: 13.12.2022
  Time: 00:01
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
<jsp:include page="../locale_buttons.jsp"/>
<h2><fmt:message key="application_canceled"/></h2>
<fmt:message key="profile" var="profile"/>
<br/>
<form action="${pageContext.request.contextPath}/profile">
    <input type="submit" value="${profile}">
</form>
</body>
</html>
