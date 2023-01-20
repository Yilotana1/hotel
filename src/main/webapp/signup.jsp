<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <title>Sign up</title>
    <style>
        <jsp:include page="styles/style.css"/>
    </style>
    <jsp:include page="parts/common.jsp"/>
</head>
<body>
<fmt:setLocale value="${sessionScope.lang.getLanguage()}"/>
<fmt:setBundle basename="message"/>
<jsp:include page="locale_buttons.jsp"/>
<form action="signup" method="post" class="my-5 w-25 d-flex flex-column align-items-center mx-auto">
    <table>
        <tr>
            <th><label for="login" class="form-label"><fmt:message key="login"/>:</label></th>
            <th><input type="text" name="login" id="login"/></th>
        </tr>
        <tr>
            <th><label for="firstname" class="form-label"><fmt:message key="firstname"/>:</label></th>
            <th><input type="text" name="firstname" id="firstname"/></th>
        </tr>
        <tr>
            <th><label for="lastname" class="form-label"><fmt:message key="lastname"/>:</label></th>
            <th><input type="text" name="lastname" id="lastname"/></th>
        </tr>
        <tr>
            <th><label for="email" class="form-label"><fmt:message key="email"/>:</label></th>
            <th><input type="email" name="email" id="email"/></th>
        </tr>
        <tr>
            <th><label for="phone" class="form-label"><fmt:message key="phone"/>:</label></th>
            <th><input type="text" name="phone" id="phone"/></th>
        </tr>
        <tr>
            <th><label for="password" class="form-label"><fmt:message key="password"/>:</label></th>
            <th><input type="password" name="password" id="password"/></th>
        </tr>
    </table>
    <fmt:message key="submit" var="submit"/>
    <input type="submit" value="${submit}" class="btn btn-primary text-white my-2"/>
    <c:if test="${!(sessionScope.get('error/signup.jsp') == null)}">
        <span class="alert alert-danger my-3" role="alert"><fmt:message
                key="${sessionScope.get('error/signup.jsp')}"/></span>
    </c:if>
</form>
</body>
</html>
