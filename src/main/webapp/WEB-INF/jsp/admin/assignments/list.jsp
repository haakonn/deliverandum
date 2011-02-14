<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib prefix="joda" uri="http://www.joda.org/joda/time/tags" %>

<jsp:include page="../../header.jsp" />

<h1>Oppgaver i ${fn:toUpperCase(courseName)}</h1>
<c:if test="${status != null}">
<p>${status}</p>
</c:if>

<table width="100%" id="assignmentsTable" style="display:none">
    <tr>
        <th>Navn</th>
        <th>Åpner</th>
        <th>Frist</th>
    </tr>
<c:forEach var="assignment" items="${assignments}" varStatus="loopStatus">
    <tr class="${loopStatus.index % 2 == 0 ? 'evenRow' : 'oddRow'} linked" onclick="window.location='manage/${assignment.id}.html'">
        <td>${assignment.name}</td>
        <td><joda:format value="${assignment.beginTime}" pattern="d/M 'kl.' HH:00" /></td>
        <td><joda:format value="${assignment.endTime}" pattern="d/M 'kl.' HH:00" /></td>
    </tr>
</c:forEach>
</table>

<script type="text/javascript">
    jQuery(document.getElementById("assignmentsTable")).fadeIn("slow");
</script> 
<jsp:include page="../../footer.jsp" />

