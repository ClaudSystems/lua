
<%@ page import="lua.estoque.iva.Iva" %>
<!DOCTYPE html>
<html>

<head>
	<meta name="layout" content="main" />
	<g:set var="entityName" value="${message(code: 'iva.label', default: 'Iva')}" />
	<title><g:message code="default.list.label" args="[entityName]" /></title>
</head>

<body>
<div class="paper-back-full">
	<section id="list-iva" class="first">
		<h4 class=" section-title no-margin-top "><g:message code="default.list.label" args="[entityName]" /></h4>
		<table class="table table-bordered margin-top-medium">
			<thead>
				<tr>
				
					<g:sortableColumn property="percentualIva" title="${message(code: 'iva.percentualIva.label', default: 'Percentual Iva')}" />
				
					<g:sortableColumn property="valido" title="${message(code: 'iva.valido.label', default: 'Valido')}" />
				
					<g:sortableColumn property="descricao" title="${message(code: 'iva.descricao.label', default: 'Descricao')}" />
				
				</tr>
			</thead>
			<tbody>
			<g:each in="${ivaInstanceList}" status="i" var="ivaInstance">
				<tr class="${(i % 2) == 0 ? 'odd' : 'even'}">
				
					<td><g:link action="show" id="${ivaInstance.id}">${fieldValue(bean: ivaInstance, field: "percentualIva")}</g:link></td>
				
					<td><g:formatBoolean boolean="${ivaInstance.valido}" /></td>
				
					<td>${fieldValue(bean: ivaInstance, field: "descricao")}</td>
				
				</tr>
			</g:each>
			</tbody>
		</table>
		<div>
			<bs:paginate total="${ivaInstanceCount}" />
		</div>
	</section>
</div>
</body>

</html>
