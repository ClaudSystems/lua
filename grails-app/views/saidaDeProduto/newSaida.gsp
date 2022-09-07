<%--
  Created by IntelliJ IDEA.
  User: Claudino
  Date: 31/03/2016
  Time: 22:38
--%>
<%  def userService = grailsApplication.mainContext.getBean("utilizadorService") %>
<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <meta name="layout" content="main"/>
   <z:head/>
</head>

<body>
<div class="panel panel-default">
    <div  style="background: #ffbcaf;font-size:16px "   class="navbar panel-heading">Nova Saida</div>
    <sec:ifNotGranted roles="saida_produto">
        <g:render template="/layouts/acessoNegado"/>
    </sec:ifNotGranted>
    <sec:access expression="hasRole('saida_produto')">
        <z:body/>
    </sec:access>
</div>
</body>
</html>