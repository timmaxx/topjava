<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<html>
<jsp:include page="fragments/headTag.jsp"/>

<body>
<jsp:include page="fragments/bodyHeader.jsp"/>

<section>
    <hr><jsp:useBean id="meal" type="ru.javawebinar.topjava.model.Meal" scope="request"/>
    <h2><spring:message code="meal.create" var="mealCreate"/><spring:message code="meal.edit" var="mealEdit"/>
        ${meal.id == null ? mealCreate : mealEdit}
    </h2>

    <%-- 1й вариант (работает) --%>
<%--
    <form method="post" action="${meal.id == null ? "../meals/create" : "../../meals/update/{meal.id}"}">
--%>

    <%-- 2й вариант (не работает) --%>
<%--
    <"../meals/create" var="pathForMealCreate"/><"../../meals/update/{meal.id}" var="pathForMealEdit"/>
    <form method="post" action="${meal.id == null ? pathForMealCreate : pathForMealEdit}">
--%>

    <%-- 3й вариант (работает) --%>
    <c:set var="pathForMealCreate" value="/meals/create"/>
    <c:set var="pathForMealEdit" value="/meals/update/${meal.id}"/>
    <form method="post" action=<c:url value="${meal.id == null ? pathForMealCreate : pathForMealEdit}"/> >

    <%-- 4й вариант (работает) --%>
<%--
    <form method="post" action="${pageContext.request.contextPath}${meal.id == null ?  "/meals/create" : "/meals/update/"}${meal.id}">
--%>
        <input type="hidden" name="id" value="${meal.id}">
        <dl>
            <dt><spring:message code="meal.dateTime"/>:</dt>
            <dd><input type="datetime-local" value="${meal.dateTime}" name="dateTime" required></dd>
        </dl>
        <dl>
            <dt><spring:message code="meal.description"/>:</dt>
            <dd><input type="text" value="${meal.description}" size=40 name="description" required></dd>
        </dl>
        <dl>
            <dt><spring:message code="meal.calories"/>:</dt>
            <dd><input type="number" value="${meal.calories}" name="calories" required></dd>
        </dl>
        <button type="submit"><spring:message code="common.save"/></button>
        <button onclick="window.history.back()" type="button"><spring:message code="common.cancel"/></button>
    </form>
</section>

<jsp:include page="fragments/footer.jsp"/>
</body>
</html>
