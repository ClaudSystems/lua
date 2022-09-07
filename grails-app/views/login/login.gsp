<%  def userService = grailsApplication.mainContext.getBean("utilizadorService") %>

<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <title>Cinodemica-Home</title>
    <meta name="layout" content="main"/>

</head>

<body>

<style>
body {
    background-color: #eaeaea;
}

.z-textbox {
    border-style: none;
    background: #FFF
}

.z-intbox {
    border-style: none;
    background: #FFF
}

.z-doublebox {
    border-style: none;
    background: #FFF
}

.z-listcell {
    border-style: none;
    background: #FFF
}

</style>




<div class="form-group">
    <div class="panel center left panel-body  form-control" style="background:#d8d8d8">
        <div class="row page-item">
            <div class="col-md-12">
                <div class="panel panel-footer">
                    <div class="panel-danger">
                        <h3>Login</h3>
                        <g:form  action="acessar" >
                            <div class="form-group">
                                <label for="exampleInputEmail1">Nome Do Utilizador</label>
                                <input type="text" class="form-control" name="username" aria-describedby="emailHelp" placeholder="introduza nome do utilizador">
                                <small id="emailHelp" class="form-text text-muted">Não partilhar o seu nome do utilizador com niguem.</small>
                            </div>
                            <div class="form-group">
                                <label for="exampleInputPassword1">Password/Senha</label>
                                <input type="password" class="form-control" name="password" placeholder="Password">
                            </div>
                            <div class="form-check">
                                <input type="checkbox" class="form-check-input" id="exampleCheck1">
                                <label class="form-check-label" for="exampleCheck1">Check me out</label>
                            </div>
                            <button type="submit" class="btn btn-primary">Submit</button>
                        </g:form>

                    </div>
                </div>
            </div>
        </div>
    </div>
</div>

</body>
</html>