<%--
  Created by IntelliJ IDEA.
  User: Claudino
  Date: 1/10/2019
  Time: 5:47 AM
--%>

<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <title></title>
    <meta name="layout" content="main" />
</head>

<body>
<div class="form-group">
    <div class="panel center left panel-body  form-control" style="background:#d8d8d8">
        <div class="row page-item">
            <div class="col-md-12">
                <div class="panel panel-footer">
                    <div class="panel-danger">
                        <h3>O seu username ou password estão incorrectos, por favor tente novamente!</h3>
                        <h1><g:link controller="login" action="login">Login</g:link></h1>
                    </div>
                </div>
            </div>
        </div>
    </div>
</body>
</html>