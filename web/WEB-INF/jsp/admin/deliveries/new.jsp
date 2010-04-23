<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib prefix="joda" uri="http://www.joda.org/joda/time/tags" %>
<jsp:include page="../../header.jsp" />

<h1>Nye innleveringer i ${fn:toUpperCase(courseName)}</h1>

<p>
Dette er innleveringer som ikke har blitt tildelt en gruppeleder.
Gruppeleder bør tildeles så snart som mulig.
</p>

<c:forEach var="assignment" items="${assignmentMap}">
    <h2>${assignment.key.name}</h2>
    <table width="100%">
	    <tr>
	        <th>Student</th>
	        <th>Levert</th>
	    </tr>
    <c:forEach var="delivery" items="${assignment.value}" varStatus="loopStatus">
        <tr class="${loopStatus.index % 2 == 0 ? 'evenRow' : 'oddRow'} linked" onclick="window.location='view/${delivery.id}.html'">
	        <td>${delivery.deliveredBy.username}</td>
	        <td><joda:format value="${delivery.deliveredAt}" pattern="dd/MM 'kl.' HH:mm" /></td>
        </tr>
    </c:forEach>
    </table>
</c:forEach>

<jsp:include page="../../footer.jsp" />
