
<%@ page import="lua.vendas.fatura.Fatura" %>
<!DOCTYPE html>
<html>

<head>
	<meta name="layout" content="main" />
	<g:set var="entityName" value="${message(code: 'fatura.label', default: 'Fatura')}" />
	<title><g:message code="default.index.label" args="[entityName]" /></title>
</head>

<body>
<div class="paper-back-full">
	<section id="index-fatura" class="first">
		<h4 class=" section-title no-margin-top "><g:message code="default.list.label" args="[entityName]" /></h4>
		<table class="table table-bordered margin-top-medium">
			<thead>
				<tr>
				
					<th><g:message code="fatura.cliente.label" default="Cliente" /></th>
				
					<g:sortableColumn property="valor" title="${message(code: 'fatura.valor.label', default: 'Valor')}" />
				
					<g:sortableColumn property="valorDoIva" title="${message(code: 'fatura.valorDoIva.label', default: 'Valor Do Iva')}" />
				
					<g:sortableColumn property="estado" title="${message(code: 'fatura.estado.label', default: 'Estado')}" />
				
					<g:sortableColumn property="condicoesDeVenda" title="${message(code: 'fatura.condicoesDeVenda.label', default: 'Condicoes De Venda')}" />
				
					<g:sortableColumn property="dataPrevistaDePagamento" title="${message(code: 'fatura.dataPrevistaDePagamento.label', default: 'Data Prevista De Pagamento')}" />
				
				</tr>
			</thead>
			<tbody>
			<g:each in="${faturas}" status="i" var="faturaInstance">
				<tr class="${(i % 2) == 0 ? 'odd' : 'even'}">
				
					<td><g:link action="show" id="${faturaInstance.id}">${fieldValue(bean: faturaInstance, field: "cliente")}</g:link></td>
				
					<td>${fieldValue(bean: faturaInstance, field: "valor")}</td>
				
					<td>${fieldValue(bean: faturaInstance, field: "valorDoIva")}</td>
				
					<td>${fieldValue(bean: faturaInstance, field: "estado")}</td>
				
					<td>${fieldValue(bean: faturaInstance, field: "condicoesDeVenda")}</td>
				
					<td><g:formatDate date="${faturaInstance.dataPrevistaDePagamento}" /></td>
				
				</tr>
			</g:each>
			</tbody>
		</table>

	</section>
</div>
</body>

</html>
