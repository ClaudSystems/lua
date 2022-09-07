<%--
  Created by IntelliJ IDEA.
  User: Claudino
  Date: 12/09/2016
  Time: 06:44
--%>

<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <title>lua-vd-list</title>
    <meta name="layout" content="main"/>
    <z:head/>
</head>

<body>
<div class="panel panel-default">
    <div class="panel-heading">Vendas a Dinheiro</div>
    <sec:ifNotGranted roles="vd">
        <g:render template="/layouts/acessoNegado"/>
    </sec:ifNotGranted>
    <sec:access expression="hasRole('vd')">
        <z:body/>
    </sec:access>
</div>

</body>
</html>