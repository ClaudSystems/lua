<%--
  Created by IntelliJ IDEA.
  User: Claudino
  Date: 3/24/2019
  Time: 9:24 PM
--%>

<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <title>Lua-Relatório-Estoque</title>
    <meta name="layout" content="main"/>
</head>

<body>
<sec:ifNotGranted roles="relatorios">
    <g:render template="/layouts/acessoNegado"/>
</sec:ifNotGranted>
<div class="row">
    <div class="col-lg-5"><g:render template="entradasDeProduto"/></div>
    <div class="col-lg-5"> <g:render template="saidasDeProduto"/></div>
</div>
<sec:access expression="hasRole('relatorios')">

</sec:access>
</body>
</html>