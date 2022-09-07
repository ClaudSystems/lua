<%--
  Created by IntelliJ IDEA.
  User: Claudino
  Date: 09/03/2016
  Time: 05:00
--%>

<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <meta name="layout" content="main"/>
    <z:head/>
</head>

<body>
<sec:ifNotGranted roles="fornecedor">
    <g:render template="/layouts/acessoNegado"/>
</sec:ifNotGranted>
<sec:access expression="hasRole('fornecedor')">
    <z:body/>
</sec:access>
</body>
</html>