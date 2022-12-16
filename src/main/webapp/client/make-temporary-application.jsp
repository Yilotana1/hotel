<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.example.hotel.model.entity.enums.ApartmentClass" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
<fmt:setLocale value="${sessionScope.lang.getLanguage()}"/>
<fmt:setBundle basename="message"/>
<jsp:include page="../locale_buttons.jsp"/>
<fieldset style="width: 30%">
    <form action="${pageContext.request.contextPath}/client/make-temporary-application">
        <table>
            <tr>
                <th><fmt:message key="stay_length"/>:</th>
                <th><input type="number" name="stay_length"/></th>
            </tr>
            <tr>
                <th><fmt:message key="number_of_people"/>:</th>
                <th><input type="number" name="number_of_people"/></th>
            </tr>
            <tr>
                <th><fmt:message key="class"/>:</th>
                <th><select name="apartment_class_id" id="apartment_class_id">
                    <option value="${ApartmentClass.STANDARD.id}">
                        Standard
                    </option>
                    <option value="${ApartmentClass.STUDIO.id}">
                        Studio
                    </option>
                    <option value="${ApartmentClass.SUITE.id}">
                        Suite
                    </option>
                    <option value="${ApartmentClass.SUPERIOR.id}">
                        Superior
                    </option>
                </select></th>
            </tr>
        </table>
        <br/>
        <fmt:message key="apply" var="apply"/>
        <input type="submit" value="${apply}"/>
    </form>
</fieldset>
<br/>
<c:if test="${!(requestScope.error == null)}">
    <span style="color:red"><fmt:message key="${requestScope.error}"/></span>
</c:if>
<br/>
<br/>
<br/>
<fmt:message key="main" var="main"/>
<form action="${pageContext.request.contextPath}/main">
    <input type="submit" value="${main}">
</form>
</body>
</html>
