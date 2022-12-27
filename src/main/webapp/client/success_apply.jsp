<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
    <jsp:include page="../parts/common.jsp"/>
</head>
<body>
<fmt:setLocale value="${sessionScope.lang.getLanguage()}"/>
<fmt:setBundle basename="message"/>
<jsp:include page="../locale_buttons.jsp"/>
<h3><fmt:message key="application_saved"/></h3>
<br/>
<fmt:message key="main" var="main"/>
<form action="${pageContext.request.contextPath}/main">
    <input type="submit" value="${main}" class="btn btn-primary text-white">
</form>
</body>
</html>
