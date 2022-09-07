<%@ page import="lua.estoque.produto.Produto" %>



<div class="form-group">
    <div class="col-lg-2">
        <div class="${hasErrors(bean: produtoInstance, field: 'codigo', 'error')} ">
            <label for="codigo" class="control-label"><g:message code="produto.codigo.label" default="Codigo"/></label>
        </div>
    </div>

    <div class="col-lg-6">
        <g:textField class="form-control" name="codigo" value="${produtoInstance?.codigo}"/>
        <span class="help-inline">${hasErrors(bean: produtoInstance, field: 'codigo', 'error')}</span>
    </div>
</div>

<div class="form-group">
    <div class="col-lg-2">
        <div class="${hasErrors(bean: produtoInstance, field: 'nome', 'error')} ">
            <label for="nome" class="control-label"><g:message code="produto.nome.label" default="Nome"/></label>
        </div>
    </div>

    <div class="col-lg-6">
        <g:textField class="form-control" name="nome" value="${produtoInstance?.nome}"/>
        <span class="help-inline">${hasErrors(bean: produtoInstance, field: 'nome', 'error')}</span>
    </div>
</div>

<div class="form-group">
    <div class="col-lg-2">
        <div class="${hasErrors(bean: produtoInstance, field: 'descricao', 'error')} ">
            <label for="descricao" class="control-label"><g:message code="produto.descricao.label"
                                                                    default="Descricao"/></label>
        </div>
    </div>

    <div class="col-lg-6">
        <g:textField class="form-control" name="descricao" value="${produtoInstance?.descricao}"/>
        <span class="help-inline">${hasErrors(bean: produtoInstance, field: 'descricao', 'error')}</span>
    </div>
</div>

<div class="form-group">
    <div class="col-lg-2">
        <div class="${hasErrors(bean: produtoInstance, field: 'itemsProduto', 'error')} ">
            <label for="itemsProduto" class="control-label"><g:message code="produto.itemsProduto.label"
                                                                       default="Items Produto"/></label>
        </div>
    </div>

    <div class="col-lg-6">

        <ul class="one-to-many">
            <g:each in="${produtoInstance?.itemsProduto ?}" var="i">
                <li><g:link controller="itemProduto" action="show" id="${i.id}">${i?.encodeAsHTML()}</g:link></li>
            </g:each>
            <li class="add">
                <g:link controller="itemProduto" action="create"
                        params="['produto.id': produtoInstance?.id]">${message(code: 'default.add.label', args: [message(code: 'itemProduto.label', default: 'ItemProduto')])}</g:link>
            </li>
        </ul>

        <span class="help-inline">${hasErrors(bean: produtoInstance, field: 'itemsProduto', 'error')}</span>
    </div>
</div>

<div class="form-group">
    <div class="col-lg-2">
        <div class="${hasErrors(bean: produtoInstance, field: 'estoques', 'error')} ">
            <label for="estoques" class="control-label"><g:message code="produto.estoques.label"
                                                                   default="Estoques"/></label>
        </div>
    </div>

    <div class="col-lg-6">
        <g:select class="form-control" name="estoques" from="${lua.estoque.estoque.Estoque.list()}" multiple="multiple"
                  optionKey="id" size="5" value="${produtoInstance?.estoques*.id}" class="many-to-many"/>
        <span class="help-inline">${hasErrors(bean: produtoInstance, field: 'estoques', 'error')}</span>
    </div>
</div>

