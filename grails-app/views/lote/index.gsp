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
    <title>Lua-Lote</title>
    <meta name="layout" content="main"/>
    <z:head/>
</head>

<body  >
<div  style="background: #ffbcaf;font-size:16px "  class="panel-heading">Lotes</div>

<sec:ifNotGranted roles="lote">
    <g:render template="/layouts/acessoNegado"/>
</sec:ifNotGranted>
<sec:access expression="hasRole('lote')">
    <z:body/>
</sec:access>

</body>
</html>