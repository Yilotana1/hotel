<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="com.example.hotel.model.entity.enums.UserStatus" %>
<%@ page import="com.example.hotel.model.entity.enums.Role" %>
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
        <th><fmt:message key="switch_to_manager"/></th>
    </tr>
    <c:forEach var="user" items="${requestScope.users}">
        <c:if test="${!sessionScope.login.equals(user.login)}">
            <form action="${pageContext.request.contextPath}/admin/edit-user" method="post">
                <tr>
                    <th>${user.login}</th>
                    <th>${user.firstname}</th>
                    <th>${user.lastname}</th>
                    <th>${user.email}</th>
                    <th>${user.phone}</th>
                    <th>
                        <div class="form-check form-switch">
                            <c:if test="${user.status == UserStatus.BLOCKED}">
                                <input type="checkbox" class="form-check-input" role="switch" name="blocked" value="yes"
                                       checked/>
                            </c:if>
                            <c:if test="${user.status == UserStatus.NON_BLOCKED}">
                                <input type="checkbox" class="form-check-input" role="switch" name="blocked"
                                       value="yes"/>
                            </c:if>
                                ${UserStatus.BLOCKED}
                        </div>
                    </th>
                    <th>
                        <div class="form-check form-switch">
                            <c:if test="${user.roles.contains(Role.MANAGER)}">
                                <input type="checkbox" class="form-check-input" role="switch" name="${Role.MANAGER}"
                                       value="yes" checked/>
                            </c:if>
                            <c:if test="${!user.roles.contains(Role.MANAGER)}">
                                <input type="checkbox" class="form-check-input" role="switch" name="${Role.MANAGER}"
                                       value="yes"/>
                            </c:if>
                                ${Role.MANAGER}
                            <c:forEach var="role" items="${user.roles}">
                                <c:if test="${role != Role.MANAGER}">
                                    <input type="hidden" name="${role}" value="yes">
                                </c:if>
                            </c:forEach>
                        </div>
                    </th>
                    <input type="hidden" value="${user.id}" name="id"/>
                    <fmt:message key="edit" var="edit"/>
                    <th><input type="submit" value="${edit}" class="btn btn-primary text-white"/></th>
                </tr>
            </form>
        </c:if>
    </c:forEach>
</table>
<jsp:include page="../pages.jsp"/>
</body>
</html>
