<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<body>
<fmt:setLocale value="${sessionScope.lang.getLanguage()}"/>
<fmt:setBundle basename="message"/>
<table class="table table-striped table-bordered table-hover w-75">
    <tr>
        <th><fmt:message key="login"/></th>
        <th><fmt:message key="firstname"/></th>
        <th><fmt:message key="lastname"/></th>
        <th><fmt:message key="email"/></th>
        <th><fmt:message key="phone"/></th>
        <th><fmt:message key="block"/></th>
    </tr>
    <c:forEach var="user" items="${requestScope.users}">
        <c:if test="${!sessionScope.login.equals(user.login)}">
                <tr>
                    <th>${user.login}</th>
                    <th>${user.firstname}</th>
                    <th>${user.lastname}</th>
                    <th>${user.email}</th>
                    <th>${user.phone}</th>
                    <th>${user.status}</th>
                </tr>
        </c:if>
    </c:forEach>
</table>
<br/>
<jsp:include page="../pages.jsp"/>
</body>
</html>
