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
    <title>Lua-Produto Filter</title>
    <meta name="layout" content="main"/>
    <z:head/>
</head>

<body>
<g:set var="ps" bean="produtoService"/>
<h3><span class="label-info navbarcell1 ">${ps.tipoDeMovimento+" de produtos"}</span></h3>
<g:if test="${ps.tipoDeMovimento.equals("entrada")||ps.tipoDeMovimento.equals("saida")||ps.tipoDeMovimento.equals("cotacao")}">
    <g:if test="${userService.getAccess("produto",session.getAttribute("user").toString())}">
        <z:body/>
    </g:if>
    <g:else>
        <h2><span class="label-danger"><g:message  code="default.acessonegado" default="Reset" /></span></h2>
    </g:else>
</g:if>


</body>
</html>