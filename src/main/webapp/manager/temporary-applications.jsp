<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <title>Title</title>
    <style>
        <jsp:include page="../styles/style.css"/>
    </style>
    <jsp:include page="../parts/common.jsp"/>
</head>
<body>
<fmt:setLocale value="${sessionScope.lang.getLanguage()}"/>
<fmt:setBundle basename="message"/>
<jsp:include page="../locale_buttons.jsp"/>
<div class="mx-3 my-3">
    <form action="${pageContext.request.contextPath}/manager/show-temporary-applications">
        <fmt:message key="login"/>: <input type="text" name="login"/>
        <fmt:message key="search" var="search"/>
        <input type="submit" value="${search}" class="btn btn-primary text-white"/>
    </form>
    <form action="${pageContext.request.contextPath}/manager/show-temporary-applications">
        <fmt:message key="list_all" var="list_all"/>
        <input type="submit" value="${list_all}" class="btn btn-primary text-white"/>
    </form>
    <table class="table table-striped table-bordered table-hover w-75">
        <tr>
            <th><fmt:message key="client_login"/></th>
            <th><fmt:message key="class"/></th>
            <th><fmt:message key="number_of_people"/></th>
            <th><fmt:message key="stay_length"/></th>
        </tr>
        <c:forEach var="temporary_application" items="${requestScope.temporary_applications}">
            <tr>
                <th>${temporary_application.clientLogin}</th>
                <th>${temporary_application.apartmentClass}</th>
                <th>${temporary_application.numberOfPeople}</th>
                <th>${temporary_application.stayLength}</th>
                <th>
                    <form action="${pageContext.request.contextPath}/manager/show-preferred-apartments">
                        <input type="hidden" name="login" value="${temporary_application.clientLogin}"/>
                        <input type="hidden" name="stay_length" value="${temporary_application.stayLength}"/>
                        <fmt:message key="apartments" var="apartments"/>
                        <input type="submit" value="${apartments}" class="btn btn-primary text-white"/>
                    </form>
                </th>
            </tr>
        </c:forEach>
    </table>
</div>
</body>
</html>
