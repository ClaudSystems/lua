<%--
  Created by IntelliJ IDEA.
  User: Claudino
  Date: 12/09/2016
  Time: 06:44
--%>
<%  def userService = grailsApplication.mainContext.getBean("utilizadorService") %>
<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <title>lua-cotacao</title>
    <meta name="layout" content="main"/>
    <z:head/>
</head>

<body>
<div class="panel panel-default">
    <div  style="background: #ffbcaf;font-size:16px "  class="panel-heading">Cotação</div>
    <sec:ifNotGranted roles="cotacao">
        <g:render template="/layouts/acessoNegado"/>
    </sec:ifNotGranted>
    <sec:access expression="hasRole('cotacao')">
        <z:body/>
    </sec:access>
</div>

</body>
</html>