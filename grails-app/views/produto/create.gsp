<%@ page import="lua.estoque.produto.Produto" %>
<!DOCTYPE html>
<html>

<head>
    <meta name="layout" content="kickstart"/>
    <g:set var="entityName" value="${message(code: 'produto.label', default: 'Produto')}"/>
    <title><g:message code="default.create.label" args="[entityName]"/></title>
</head>

<body>

<section id="create-produto" class="first">
    <g:hasErrors bean="${produtoInstance}">
        <div class="alert alert-danger">
            <g:renderErrors bean="${produtoInstance}" as="list"/>
        </div>
    </g:hasErrors>
    <g:form action="save" class="form-horizontal" role="form">
        <g:render template="form"/>
        <div class="form-actions margin-top-medium">
            <g:submitButton name="create" class="btn btn-primary"
                            value="${message(code: 'default.button.create.label', default: 'Create')}"/>
            <button class="btn" type="reset"><g:message code="default.button.reset.label" default="Reset"/></button>
        </div>
    </g:form>
</section>

</body>

</html>
