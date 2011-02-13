<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib prefix="joda" uri="http://www.joda.org/joda/time/tags" %>
<jsp:include page="../../header.jsp" />

<h1>Evaluere oppgave i ${fn:toUpperCase(courseName)}</h1>

<c:if test="${status != null}">
<p>${status}</p>
</c:if>

<p>Levert av <a href="../user/${student.username}.html">${student.fullName}</a> &lt;${student.email}&gt;
    <joda:format value="${delivery.deliveredAt}" pattern="d/M 'kl.' H:m" /></p>

<p>Oppgave: <a href="../../assignments/edit/${delivery.assignment.id}.html">${delivery.assignment.name}</a></p>
<h2>Innleverte filer</h2>
<p>Bare den nyeste (øverste) skal vurderes! De eldre filene er tidligere forsøk og er bare tatt med for ordens skyld.</p>

<table width="100%">
    <tr>
        <th>Filavn</th>
        <th>Studentens merknader</th>
    </tr>
<c:forEach var="deliveredFile" items="${delivery.files}" varStatus="loopStatus">
    <tr class="${loopStatus.first ? 'importantRow ' : ''}${loopStatus.index % 2 == 0 ? 'evenRow' : 'oddRow'}">
        <td><a href="../../../file.html?name=${deliveredFile.file.name}&assignment=${delivery.assignment.id}&user=${student.username}">${deliveredFile.file.name}</a></td>
        <td>${deliveredFile.notes}</td>
    </tr>
</c:forEach>
</table>

<h2>Vurdering</h2>

<form:form method="POST" enctype="multipart/form-data">
    <p>
    Vurdering/karakter/bestått:
    <form:input path="grade"/>
    </p>

    <p>
    Begrunnelse for/kommentarer til vurderingen (sendes til studenten på epost når vurderingen merkes som avsluttet)<br />
    <form:textarea path="gradeComment" cols="50" rows="5" />
    </p>

    <p>
    Filvedlegg:
    <input type="file" name="gradingAttachment" />
    (Eksisterende: <a href="../../../attachment.html?assignment=${delivery.assignment.id}&user=${student.username}">${existingAttachment}</a>)<br />
    </p>
    
    <p>
    Gruppeleder som har ansvar for å vurdere denne oppgaven: 
    <form:select path="assignedTo">
        <form:option value="" label="IKKE FORDELT" />
        <form:options items="${admins}" itemValue="username" itemLabel="fullName" />
    </form:select>
    </p>
    
    <p>
    <form:checkbox path="closed" label="Vurderingen er avsluttet" />
    </p>
    <input type="submit" value="Lagre" />
</form:form>

<jsp:include page="../../footer.jsp" />
