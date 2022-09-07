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

</head>

<body>
<span class="titled-pane">VDs</span>

<sec:ifNotGranted roles="vd">
    <g:render template="/layouts/acessoNegado"/>
</sec:ifNotGranted>
<sec:access expression="hasRole('vd')">

</sec:access>


</body>
</html>