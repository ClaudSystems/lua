<%--
  Created by IntelliJ IDEA.
  User: Claudino
  Date: 18/01/2016
  Time: 12:49
--%>

<%@ page contentType="text/html;charset=UTF-8" %>
<%  def userService = grailsApplication.mainContext.getBean("utilizadorService") %>
<html>
<head>
    <title>Lua-POS</title>

<z:head/>
</head>

<body>

<sec:ifNotGranted roles="pos">
    <g:render template="/layouts/acessoNegado"/>
</sec:ifNotGranted>
<sec:access expression="hasRole('pos')">
    <section id="index-aluno" class="first">
<z:body/>

    </section>
</sec:access>




</body>
</html>