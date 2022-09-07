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
    <title>Editar-vd</title>
    <meta name="layout" content="main"/>
    <z:head/>
</head>

<body>
<div  style="background: #ffbcaf;font-size:16px "  class="panel-heading">
    VD No.${id} <span  ><g:link  action="listagemVD"><button style="color: white;" class="btn btn-success">VDs</button></g:link></span><span class="-align-center"><g:link  action="newVd"><button style="color: white;" class="btn btn-success">Nova VD</button></g:link></span></div>

<sec:ifNotGranted roles="vd">
        <g:render template="/layouts/acessoNegado"/>
    </sec:ifNotGranted>
    <sec:access expression="hasRole('vd')">
        <z:body/>
    </sec:access>
</div>

</body>
</html>