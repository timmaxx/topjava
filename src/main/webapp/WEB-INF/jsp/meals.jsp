<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://topjava.javawebinar.ru/functions" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<html>
<jsp:include page="fragments/headTag.jsp"/>

<body>
<jsp:include page="fragments/bodyHeader.jsp"/>

<section>
    <hr/>
    <h2><spring:message code="meal.title"/></h2>

    <%-- Здесь и далее представлены несколько вариантов работы со ссылками --%>
    <%--<form method="get" action="${pageContext.request.contextPath}/meals/filter">--%>
    <%--<form method="get" action="<spring:url value="/meals/filter"/>">--%>
    <form method="get" action='<c:url value="/meals/filter"/>'>
    <%-- IDEA считает, что есть ошибка для всех вариантов <c:url ...> (но приложение работает).
         Не хватает какой-то интеграции?
         Конкретно на этих строках пишет:
            Error:(20, 47) Cannot resolve directory 'meals'
            Error:(20, 53) Cannot resolve file 'filter'
    --%>

        <dl>
            <dt><spring:message code="meal.startDate"/>:</dt>
            <dd><input type="date" name="startDate" value="${param.startDate}"></dd>
        </dl>
        <dl>
            <dt><spring:message code="meal.endDate"/>:</dt>
            <dd><input type="date" name="endDate" value="${param.endDate}"></dd>
        </dl>
        <dl>
            <dt><spring:message code="meal.startTime"/>:</dt>
            <dd><input type="time" name="startTime" value="${param.startTime}"></dd>
        </dl>
        <dl>
            <dt><spring:message code="meal.endTime"/>:</dt>
            <dd><input type="time" name="endTime" value="${param.endTime}"></dd>
        </dl>
        <button type="submit"><spring:message code="common.filter"/></button>
    </form>
    <hr/>

    <%--<a href="${pageContext.request.contextPath}/meals/create"> --%>
    <%--<a href="<spring:url value="/meals/create"/>"> --%>
    <a href='<c:url value="/meals/create"/>'>

        <spring:message code="meal.add"/></a>

    <br><br>
    <table border="1" cellpadding="8" cellspacing="0">
        <thead>
        <tr>
            <th><spring:message code="meal.date"/></th>
            <th><spring:message code="meal.description"/></th>
            <th><spring:message code="meal.calories"/></th>
            <th></th>
            <th></th>
        </tr>
        </thead>
        <c:forEach items="${requestScope.meals}" var="meal">
            <jsp:useBean id="meal" type="ru.javawebinar.topjava.to.MealTo"/>
            <tr data-meal-excess="${meal.excess}">
                <td>
                        <%--${meal.dateTime.toLocalDate()} ${meal.dateTime.toLocalTime()}--%>
                        <%--<%=TimeUtil.toString(meal.getDateTime())%>--%>
                        <%--${fn:replace(meal.dateTime, 'T', ' ')}--%>
                        ${fn:formatDateTime(meal.dateTime)}
                </td>
                <td>${meal.description}</td>
                <td>${meal.calories}</td>

                <%--<td><a href="${pageContext.request.contextPath}/meals/update/${meal.id}"><spring:message code="common.update"/></a></td>--%>
                <%--<td><a href="<spring:url value="/meals/update/${meal.id}"/>"><spring:message code="common.update"/></a></td>--%>
                <td><a href='<c:url value="/meals/update/${meal.id}"/>'><spring:message code="common.update"/></a></td>

                <%--<td><a href="${pageContext.request.contextPath}/meals/delete/${meal.id}"><spring:message code="common.delete"/></a></td>--%>
                <%--<td><a href="<spring:url value="/meals/delete/${meal.id}"/>"><spring:message code="common.delete"/></a></td>--%>
                <td><a href='<c:url value="/meals/delete/${meal.id}"/>'><spring:message code="common.delete"/></a></td>
            </tr>
        </c:forEach>
    </table>
</section>

<jsp:include page="fragments/footer.jsp"/>
</body>
</html>
