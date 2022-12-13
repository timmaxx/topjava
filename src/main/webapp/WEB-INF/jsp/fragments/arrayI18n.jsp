<%@ page contentType="text/html" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%--
  Есть пара вопросов по интернационализации:
  1. В topjava.meals.js сделал безусловный перевод на русский.
     А как сделать более универсально? Также - через заполнение i18n[]?
  2. Я в Хроме установил плагин Locale Switcher. Для отображения пользователю он работает.
     Но когда я переключился на английскую локаль и пытаюсь через контектное меню выбрать
     "Просмотр кода страницы", то показывается html, где всё по русски.
     Это специфика этого плагина?
--%>

<script type="text/javascript">
  <c:forEach var="key" items='<%=new String[]{"common.deleted","common.saved","common.enabled","common.disabled","common.errorStatus","common.confirm"}%>'>
    i18n["${key}"] = "<spring:message code="${key}"/>";
  </c:forEach>
</script>