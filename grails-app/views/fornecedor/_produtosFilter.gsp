<%--
  Created by IntelliJ IDEA.
  User: Claudino
  Date: 09/03/2016
  Time: 05:00
--%>
<%  def userService = grailsApplication.mainContext.getBean("utilizadorService") %>
<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <meta name="layout" content="main"/>
    <z:head/>
</head>

<body>
<g:if test="${userService.getAccess("fornecedor",session.getAttribute("user").toString())}">
    <z:body/>
</g:if>
<g:else>
    <h2><span class="label-danger"><g:message  code="default.acessonegado" default="Reset" /></span></h2>
</g:else>
</body>
</html>