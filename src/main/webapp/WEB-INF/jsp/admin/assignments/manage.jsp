<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<jsp:include page="../../header.jsp" />

<%@ include file="assignmentFormDatePicker.jsp" %>
 
<h1>Endre oppgave i ${fn:toUpperCase(courseName)}</h1>

<c:if test="${status != null}">
<p>${status}</p>
</c:if>

<form:form method="POST">
<%@ include file="assignmentForm.jsp" %>
<form:checkbox path="delete"
label="Slett denne oppgaven (ingen angrefunksjon! <em>Tilhørende leveringer vil også forsvinne!</em>)" htmlEscape="false" /><br />
<input type="submit" value="Endre" onclick="return !$('#delete1:checked').val() || confirm('Vil du virkelig slette denne oppgaven og alle innleveringer forbundet med den?');"/>
</form:form>

<%--
<h1>Legge inn innlevering på vegne av student</h1>

<form:form method="POST" enctype="multipart/form-data">
    <p>
    Fil:
    <input type="file" name="file" /><br />
    </p>
    <p>
    Merknader (valgfritt):<br />
    <form:textarea path="notes" cols="50" rows="5" />
    </p>
    <input type="submit" value="Levere besvarelse" />
</form:form>
--%>

<jsp:include page="../../footer.jsp" />
