<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%--
  Created by IntelliJ IDEA.
  User: Anatoliy
  Date: 03.12.2022
  Time: 20:58
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
<body>
<fmt:setLocale value="${sessionScope.lang.name()}"/>
<fmt:setBundle basename="message"/>

<jsp:include page="profile_header.jsp"/>
<fmt:message key="main" var="main"/>
<form action="${pageContext.request.contextPath}/main">
    <input type="submit" value="${main}">
</form>
<a href="logout"><fmt:message key="signout"/></a>
</body>
</html>
