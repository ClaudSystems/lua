<%--
  Created by IntelliJ IDEA.
  User: Claudino
  Date: 12/09/2016
  Time: 15:39
--%>
%{--<%  def userService = grailsApplication.mainContext.getBean("utilizadorService") %>--}%
<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <meta name="layout" content="main"/>
    <title>Nova Fatura</title>
    <z:head/>
</head>

<body>
<div  style="background: #ffbcaf;font-size:16px "  class="panel-heading">Nova Fatura
    <g:link  action="faturas"><button style="color: white;" class="btn btn-success">Faturas</button></g:link>
</div>

<sec:ifNotGranted roles="fatura">
    <g:render template="/layouts/acessoNegado"/>
</sec:ifNotGranted>
<sec:access expression="hasRole('fatura')">
    <z:body/>
</sec:access>
</body>
</html>