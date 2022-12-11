<%--
  Created by IntelliJ IDEA.
  User: Anatoliy
  Date: 02.12.2022
  Time: 16:15
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
<table align="right" class="locale_table">
    <tr>
        <th class="locale_table">
            <form>
                <input value="ua" type="submit"/>
                <input value="ua" type="hidden" name="lang"/>
                <input value="${param}" name="request_params" type="hidden"/>
            </form>
        </th>
        <th class="locale_table">
            <form>
                <input value="en" type="submit"/>
                <input value="en" type="hidden" name="lang"/>
                <input value="${param}" name="request_params" type="hidden"/>
            </form>
        </th>
    </tr>
</table>
</body>
</html>
