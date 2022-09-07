<nav class="navbar navbar-default" xmlns="">
    <div class="container-fluid">
        <!-- Brand and toggle get grouped for better mobile display -->

        <div class="navbar-header">
        <div class="collapse navbar-collapse"
             id="bs-example-navbar-collapse-1">

            <ul class="nav navbar-nav">
                <li class="dropdown">

                <li  id="vendas" ><g:link  controller="home" action="index">Vendas </g:link></li>
                <li id="contabi" ><g:link controller="home" action="contabilidade">Contabilidade</g:link></li>



               %{-- <li class="dropdown">
                    <a href="javascript:void(0);" class="dropdown-toggle" data-toggle="dropdown">Documentos</a>
                    <ul class="dropdown-menu dropdown-menu-left">
                        <li><g:link controller="cotacao" action="cotacaoCrud" target="_blank">Cotações</g:link> </li>


                        <li><g:link controller="fatura" action="index" target="_blank">Faturas</g:link> </li>
                        <li><g:link controller="fatura" action="index" target="_blank">Faturas-Recibo</g:link> </li>
                        <li><g:link controller="recibo" action="index" target="_blank">Recibos</g:link> </li>
                        <li role="presentation" class="divider"></li>
                        <li><g:link controller="ordemDeCompra" action="index" target="_blank">Ordem de Compra</g:link> </li>
                        <li><g:link controller="guiaDeRemessa" action="index" target="_blank">Guia de Remessa</g:link> </li>
                    </ul>
                </li>--}%
                <li><g:link controller="settings" action="index" class="active">Definições</g:link></li>
                <sec:access expression="hasRole('pos')">
                    <li><g:link controller="pos" action="listagemDeVds" class="active">POS Res.</g:link></li>
                </sec:access>

                <li><g:render template="/_menu/logout"/></li>

            </ul>
         </div>

        </div>

        <!-- Collect the nav links, forms, and other content for toggling -->

    </div><!-- /.container-fluid -->
</nav>

