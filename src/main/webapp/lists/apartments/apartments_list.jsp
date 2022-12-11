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
<fmt:setLocale value="${sessionScope.lang.name()}"/>
<fmt:setBundle basename="message"/>

<jsp:include page="selection.jsp"/>
<table>
    <tr>
        <th><fmt:message key="floor"/></th>
        <th><fmt:message key="status"/></th>
        <th><fmt:message key="class"/></th>
        <th><fmt:message key="number_of_people"/></th>
        <th><fmt:message key="price"/></th>
        <th><fmt:message key="demand"/></th>
    </tr>
    <c:forEach var="apartment" items="${requestScope.apartments}">
        <tr>
            <th>${apartment.floor}</th>
            <th>${apartment.status}</th>
            <th>${apartment.apartmentClass}</th>
            <th>${apartment.numberOfPeople}</th>
            <fmt:message key="currency_koef" var="koef"/>
            <th>${apartment.price * koef} <fmt:message key="currency_sign"/></th>
            <th>${apartment.demand}</th>
            <th>
                <form action="${pageContext.request.contextPath}/client/show-application-page">
                    <fmt:message key="apply" var="apply"/>
                    <input type="hidden" name="number" value="${apartment.number}">
                    <input type="submit" value="${apply}">
                </form>
            </th>
        </tr>
    </c:forEach>
</table>
<br/>
<br/>
<br/>
<jsp:include page="../pages.jsp"/>

</body>
</html>
