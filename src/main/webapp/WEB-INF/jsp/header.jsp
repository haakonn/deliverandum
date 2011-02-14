<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<% String abspath = getServletContext().getContextPath(); %>
<c:set var="absPath" value="${pageContext.request.contextPath}" />
<c:set var="requestUri" value="${pageContext.request.requestURI}" />
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>Innlevering i fjernundervisningen - ${requestUri} - ${user.admin}</title>
    <meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
    <link REL="SHORTCUT ICON" HREF="/favicon.ico">
    <link href="<%= abspath %>/css/main.css" rel="stylesheet" type="text/css" />
    <link rel="stylesheet" href="http://ajax.googleapis.com/ajax/libs/jqueryui/1.7/themes/ui-lightness/jquery-ui.css" type="text/css" media="all" />
  </head>
  <body>
  <script src="http://ajax.googleapis.com/ajax/libs/jquery/1.3/jquery.min.js" type="text/javascript"></script>

  <div class="container">
    <div class="header">
      <h1>Innlevering i fjernundervisningen</h1>
      <div class="it-header">
        <img src="${absPath}/graphics/uib_topp.gif" alt="Universitetet i Bergen - JAFU" />
        <a href="http://nettkurs.uib.no" title="JAFU nettkurs">JAFU</a>
      </div>
    </div>

    <!-- start Menu HTML -->
    <div class="left">
      <div id="lang">
      </div>
      <div id="menu">
        <ul>
          <li>
            <h2><span>Navigasjon</span></h2>
            <a href="/dpg" title="Kurs">Tilbake til kurssidene</a>
          </li>
          <c:if test="${user.admin && !fn:contains(requestUri, 'courseList')}">
          <li>
            <h2><span>Admin</span></h2>
            <a href="${pageContext.request.contextPath}/${courseName}/admin/users.html" title="Studenter">Studenter</a>
            <a href="${pageContext.request.contextPath}/${courseName}/admin/assignments/list.html" title="Oppgaveliste">Oppgaveliste</a>
            <a href="${pageContext.request.contextPath}/${courseName}/admin/assignments/new.html" title="Ny oppgave">Ny oppgave</a>
            <a href="${pageContext.request.contextPath}/${courseName}/admin/deliveries/my.html" title="Mine tildelte innleveringer">Mine tildelte innleveringer</a>
            <a href="${pageContext.request.contextPath}/${courseName}/admin/deliveries/new.html" title="Nye innleveringe">Nye innleveringer</a>
            <a href="${pageContext.request.contextPath}/${courseName}/admin/deliveries/all.html" title="Alle innleveringer">Alle innleveringer</a>
          </li>
          </c:if>
        </ul>
      </div>
    </div>
    <div class="content">
 
 