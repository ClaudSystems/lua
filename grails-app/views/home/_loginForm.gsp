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
</head>

<body>
<g:form controller="home" action="login" role="form"  >

    <h4>Login</h4>

    <div class="form-group  col-lg-4 ">
        <div class="input-group login-input">
            <span class="input-group-addon"><i class="fa fa-user"></i></span>
            <input name="username" type="text" class="form-control"
                   placeholder="Username">
        </div>
        <br>
        <div class="input-group login-input">
            <span class="input-group-addon"><i class="fa fa-lock"></i></span>
            <input name="password" type="password" class="form-control"
                   placeholder="Password">
        </div>
        %{--<div class="checkbox  pull-left">
            <label> <input  type="checkbox"> Remember me
            </label>
        </div>--}%
        <button type="submit"
                class="btn btn-ar btn-primary pull-right">Login</button>
        <div class="clearfix"></div>
    </div>
</g:form>
</body>
</html>