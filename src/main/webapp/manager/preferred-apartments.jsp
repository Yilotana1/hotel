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
    <table class="table table-striped table-bordered table-hover w-75">
        <tr>
            <th><fmt:message key="number"/></th>
            <th><fmt:message key="floor"/></th>
            <th><fmt:message key="status"/></th>
            <th><fmt:message key="class"/></th>
            <th><fmt:message key="number_of_people"/></th>
            <th><fmt:message key="price"/></th>
            <th><fmt:message key="demand"/></th>
        </tr>
        <c:forEach var="apartment" items="${requestScope.apartments}">
            <tr>
                <th>${apartment.number}</th>
                <th>${apartment.floor}</th>
                <th>${apartment.status}</th>
                <th>${apartment.apartmentClass}</th>
                <th>${apartment.numberOfPeople}</th>
                <fmt:message key="currency_koef" var="koef"/>
                <th>${apartment.price * koef} <fmt:message key="currency_sign"/></th>
                <th>${apartment.demand}</th>
                <th>
                    <form action="${pageContext.request.contextPath}/manager/apply-for-client" method="post">
                        <fmt:message key="choose" var="choose"/>
                        <input type="hidden" name="number" value="${apartment.number}">
                        <input type="hidden" name="login" value="${requestScope.login}">
                        <input type="hidden" name="stay_length" value="${requestScope.stay_length}">
                        <input type="submit" value="${choose}" class="btn btn-primary text-white">
                    </form>
                </th>
            </tr>
        </c:forEach>
    </table>
    <jsp:include page="../lists/pages.jsp"/>
</div>
</body>
</html>
