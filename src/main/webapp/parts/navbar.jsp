<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.example.hotel.model.entity.enums.Role" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
<fmt:setLocale value="${sessionScope.lang.getLanguage()}"/>
<fmt:setBundle basename="message"/>
<nav class="navbar navbar-expand-lg navbar-dark" style="background-color: #16437e">
    <div class="container-fluid">

        <a class="navbar-brand" href="${pageContext.request.contextPath}/main"><fmt:message key="main_title"/></a>

        <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarText"
                aria-controls="navbarText" aria-expanded="false" aria-label="Toggle navigation">
            <span class="navbar-toggler-icon"></span>
        </button>
        <div class="collapse navbar-collapse" id="navbarText">
            <ul class="navbar-nav me-auto mb-2 mb-lg-0">

                <li class="nav-item">
                    <a class="nav-link" href="${pageContext.request.contextPath}/main"><fmt:message key="main"/></a>
                </li>
                <c:if test="${sessionScope.roles != null}">
                    <li class="nav-item">
                        <a class="nav-link"
                           href="${pageContext.request.contextPath}/client/make-temporary-application.jsp">
                            <fmt:message key="specify_preferred"/>
                        </a>
                    </li>
                </c:if>

                <c:if test="${sessionScope.roles.contains(Role.ADMIN)}">
                    <li class="nav-item">
                        <a href="${pageContext.request.contextPath}/admin/manage-users" class="nav-link"><fmt:message
                                key="manage_users"/></a>
                    </li>
                    <li class="nav-item">
                        <a href="${pageContext.request.contextPath}/admin/show-apartment-management"
                           class="nav-link"><fmt:message
                                key="manage_apartments"/></a>
                    </li>
                </c:if>
                <c:if test="${sessionScope.roles.contains(Role.MANAGER) && !sessionScope.roles.contains(Role.ADMIN)}">
                    <li class="nav-item">
                        <a href="${pageContext.request.contextPath}/manager/list-users" class="nav-link"><fmt:message
                                key="list_users"/></a>
                    </li>
                    <li class="nav-item">
                        <a href="${pageContext.request.contextPath}/manager/show-apartments"
                           class="nav-link"><fmt:message key="list_apartments"/></a>
                    </li>
                </c:if>
                <c:if test="${sessionScope.roles.contains(Role.MANAGER)}">
                    <li class="nav-item">
                        <a href="${pageContext.request.contextPath}/manager/show-temporary-applications"
                           class="nav-link"><fmt:message
                                key="manage_applications"/></a>
                    </li>
                </c:if>


            </ul>
            <c:if test="${sessionScope.roles != null}">
                <form action="${pageContext.request.contextPath}/profile">
                    <fmt:message key="profile" var="profile"/>
                    <input type="submit" value="${profile}" class="btn btn-primary mx-3">
                </form>
                <form action="${pageContext.request.contextPath}/logout">
                    <fmt:message key="signout" var="signout"/>
                    <input type="submit" value="${signout}" class="btn btn-primary">
                </form>
            </c:if>
            <c:if test="${sessionScope.roles == null}">
                <form action="${pageContext.request.contextPath}/signup.jsp">
                    <fmt:message key="signup" var="signup"/>
                    <input type="submit" value="${signup}" class="btn btn-primary mx-3">
                </form>
                <form action="${pageContext.request.contextPath}/login.jsp">
                    <fmt:message key="signin" var="signin"/>
                    <input type="submit" value="${signin}" class="btn btn-primary">
                </form>
            </c:if>
        </div>
    </div>
</nav>
</body>
</html>
