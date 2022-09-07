<%--
  Created by IntelliJ IDEA.
  User: Claudino
  Date: 12/09/2016
  Time: 15:39
--%>
<%  def userService = grailsApplication.mainContext.getBean("utilizadorService") %>
<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <meta name="layout" content="main"/>
    <z:head/>
    <g:set var="item" bean="composersService"/>
</head>

<body>
<div  style="background: #ffbcaf;font-size:16px "  class="panel-heading">Nova VD
    <g:link  action="listagemVD"><button style="color: white;" class="btn btn-success">VDs</button></g:link>
</div>

<sec:ifNotGranted roles="vd">
    <g:render template="/layouts/acessoNegado"/>
</sec:ifNotGranted>
<sec:access expression="hasRole('vd')">
    <z:body/>
</sec:access>


</body>
</html>