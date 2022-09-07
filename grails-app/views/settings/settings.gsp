
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
<body>
<sec:ifNotGranted roles="settings">
    <g:render template="/layouts/acessoNegado"/>
</sec:ifNotGranted>
<sec:access expression="hasRole('settings')">
    <section class="margin-bottom">
        %{-- <p class="lead lead-lg text-center primary-color margin-bottom">Sistema Lua <strong>CSV</strong> Compras Stock e Vendas</p>--}%

        <div class="panel panel-default">
            <div class="panel-heading"></div>
                <div class="col-md-12">
                    <span  style="background: #ffbcaf;font-size:16px "  class="titled-pane">Saida de Produtos</span>
                    <div class="panel-group" id="accordion">
                        <div class="panel panel-default wow fadeInUp">
                            <div class="panel-heading panel-heading-link">
                                <z:body/>
                            </div>
                        </div>
                    </div>
                </div>


        </div>
    </section>
</sec:access>

</body>
</html>