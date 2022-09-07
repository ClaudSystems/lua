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
    <title>Lua-POS</title>
    <meta name="layout" content="main"/>
    <z:head/>
</head>

<body>
<div  style="background: #ffbcaf;font-size:16px "  class="panel-heading">Categorias de Produtos</div>
<sec:ifNotGranted roles="categoria">
    <g:render template="/layouts/acessoNegado"/>
</sec:ifNotGranted>
<sec:access expression="hasRole('categoria')">
    <z:body/>
</sec:access>





</body>
</html>