<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: Anatoliy
  Date: 18.12.2022
  Time: 00:01
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
<fmt:setLocale value="${sessionScope.lang.getLanguage()}"/>
<fmt:setBundle basename="message"/>

<table>
    <tr>
        <th><fmt:message key="number"/></th>
        <th><fmt:message key="floor"/></th>
        <th><fmt:message key="busy"/></th>
        <th><fmt:message key="class"/></th>
        <th><fmt:message key="number_of_people"/></th>
        <th><fmt:message key="price"/></th>
        <th><fmt:message key="demand"/></th>
        <th><fmt:message key="resident_login"/></th>
    </tr>
    <c:forEach var="apartmentEntry" items="${requestScope.apartments.entrySet()}">
        <input type="hidden" name="number" value="${apartmentEntry.getKey().number}"/>
        <tr>
            <th>${apartmentEntry.getKey().number}</th>
            <th>${apartmentEntry.getKey().floor}</th>
            <th>${apartmentEntry.getKey().status}</th>
            <th>${apartmentEntry.getKey().apartmentClass}
            </th>
            <th>${apartmentEntry.getKey().numberOfPeople}</th>
            <fmt:message key="currency_koef" var="koef"/>
            <fmt:message key="currency_sign" var="sign"/>
            <th>${apartmentEntry.getKey().price * koef} ${sign}</th>
            <th>${apartmentEntry.getKey().demand}</th>
            <th>
                <c:if test="${apartmentEntry.getValue() != null}">
                    ${apartmentEntry.getValue()}
                </c:if>
                <c:if test="${apartmentEntry.getValue() == null}">
                    <fmt:message key="no_resident"/>
                </c:if>
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
