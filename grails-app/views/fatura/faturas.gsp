<%--
  Created by IntelliJ IDEA.
  User: Claudino
  Date: 12/09/2016
  Time: 15:39
--%>

<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <meta name="layout" content="main"/>
    <title>Faturas</title>
    <z:head/>
</head>

<body>
<div  style="background: #ffbcaf;font-size:16px "  class="panel-heading">Faturas
<g:link  action="newFatura"><button style="color: white;" class="btn btn-success">Nova Fatura</button></g:link>
</div>
<sec:ifNotGranted roles="fatura">
    <g:render template="/layouts/acessoNegado"/>
</sec:ifNotGranted>
<sec:access expression="hasRole('fatura')">
    <z:body/>
</sec:access>
</body>
</html>