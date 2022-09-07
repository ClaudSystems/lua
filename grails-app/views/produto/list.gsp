<%@ page import="lua.estoque.produto.Produto" %>
<!DOCTYPE html>
<html>

<head>
    <meta name="layout" content="kickstart"/>
    <g:set var="entityName" value="${message(code: 'produto.label', default: 'Produto')}"/>
    <title><g:message code="default.list.label" args="[entityName]"/></title>
</head>

<body>

<section id="list-produto" class="first">
    <table class="table table-bordered margin-top-medium">
        <thead>
        <tr>

            <g:sortableColumn property="codigo" title="${message(code: 'produto.codigo.label', default: 'Codigo')}"/>

            <g:sortableColumn property="nome" title="${message(code: 'produto.nome.label', default: 'Nome')}"/>

            <g:sortableColumn property="descricao"
                              title="${message(code: 'produto.descricao.label', default: 'Descricao')}"/>

        </tr>
        </thead>
        <tbody>
        <g:each in="${produtoInstanceList}" status="i" var="produtoInstance">
            <tr class="${(i % 2) == 0 ? 'odd' : 'even'}">

                <td><g:link action="show"
                            id="${produtoInstance.id}">${fieldValue(bean: produtoInstance, field: "codigo")}</g:link></td>

                <td>${fieldValue(bean: produtoInstance, field: "nome")}</td>

                <td>${fieldValue(bean: produtoInstance, field: "descricao")}</td>

            </tr>
        </g:each>
        </tbody>
    </table>

    <div>
        <bs:paginate total="${produtoInstanceCount}"/>
    </div>
</section>

</body>

</html>
