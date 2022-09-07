<%--
  Created by IntelliJ IDEA.
  User: Claudino
  Date: 18/01/2016
  Time: 12:49
--%>

<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <title>Lua-Diario</title>
    <meta name="layout" content="main"/>
    <z:head/>
</head>

<body>
<h4 class="section-title no-margin-top">Diário De Vendas</h4>
<sec:ifNotGranted roles="diario">
    <g:render template="/layouts/acessoNegado"/>
</sec:ifNotGranted>
<sec:access expression="hasRole('diario')">
    <z:body/>
</sec:access>

</body>
</html>