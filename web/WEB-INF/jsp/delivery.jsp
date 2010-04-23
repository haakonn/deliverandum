<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib prefix="joda" uri="http://www.joda.org/joda/time/tags" %>

<jsp:include page="header.jsp" />

<h1>Innlevering: ${delivery.assignment.name}</h1>

<p>Du leverte denne oppgaven <joda:format value="${delivery.deliveredAt}" pattern="dd/MM 'kl.' HH:mm" /></p>

<p><strong>Status:</strong>
	<c:choose>
	    <c:when test="${delivery.state == 'RECEIVED' && delivery.assignedTo == null}">Mottatt</c:when>
	    <c:when test="${delivery.state == 'RECEIVED' && delivery.assignedTo != null}">Under vurdering</c:when>
	    <c:when test="${delivery.state == 'EVALUATED'}">Vurdert</c:when>
	</c:choose>
</p>

<p><strong>Vurdering:</strong>
    <c:choose>
        <c:when test="${empty delivery.grade}">Kommer</c:when>
        <c:otherwise><strong>${delivery.grade}</strong></c:otherwise> 
    </c:choose>
</p>

<p><strong>Gruppelederens merknader:</strong></p>

<blockquote>
    <p>${delivery.gradeComment}</p>
</blockquote>

<jsp:include page="footer.jsp" />
