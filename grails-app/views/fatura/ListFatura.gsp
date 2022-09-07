<%--
  Created by IntelliJ IDEA.
  User: Claudino
  Date: 12/09/2016
  Time: 06:44
--%>

<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <title>lua-fatura-list</title>
    <meta name="layout" content="main"/>
    <z:head/>
</head>

<body>
<div class="panel panel-default">
    <div  style="background: #ffbcaf;font-size:16px "  class="panel-heading">Faturas</div>

    <sec:ifNotGranted roles="fatura">
        <g:render template="/layouts/acessoNegado"/>
    </sec:ifNotGranted>
    <sec:access expression="hasRole('fatura')">
        <z:body/>
    </sec:access>
</div>

</body>
</html>