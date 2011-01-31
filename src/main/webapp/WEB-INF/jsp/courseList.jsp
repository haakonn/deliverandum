<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<jsp:include page="header.jsp" />

<h1>Velg kurs</h1>

<p>
Innlogget som
<c:choose>
    <c:when test="${user.admin}">administrator</c:when>
    <c:otherwise>bruker</c:otherwise>
</c:choose>
${user.fullName} (${user.username}).
</p>

<p>
<c:choose>
    <c:when test="${user.admin}">Som administrator har du tilgang til alle kursene.</c:when>
    <c:otherwise>Du er oppmeldt til flere kurs.</c:otherwise>
</c:choose>

Velg et av dem:</p>

<ul>
<c:forEach var="course" items="${courses}">
    <li><a href="${course}/index.html">${fn:toUpperCase(course)}</a></li>
</c:forEach>
</ul>

<jsp:include page="footer.jsp" />
