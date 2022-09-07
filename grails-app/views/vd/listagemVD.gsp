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
    <title>Lua-VDs</title>
    <meta name="layout" content="main"/>
    <z:head/>
</head>

<body>
<div  style="background: #ffbcaf;font-size:16px "  class="panel-heading">VDs
    <g:link  action="newVd"><button style="color: white;" class="btn btn-success">Nova VD</button></g:link>
</div>
<sec:ifNotGranted roles="vd">
    <g:render template="/layouts/acessoNegado"/>
</sec:ifNotGranted>
<sec:access expression="hasRole('vd')">
    <z:body/>
</sec:access>

</body>
</html>