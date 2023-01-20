<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
    <title>Login</title>
    <jsp:include page="parts/common.jsp"/>
</head>
<body>
<fmt:setLocale value="${sessionScope.lang.getLanguage()}"/>
<fmt:setBundle basename="message"/>
<jsp:include page="locale_buttons.jsp"/>
<form action="${pageContext.request.contextPath}/login" method="get"
      class="my-5 w-25 d-flex flex-column align-items-center mx-auto">
    <div class="mb-3">
        <label class="form-label" for="login"><fmt:message key="login"/></label>
        <input type="text" class="form-control" name="login" id="login"/>
    </div>
    <div class="mb-3">
        <label for="password" class="form-label"><fmt:message key="password"/></label>
        <input type="password" name="password" class="form-control" id="password"/>
    </div>
    <fmt:message key="submit" var="submit"/>
    <input type="submit" value="${submit}" class="btn btn-primary text-white"/>
    <c:if test="${!(sessionScope.get('error/login.jsp') == null)}">
        <span class="alert alert-danger my-3" role="alert"><fmt:message key="${sessionScope.get('error/login.jsp')}"/></span>
    </c:if>
</form>
</body>
</html>
