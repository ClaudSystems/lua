<%@ page import="lua.estoque.produto.Produto" %>
<!DOCTYPE html>
<html>

<head>
    <meta name="layout" content="main"/>
    <g:set var="entityName" value="${message(code: 'produto.label', default: 'Produto')}"/>
    <title><g:message code="default.show.label" args="[entityName]"/></title>
</head>

<body>

<section id="show-produto" class="first">

    <table class="table table-hover">
        <tbody>

        <tr class="prop">
            <td valign="top" class="name"><g:message code="produto.codigo.label" default="Codigo"/></td>

            <td valign="top" class="value">${fieldValue(bean: produtoInstance, field: "codigo")}</td>

        </tr>

        <tr class="prop">
            <td valign="top" class="name"><g:message code="produto.nome.label" default="Nome"/></td>

            <td valign="top" class="value">${fieldValue(bean: produtoInstance, field: "nome")}</td>

        </tr>

        <tr class="prop">
            <td valign="top" class="name"><g:message code="produto.descricao.label" default="Descricao"/></td>

            <td valign="top" class="value">${fieldValue(bean: produtoInstance, field: "descricao")}</td>

        </tr>

        <tr class="prop">
            <td valign="top" class="name"><g:message code="produto.itemsProduto.label" default="Items Produto"/></td>

            <td valign="top" style="text-align: left;" class="value">
                <ul>
                    <g:each in="${produtoInstance.itemsProduto}" var="i">
                        <li><g:link controller="itemProduto" action="show"
                                    id="${i.id}">${i?.encodeAsHTML()}</g:link></li>
                    </g:each>
                </ul>
            </td>

        </tr>

        <tr class="prop">
            <td valign="top" class="name"><g:message code="produto.estoques.label" default="Estoques"/></td>

            <td valign="top" style="text-align: left;" class="value">
                <ul>
                    <g:each in="${produtoInstance.estoques}" var="e">
                        <li><g:link controller="estoque" action="show" id="${e.id}">${e?.encodeAsHTML()}</g:link></li>
                    </g:each>
                </ul>
            </td>

        </tr>

        </tbody>
    </table>
</section>

</body>

</html>
