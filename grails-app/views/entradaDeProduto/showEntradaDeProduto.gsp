<%--
  Created by IntelliJ IDEA.
  User: Claudino
  Date: 13/09/2016
  Time: 11:10
--%>
<%  def userService = grailsApplication.mainContext.getBean("utilizadorService") %>
<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <title>lua-Mostrar Entrada de Pordutos</title>
    <meta name="layout" content="main"/>
    <z:head/>
</head>

<body>
<sec:ifNotGranted roles="entrada_produto">
    <g:render template="/layouts/acessoNegado"/>
</sec:ifNotGranted>
<sec:access expression="hasRole('entrada_produto')">
    <z:body/>
</sec:access>
</body>
</html>