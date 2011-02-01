<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib prefix="joda" uri="http://www.joda.org/joda/time/tags" %>
<jsp:include page="../../header.jsp" />

<h1>Alle innleveringer i ${fn:toUpperCase(courseName)}</h1>

<c:forEach var="assignment" items="${assignmentMap}">
    <h2>${assignment.key.name}</h2>
    <table width="100%">
	    <tr>
	        <th>Student</th>
	        <th>Levert</th>
	        <th>Tildelt gruppeleder</th>
	    </tr>
    <c:forEach var="delivery" items="${assignment.value}" varStatus="loopStatus">
        <tr class="${loopStatus.index % 2 == 0 ? 'evenRow' : 'oddRow'} linked" onclick="window.location='view/${delivery.id}.html'">
	        <td>${delivery.deliveredBy.fullName}</td>
	        <td><joda:format value="${delivery.deliveredAt}" pattern="d/M 'kl.' HH:mm" /></td>
	        <td>
	           <c:if test="${delivery.assignedTo != null}">
	               ${delivery.assignedTo.fullName}
	           </c:if>
	        </td>
        </tr>
    </c:forEach>
    </table>
</c:forEach>

<jsp:include page="../../footer.jsp" />