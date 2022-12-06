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
<%@ page import="com.example.hotel.model.entity.enums.UserStatus" %>
<%@ page import="com.example.hotel.model.entity.enums.Role" %>
<html>
<body>
<fmt:setLocale value="${sessionScope.lang.name()}"/>
<fmt:setBundle basename="message"/>
<table>
    <tr>
        <th><fmt:message key="login"/></th>
        <th><fmt:message key="firstname"/></th>
        <th><fmt:message key="lastname"/></th>
        <th><fmt:message key="email"/></th>
        <th><fmt:message key="phone"/></th>
        <th><fmt:message key="block"/></th>
        <th><fmt:message key="switch_to_manager"/></th>
    </tr>
    <c:forEach var="user" items="${requestScope.users}">
        <c:if test="${!sessionScope.login.equals(user.login)}">
            <form action="${pageContext.request.contextPath}/admin/edit-user">
                <tr>
                    <th>${user.login}</th>
                    <th>${user.firstname}</th>
                    <th>${user.lastname}</th>
                    <th>${user.email}</th>
                    <th>${user.phone}</th>
                    <th>
                        <c:if test="${user.status == UserStatus.BLOCKED}">
                            <input type="checkbox" name="blocked" value="yes" checked/>
                        </c:if>
                        <c:if test="${user.status == UserStatus.NON_BLOCKED}">
                            <input type="checkbox" name="blocked" value="yes"/>
                        </c:if>
                            ${UserStatus.BLOCKED}
                    </th>
                    <th>
                        <c:if test="${user.roles.contains(Role.MANAGER)}">
                            <input type="checkbox" name="${Role.MANAGER}" value="yes" checked/>
                        </c:if>
                        <c:if test="${!user.roles.contains(Role.MANAGER)}">
                            <input type="checkbox" name="${Role.MANAGER}" value="yes"/>
                        </c:if>
                            ${Role.MANAGER}
                        <c:forEach var="role" items="${user.roles}">
                            <c:if test="${role != Role.MANAGER}">
                                <input type="hidden" name="${role}" value="yes">
                            </c:if>
                        </c:forEach>
                    </th>
                    <input type="hidden" value="${user.id}" name="id"/>
                    <fmt:message key="edit" var="edit"/>
                    <th><input type="submit" value="${edit}"/></th>
                </tr>
            </form>
        </c:if>
    </c:forEach>
</table>
<br/>
<br/>
<br/>
<jsp:include page="../pages.jsp"/>

</body>
</html>
