<%--
  Created by IntelliJ IDEA.
  User: Anatoliy
  Date: 02.12.2022
  Time: 22:10
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
<fmt:setLocale value="${sessionScope.lang.getLanguage()}"/>
<fmt:setBundle basename="message"/>
<form action="${pageContext.request.contextPath}/main">
    <label for="sorted_by"></label>
    Sort by:
    <select name="sorted_by" id="sorted_by">
        <c:forEach var="sorted_by_item" items="${requestScope.sorted_by_list}">

            <c:if test="${sorted_by_item.equals(requestScope.sorted_by)}">
                <option value="${sorted_by_item}" selected="selected"><fmt:message key="${sorted_by_item}"/></option>
            </c:if>
            <c:if test="${!sorted_by_item.equals(requestScope.sorted_by)}">
                <option value="${sorted_by_item}"><fmt:message key="${sorted_by_item}"/></option>
            </c:if>
            <c:if test="${requestScope.sorted_by == null && sorted_by_item.equals(requestScope.default_sorting)}">
                <option value="${requestScope.default_sorting}" selected="selected"><fmt:message key="${requestScope.default_sorting}"/></option>
            </c:if>
        </c:forEach>
    </select>
    <fmt:message key="sort" var="sort"/>
    <input type="submit" value="${sort}">
</form>
</body>
</html>
