<%--
  Created by IntelliJ IDEA.
  User: Claudino
  Date: 18/01/2016
  Time: 12:49
--%>
<%  def userService = grailsApplication.mainContext.getBean("utilizadorService") %>
<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <title>Lua-Utilizador</title>
    <meta name="layout" content="main"/>
    <z:head/>
</head>

<body>
<h4 class="section-title no-margin-top">Fornecedores</h4>
<g:if test="${userService.getAccess("utilizador",session.getAttribute("user").toString())}">
    <z:body/>
</g:if>
<g:else>
    <h2><span class="label-danger"><g:message  code="default.acessonegado" default="Reset" /></span></h2>
</g:else>

</body>
</html>