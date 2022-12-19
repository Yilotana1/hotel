<%--
  Created by IntelliJ IDEA.
  User: Anatoliy
  Date: 02.12.2022
  Time: 18:24
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<body>
<fmt:setLocale value="${sessionScope.lang.getLanguage()}"/>
<fmt:setBundle basename="message"/>
<table>
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
<br/>
<br/>
<jsp:include page="../pages.jsp"/>

</body>
</html>
