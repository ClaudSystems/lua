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
    <title>Editar-Fatura</title>
    <meta name="layout" content="main"/>
    <z:head/>
</head>

<body>
<div class="panel panel-default">

    <h4 class="section-title no-margin-top">  <div  style="background: #ffbcaf;font-size:16px " class="panel-heading label-primary ">Fatura No.${id} <span class="col-lg-4 "></span></div></h4>
    <sec:ifNotGranted roles="fatura">
        <g:render template="/layouts/acessoNegado"/>
    </sec:ifNotGranted>
    <sec:access expression="hasRole('fatura')">
        <z:body/>
    </sec:access>
</div>

</body>
</html>