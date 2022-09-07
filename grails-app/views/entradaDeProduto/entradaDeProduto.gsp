<%--
  Created by IntelliJ IDEA.
  User: Claudino
  Date: 13/09/2016
  Time: 09:52
--%>
<%  def userService = grailsApplication.mainContext.getBean("utilizadorService") %>
<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <title>lua-entrada de Produtos</title>
    <meta name="layout" content="main"/>
    <z:head/>
</head>

<body>

<div class="panel panel-default">
    <sec:ifNotGranted roles="entrada_produto">
        <g:render template="/layouts/acessoNegado"/>
    </sec:ifNotGranted>
    <sec:access expression="hasRole('entrada_produto')">
        <z:body/>
    </sec:access>
</div>

</body>
</html>