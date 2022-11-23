<%@ page contentType="text/html" pageEncoding="UTF-8" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>

<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title><spring:message code="app.title"/></title>

    <%-- Здесь и далее представлены несколько вариантов работы со ссылками --%>
    <%--<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/style.css">--%>
    <%--<link rel="stylesheet" href="<spring:url value="/resources/css/style.css"/>">--%>
    <link rel="stylesheet" href='<c:url value="/resources/css/style.css"/>'>

</head>
