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
    <title>Lua-Modelos</title>
    <meta name="layout" content="main"/>
    <z:head/>
</head>

<body>

<sec:ifNotGranted roles="precos">
    <g:render template="/layouts/acessoNegado"/>
</sec:ifNotGranted>
<sec:access expression="hasRole('precos')">
    <z:body/>
</sec:access>

</body>
</html>