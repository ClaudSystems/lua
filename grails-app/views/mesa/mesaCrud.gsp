<%--
  Created by IntelliJ IDEA.
  User: Claudino
  Date: 18/01/2016
  Time: 12:49
--%>
<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <title>Lua-Mesas</title>
    <meta name="layout" content="main"/>
    <z:head/>
</head>

<body>
<div  style="background: #ffbcaf;font-size:16px "  class="panel-heading">Mesas</div>
<sec:ifNotGranted roles="mesa">
    <g:render template="/layouts/acessoNegado"/>
</sec:ifNotGranted>
<sec:access expression="hasRole('mesa')">
    <z:body/>
</sec:access>

</body>
</html>