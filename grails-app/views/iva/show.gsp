
<%@ page import="lua.estoque.iva.Iva" %>
<!DOCTYPE html>
<html>

<head>
	<meta name="layout" content="main" />
	<g:set var="entityName" value="${message(code: 'iva.label', default: 'Iva')}" />
	<title><g:message code="default.show.label" args="[entityName]" /></title>
</head>

<body>
<div class="paper-back-full">
	<section id="show-iva" class="first">
		<h4 class=" section-title no-margin-top "><g:message code="default.show.label" args="[entityName]" /></h4>
		<table class="table table-hover">
			<tbody>
			
				<tr class="prop">
					<td valign="top" class="name"><g:message code="iva.percentualIva.label" default="Percentual Iva" /></td>
					
					<td valign="top" class="value">${fieldValue(bean: ivaInstance, field: "percentualIva")}</td>
					
				</tr>
			
				<tr class="prop">
					<td valign="top" class="name"><g:message code="iva.valido.label" default="Valido" /></td>
					
					<td valign="top" class="value"><g:formatBoolean boolean="${ivaInstance?.valido}" /></td>
					
				</tr>
			
				<tr class="prop">
					<td valign="top" class="name"><g:message code="iva.descricao.label" default="Descricao" /></td>
					
					<td valign="top" class="value">${fieldValue(bean: ivaInstance, field: "descricao")}</td>
					
				</tr>
			
			</tbody>
		</table>
	</section>
</div>
</body>

</html>
