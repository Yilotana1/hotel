<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<body>
<table align="right" class="locale_table mx-3 my-3">
    <tr>
        <th class="locale_table">
            <form>
                <c:if test="${sessionScope.lang.getLanguage().equals('ua')}">
                    <input value="ua" type="submit" class="btn btn-primary text-white"/>
                </c:if>
                <c:if test="${!sessionScope.lang.getLanguage().equals('ua')}">
                    <input value="ua" type="submit" class="btn btn-outline-primary"/>
                </c:if>
                <input value="ua" type="hidden" name="lang"/>
                <input value="${pageContext.request.requestURI}" type="hidden" name="page_uri">
                <input value="${param}" name="request_params" type="hidden"/>
            </form>
        </th>
        <th class="locale_table">
            <form>
                <c:if test="${sessionScope.lang.getLanguage().equals('en')}">
                    <input value="en" type="submit" class="btn btn-primary text-white"/>
                </c:if>
                <c:if test="${!sessionScope.lang.getLanguage().equals('en')}">
                    <input value="en" type="submit" class="btn btn-outline-primary"/>
                </c:if>
                <input value="en" type="hidden" name="lang"/>
                <input value="${pageContext.request.requestURI}" type="hidden" name="page_uri">
                <input value="${param}" name="request_params" type="hidden"/>
            </form>
        </th>
    </tr>
</table>
</body>
</html>
