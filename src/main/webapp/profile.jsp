<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="custom" uri="custom" %>
<%@ page import="com.example.hotel.model.entity.enums.Role" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <title>Title</title>
    <jsp:include page="parts/common.jsp"/>
<body>
<fmt:setLocale value="${sessionScope.lang.getLanguage()}"/>
<fmt:setBundle basename="message"/>
<div class="mx-3">
    <jsp:include page="profile_header.jsp"/>
    <c:if test="${sessionScope.roles.contains(Role.CLIENT)}">
        <br/>
        <jsp:include page="update_money_account.jsp"/>
        <br/>
    </c:if>
</div>

</body>
</html>
