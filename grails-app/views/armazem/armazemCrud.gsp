<%--
  Created by IntelliJ IDEA.
  User: Claudino
  Date: 18/01/2016
  Time: 12:49
--%>

<%@ page contentType="text/html;charset=UTF-8" %>
%{--<%  def userService = grailsApplication.mainContext.getBean("utilizadorService") %>--}%
<html>
<head>
    <title>Lua-Armazém</title>
    <meta name="layout" content="main"/>
    <z:head/>
</head>

<body>
<h4 class="section-title no-margin-top">Armazens</h4>
<sec:ifNotGranted roles="armazem">
    <g:render template="/layouts/acessoNegado"/>
</sec:ifNotGranted>
<sec:access expression="hasRole('armazem')">
    <z:body/>
</sec:access>





</body>
</html>