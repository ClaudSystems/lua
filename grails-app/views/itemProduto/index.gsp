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
    <meta name="layout" content="main"/>
    <z:head/>
</head>

<body>
<h4 class="section-title no-margin-top">Produto</h4>

<sec:ifNotGranted roles="item_produto">
    <g:render template="/layouts/acessoNegado"/>
</sec:ifNotGranted>
<sec:access expression="hasRole('item_produto')">
    <z:body/>
</sec:access>

</body>
</html>