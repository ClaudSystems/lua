<%--
  Created by IntelliJ IDEA.
  User: Claudino
  Date: 28/12/2015
  Time: 17:43
--%>

<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <meta name="layout" content="main"/>
    <z:head/>
</head>

<body>
    <g:if test="${session.getAttribute("user").equals(null)}">
     <z:body/>
    </g:if>
        <g:else>
            Acesso com sucesso ao sistema!

        </g:else>

</body>
</html>