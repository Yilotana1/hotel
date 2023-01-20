<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <title>Title</title>
</head>
<body>
<nav aria-label="..." class="mx-3">
    <ul class="pagination">
        <c:forEach begin="1" end="${requestScope.count}" step="1" var="i">
            <form action="${requestScope.path}">
                <c:if test="${requestScope.page.equals(i)}">
                    <li class="page-item active">
                        <input type="submit" value="${i}" class="btn btn-primary text-white"/>
                    </li>
                </c:if>
                <c:if test="${!requestScope.page.equals(i)}">
                    <li class="page-item">
                        <input type="submit" value="${i}" class="page-link"/>
                    </li>
                </c:if>
                <input type="hidden" name="page" value="${i}"/>
            </form>
        </c:forEach>
    </ul>
</nav>
</body>
</html>
