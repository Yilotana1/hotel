<%--
  Created by IntelliJ IDEA.
  User: Anatoliy
  Date: 02.12.2022
  Time: 23:10
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
<table>
    <tr>
        <c:forEach begin="1" end="${requestScope.count}" step="1" var="i">
            <th style="border: none">
                <form action="${requestScope.path}">
                    <c:if test="${requestScope.page.equals(i)}">
                        <input style="color: red" type="submit" value="${i}">
                    </c:if>
                    <c:if test="${!requestScope.page.equals(i)}">
                        <input type="submit" value="${i}">
                    </c:if>
                    <input type="hidden" name="page" value="${i}"/>
                </form>
            </th>
        </c:forEach>
    </tr>
</table>
</body>
</html>
