<%--
  Created by IntelliJ IDEA.
  User: Claudino
  Date: 28/12/2015
  Time: 15:39
--%>

<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <title>Lua-Home</title>
    <meta name="layout" content="main"/>
    <z:head/>
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

    <g:set var="produtoServiceInstance" bean="produtoService"/>
    <section class="margin-bottom">
        %{-- <p class="lead lead-lg text-center primary-color margin-bottom">Sistema Lua <strong>CSV</strong> Compras Stock e Vendas</p>--}%
       %{-- <div class="row">
            <div class="col-md-3">
                <div class="panel-group" id="accordion">
                    <div class="panel panel-default wow fadeInUp">
                        <div class="panel-heading panel-heading-link">
                            <a data-toggle="collapse" data-parent="#accordion" href="#collapseOne">
                                <i class="fa fa-lightbulb-o"></i><img
                                    src="${resource(dir: 'images', file: 'vendas.png')}"> Vendas
                            </a>

                        </div>

                        <div id="collapseOne" class="panel-collapse collapse in">
                            <div class="panel-body">
                                <g:link controller="cotacao" action="listCotacao" id="cotacao"><img
                                        src="${resource(dir: 'images', file: 'cotacao.png')}"/> <span>Cotações</span></g:link>

                            </div>

                            <div class="panel-body">

                                <g:link controller="fatura" action="listFatura"><img
                                        src="${resource(dir: 'images', file: 'invoice.png')}"> <span>Faturas</span></g:link>
                              --}%%{--  <br/>--}%%{--
                               --}%%{-- <g:link controller="fatura" action="newFatura">Nova Fatura</g:link>--}%%{--
                            </div>

                            <sec:access expression="hasRole('pos')">
                                <div class="panel-body">

                                <g:link controller="pos" action="pos"><img src="${resource(dir: 'images', file: 'pos.png')}"> <span>POS Restaurante</span></g:link>
                            </div>
                            </sec:access>
                            <div class="panel-body">
                                <g:link controller="vd" action="ListagemVD"><img src="${resource(dir: 'images', file: 'list32.png')}"> <span>VD´s</span></g:link>
                            </div>
                            <div class="panel-body">
                                <g:link controller="diario" action="diario"><img src="${resource(dir: 'images', file: 'open_diario.png')}"> <span>Diários</span></g:link>

                            </div>
                        </div>
                    </div>

                    <div class="panel panel-default wow fadeInUp">
                        <div class="panel-heading panel-heading-link">
                            <a data-toggle="collapse" data-parent="#accordion" href="#collapseTwo" class="collapsed">
                                <i class="fa fa-desktop"></i><img
                                    src="${resource(dir: 'images', file: 'estoque.png')}">  Stock
                            </a>
                        </div>

                        <div id="collapseTwo" class="panel-collapse collapse">
                            <div class="panel-body">

                              <div>  <g:link controller="entradaDeProduto" id="" action="list"><img    src="${resource(dir: 'images', file: 'buy-32.png')}"> <span>Entradas</span></g:link></div>

                            </div>

                            <div class="panel-body">

                                <g:link controller="saidaDeProduto" action="saidaList"><span><img
                                        src="${resource(dir: 'images', file: 'venda_32.png')}"> Saidas
                                </span></g:link>
                            </div>
                            <div class="panel-body">

                                <g:link controller="armazem" action="index"><span><img
                                        src="${resource(dir: 'images', file: 'armazem.png')}"> Armazens
                                </span></g:link>
                            </div>
                        </div>
                    </div>

                    <div class="panel panel-default wow fadeInUp">
                        <div class="panel-heading panel-heading-link">
                            <a data-toggle="collapse" data-parent="#accordion" href="#collapseThree" class="collapsed">
                                <i class="fa fa-user"></i> <img
                                    src="${resource(dir: 'images', file: 'compra.png')}">  Compras
                            </a>
                        </div>

                        <div id="collapseThree" class="panel-collapse collapse">
                            <div class="panel-body">
                                <g:link controller="home" action="index"><img
                                        src="${resource(dir: 'images', file: 'po.png')}"> Encomendas</g:link>
                            </div>
                                <div class="panel-body">
--}%%{--
                                <g:link controller="home"  action="index"><img    src="${resource(dir: 'images', file: 'list32.png')}"> <span>Entradas</span></g:link>
--}%%{--
                                </div>
                            </div>
                        </div>
                    </div>

                    <div class="panel panel-default wow fadeInUp">
                        <div class="panel-heading panel-heading-link">
                            <a data-toggle="collapse" data-parent="#accordion" href="#collapseFive" class="collapsed">
                                <i class="fa fa-user"></i><img
                                    src="${resource(dir: 'images', file: 'entety.png')}">  Entidades
                            </a>
                        </div>

                        <div id="collapseFive" class="panel-collapse collapse">
                            <div class="panel-body"><g:link controller="cliente" action="clientes"><img
                                    src="${resource(dir: 'images', file: 'client.png')}">  Clientes</g:link></div>

                            <div class="panel-body"><g:link controller="fornecedor" action="index"><img
                                    src="${resource(dir: 'images', file: 'fornecedr.png')}">  Fornecedores</g:link>
                            </div>

                        </div>
                    </div>
                </div>
            <div class="col-md-3">
                <div class="panel-group" id="accordion1">
                <div class="panel panel-default wow fadeInUp">
                    <div class="panel-heading panel-heading-link">
                        <a data-toggle="collapse" data-parent="#accordion1" href="#collapseFiv" class="collapsed">
                            <i class="fa fa-user"></i><img
                                src="${resource(dir: 'images', file: 'cadastro.png')}"> Cadastro
                        </a>
                    </div>

                    <div id="collapseFiv" class="panel-collapse collapse">
<sec:access expression="hasRole('destino')">
                        <div class="panel-body"><g:link controller="destino" action="index"><img
                                src="${resource(dir: 'images', file: 'dstin.png')}"> Destino</g:link></div>
</sec:access>
<sec:access expression="hasRole('produto')">
                        <div class="panel-body"><g:link controller="produto" action="index"><img
                                src="${resource(dir: 'images', file: 'produt.png')}"> Produtos</g:link></div>
</sec:access>
<sec:access expression="hasRole('unidade')">
                        <div class="panel-body"><g:link controller="unidade" action="index"><img
                                src="${resource(dir: 'images', file: 'embalagem.png')}"> Unidades De Embalagen</g:link>
                        </div>
</sec:access>
<sec:access expression="hasRole('modelo')">
                        <div class="panel-body"><g:link controller="modelo" action="index"><img
                                src="${resource(dir: 'images', file: 'model.png')}"> Modelos</g:link></div>
</sec:access>
<sec:access expression="hasRole('lote')">
                        <div class="panel-body"><g:link controller="lote" action="index"><img
                                src="${resource(dir: 'images', file: 'lote.png')}"> Lotes</g:link></div>
</sec:access>
<sec:access expression="hasRole('categoria')">
                        <div class="panel-body"><g:link controller="categoria" action="categoriaCrud"><img
                                src="${resource(dir: 'images', file: 'categoria32.png')}"> Categorias de Produtos</g:link></div>
</sec:access>
                        <sec:access expression="hasRole('mesa')">
                             <div class="panel-body"><g:link controller="mesa" action="mesaCrud"><img
                                src="${resource(dir: 'images', file: 'table_32.png')}">Mesas (Restaurante)</g:link></div>
                        </sec:access>
                        <sec:access expression="hasRole('classe')">
                             <div class="panel-body"><g:link controller="classe" action="classe"><img
                                src="${resource(dir: 'images', file: 'table_32.png')}">Classes </g:link></div>
                        </sec:access>
                    </div>
                </div>
                </div>
            </div>
            <div class="col-md-3">
                <div class="panel-group" id="accordion2">
                <div class="panel panel-default wow fadeInUp">
                    <div class="panel-heading panel-heading-link">
                        <a data-toggle="collapse" data-parent="#accordion2" href="#collapseSi">
                            <i class="fa fa-lightbulb-o"></i><span
                                class="glyphicon glyphicon-option-list"></span> Relatórios
                        </a>
                    </div>

                    <div id="collapseSi" class="panel-collapse collapse">
                        <div class="panel-body"><g:link controller="relatorios"
                                                        action="estoque">Estoque</g:link></div>
                        <div class="panel-body"><g:link controller="relatorios"
                                                        action="vendas">Vendas</g:link></div>


                        <div class="panel-body"><g:link controller="relatorios"
                                                        action="iva">IVA</g:link></div>
                    </div>
                </div>
                </div>
            </div>
            <div class="col-lg-3">


            </div>
</div>--}%
        <z:body/>
    </section>
%{--  <g:render template="/layout/footer"/>--}%


</body>
</html>