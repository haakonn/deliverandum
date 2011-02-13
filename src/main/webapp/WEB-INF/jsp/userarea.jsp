<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib prefix="joda" uri="http://www.joda.org/joda/time/tags" %>

<jsp:include page="header.jsp" />

<h1>Innlevering i ${fn:toUpperCase(courseName)}</h1>

<c:if test="${status != null}">
<p>${status}</p>
</c:if>

<c:if test="${error != null}">
<p>FEILMELDING: ${error}</p>
</c:if>

<h2>Oppgaver som kan leveres nå</h2>

<c:choose>
    <c:when test="${empty openAssignments}">
        <p>For øyeblikket kan ingen oppgaver leveres.</p>
        <p>Hvis du har spørsmål angående dette, vennligst ta kontakt med
        <a href="mailto:${courseAdminEmail}">${courseAdminEmail}</a>.</p>
    </c:when>
    <c:otherwise>
        <form:form method="POST" enctype="multipart/form-data">
            <p>
            Oppgave:
            <c:choose>
                <c:when test="${fn:length(openAssignments) == 1}">
                    <spring:nestedPath path="command">
                        <input type="hidden" name="assignmentId" value="${openAssignments[0].id}" />
                    </spring:nestedPath>
                    ${openAssignments[0].name}
                </c:when>
                <c:otherwise>
		            <form:select path="assignmentId">
		                <form:options items="${openAssignments}" itemValue="id" itemLabel="name" />
		            </form:select>
                </c:otherwise>
            </c:choose>
            </p>
            <p>
            Fil:
            <input type="file" name="file" /><br />
            </p>
            <p>
            Merknader (valgfritt):<br />
            <form:textarea path="notes" cols="50" rows="5" />
            </p>
            <input type="submit" value="Levere" />
        </form:form>
    </c:otherwise>
</c:choose>

<h2>Tidligere leverte oppgaver</h2>

<c:choose>
    <c:when test="${empty deliveries}">
        Ingen foreløpig.
    </c:when>
    <c:otherwise>
                <p>Her er en liste over oppgaver som du tidligere har levert. Hver rad kan trykkes på for å se mer informasjon.</p>
		<table width="100%">
		    <th>Oppgave</th>
		    <th>Fil</th>
		    <th>Tid</th>
		    <th>Status</th>
		    <th>Resultat</th>
		    <c:forEach var="delivery" items="${deliveries}" varStatus="loopStatus">
		          <tr class="${loopStatus.index % 2 == 0 ? 'evenRow' : 'oddRow'} linked" onclick="window.location='delivery/${delivery.id}.html'">
		              <td>${delivery.assignment.name}</td>
		              <td>
		                  <a
		                  href="file.html?name=${delivery.files[0].file.name}&assignment=${delivery.assignment.id}&user=${user.username}"
		                  title="${delivery.files[0].notes}">${delivery.files[0].file.name}</a>
                      </td>
		              <td><joda:format value="${delivery.deliveredAt}" pattern="dd/MM 'kl.' H:m" /></td>
		              <td>
                          <c:choose>
                              <c:when test="${delivery.state == 'RECEIVED' && delivery.assignedTo == null}">Mottatt</c:when>
                              <c:when test="${delivery.state == 'RECEIVED' && delivery.assignedTo != null}">Under vurdering</c:when>
                              <c:when test="${delivery.state == 'EVALUATED'}">Vurdert</c:when>
                          </c:choose>
		              </td>
		              <td>
		                  <c:choose>
		                      <c:when test="${empty delivery.grade}">Kommer</c:when>
		                      <c:otherwise><strong>${delivery.grade}</strong></c:otherwise> 
		                  </c:choose>
		              </td>
		          </tr>
            </c:forEach>
		</table>
    </c:otherwise>
</c:choose>

<jsp:include page="footer.jsp" />
