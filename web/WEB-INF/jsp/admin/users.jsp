<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<jsp:include page="../header.jsp" />

<h1>Studenter</h1>

<p>
Dette er en liste over alle studenter som har levert oppgave så langt.
Studenter som ikke har levert noe, er <em>ikke</em> med på denne listen.
</p>

<ul>
<c:forEach var="student" items="${students}">
    <li><a href="deliveries/user/${student.username}.html">${student.fullName}</a></li>
</c:forEach>
</ul>

<jsp:include page="../footer.jsp" />
