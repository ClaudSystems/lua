
<%--
  Created by IntelliJ IDEA.
  User: Claudino
  Date: 5/22/2018
  Time: 6:56 PM
--%>
<%  def userService = grailsApplication.mainContext.getBean("utilizadorService") %>
<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <title></title>
    <meta name="layout" content="main"/>
</head>
<sec:ifNotGranted roles="settings">
    <g:render template="/layouts/acessoNegado"/>
</sec:ifNotGranted>
<sec:access expression="hasRole('settings')">
    <section class="margin-bottom">
        %{-- <p class="lead lead-lg text-center primary-color margin-bottom">Sistema Lua <strong>CSV</strong> Compras Stock e Vendas</p>--}%
        <div class="row">

            <div class="col-md-3">
                <div class="panel-group" id="accordion">
                    <div class="panel panel-default wow fadeInUp">
                        <div class="panel-heading panel-heading-link">
                            <a data-toggle="collapse" data-parent="#accordion" href="#collapseOne">
                                <i class="fa fa-lightbulb-o"></i><span class="glyphicon glyphicon-circle-arrow-down"/> Utilizadores & Roles</a>
                        </div>
                        <div id="collapseOne" class="panel-collapse collapse in">
                            <div class="panel-body">
                                <g:link controller="utilizador" action="index"><img src="${resource(dir:'images', file:'userr.png')}"> Utilizadores</g:link>
                                <div class="panel-body">  <g:link controller="roleGroup" action="index"><img src="${resource(dir:'images', file:'role_.png')}"> Group Roles</g:link></div>
                            </div>
                            <div class="panel-body">
                                <g:link  controller="precos"  action="precos"  ><img src="${resource(dir:'images', file:'precos.png')}"> Preços</g:link>
                            </div>
                        </div>
                    </div>
                    <div class="panel panel-default wow fadeInUp">
                        <div class="panel-heading panel-heading-link">
                            <a data-toggle="collapse" data-parent="#accordion" href="#collapseTree" class="collapsed">
                                <i class="fa fa-desktop"></i><span class="glyphicon glyphicon-home"/> IVA
                            </a>
                        </div>
                        <div id="collapseTree" class="panel-collapse collapse">
                            <div class="panel-body">
                                <g:link   controller="iva"  action="index" ><span class="glyphicon glyphicon-list"/><span> Listagem</span></g:link>
                                <g:link   controller="iva"  action="create" ><span class="glyphicon glyphicon-new-window"/><span> ADD IVA</span></g:link>
                            </div>

                        </div>
                    </div>
                    <div class="panel panel-default wow fadeInUp">
                        <div class="panel-heading panel-heading-link">
                            <a data-toggle="collapse" data-parent="#accordion" href="#collapseTwo" class="collapsed">
                                <i class="fa fa-desktop"></i><span class="glyphicon glyphicon-home"/>  Stock
                            </a>
                        </div>
                        <div id="collapseTwo" class="panel-collapse collapse">
                           %{-- <div class="panel-body">
                                <g:link   controller="entradaDeProduto" id="entrada" action="list" ><span class="glyphicon glyphicon-log-in"/><span></span></g:link>
                            </div>--}%
                            <div class="panel-body">
                                <g:link   action="settings" ><span class="glyphicon glyphicon-log-out"/>Saida de Produtos<span></span></g:link>
                            </div>
                        </div>
                    </div>

                </div>
            </div>
            <div class="col-lg-4">
            </div>
        </div>
    </section>
</sec:access>






</body>
</html>