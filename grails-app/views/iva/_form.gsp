<%@ page import="lua.estoque.iva.Iva" %>



 <div class="form-group">
   	<div class="col-lg-12">
		<div class=" col-lg-3 ${hasErrors(bean: ivaInstance, field: 'percentualIva', 'error')} required">
			<label for="percentualIva" class="col-lg-3"><g:message code="iva.percentualIva.label" default="Percentual Iva" /><span class="required-indicator">*</span></label>
		</div>
		<div class="col-lg-9">
			<g:field class="form-control" name="percentualIva" value="${fieldValue(bean: ivaInstance, field: 'percentualIva')}" required=""/>
			<span class="help-inline">${hasErrors(bean: ivaInstance, field: 'percentualIva', 'error')}</span>
		</div>
	</div>
 </div>

 <div class="form-group">
   	<div class="col-lg-12">
		<div class=" col-lg-3 ${hasErrors(bean: ivaInstance, field: 'valido', 'error')} ">
			<label for="valido" class="col-lg-3"><g:message code="iva.valido.label" default="Valido" /></label>
		</div>
		<div class="col-lg-9">
			<g:checkBox name="valido" value="${ivaInstance?.valido}" />
			<span class="help-inline">${hasErrors(bean: ivaInstance, field: 'valido', 'error')}</span>
		</div>
	</div>
 </div>

 <div class="form-group">
   	<div class="col-lg-12">
		<div class=" col-lg-3 ${hasErrors(bean: ivaInstance, field: 'descricao', 'error')} ">
			<label for="descricao" class="col-lg-3"><g:message code="iva.descricao.label" default="Descricao" /></label>
		</div>
		<div class="col-lg-9">
			<g:textField class="form-control" name="descricao" value="${ivaInstance?.descricao}"/>
			<span class="help-inline">${hasErrors(bean: ivaInstance, field: 'descricao', 'error')}</span>
		</div>
	</div>
 </div>

