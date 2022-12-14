<%@ page contentType="text/html" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%--
  Есть вопрос по интернационализации:
  1. Я в Хроме установил плагин Locale Switcher. Для отображения пользователю он работает.
     Но когда я переключился на английскую локаль и пытаюсь через контектсное меню выбрать
     "Просмотр кода страницы", то показывается html, где всё прописано по русской локали.
     Это специфика этого плагина?
     Ответ: Locale Switcher по F5 и Shift+F5 делает запросы с разными заголовками.
     На просмотр кода делает с Content-Language: ru.
     Т.е. это его бага или фича.
--%>

<script type="text/javascript">
    const i18n = [];
    <c:forEach var="key" items='<%=new String[]{"common.deleted","common.saved","common.enabled","common.disabled","common.errorStatus","common.confirm", "common.i18nForDataTables"}%>'>
        i18n["${key}"] = "<spring:message code="${key}"/>";
    </c:forEach>

    i18n["addTitle"] = "<spring:message code="${param.addTitle}"/>";
    i18n["editTitle"] = "<spring:message code="${param.editTitle}"/>";
</script>