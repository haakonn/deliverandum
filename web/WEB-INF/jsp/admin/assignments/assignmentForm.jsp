<form:hidden path="id" />
<form:hidden path="courseName" />
<form:hidden path="beginDateFull" />
<form:hidden path="endDateFull" />
<form:label path="name">Navn på oppgaven (kort, f.eks. "Ukeøving 1"):</form:label>
<form:input path="name" maxlength="20" /><br />
<form:label path="beginDate">Åpnes fra:</form:label> <form:input path="beginDate" maxlength="10" size="10" readonly="true" />
<form:label path="beginHour">klokken</form:label>
<form:select path="beginHour">
<%@ include file="assignmentFormHours.jsp" %>
</form:select><br />
<form:label path="endDate">Frist:</form:label> <form:input path="endDate" maxlength="10" size="10" readonly="true" />
<form:label path="endHour">klokken</form:label>
<form:select path="endHour">
<%@ include file="assignmentFormHours.jsp" %>
</form:select>
<br />