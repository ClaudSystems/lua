<%@ page import="lua.entidades.cliente.Cliente" %>



<div class="col-lg-6">
	<div class="control-group">
		<div class="fieldcontain ${hasErrors(bean: clienteInstance, field: 'codigo', 'error')} required">
			<div class="control-label">
				<label for="codigo">
					<g:message code="cliente.codigo.label" default="Codigo" />
					<span class="required-indicator">*</span>
				</label>
			</div>
			<div class="form-control">
				<g:textField name="codigo" required="" value="${clienteInstance?.codigo}"/>

			</div>
		</div>
	</div>
</div>

<div class="col-lg-6">
	<div class="control-group">
		<div class="fieldcontain ${hasErrors(bean: clienteInstance, field: 'nome', 'error')} required">
			<div class="control-label">
				<label for="nome">
					<g:message code="cliente.nome.label" default="Nome" />
					<span class="required-indicator">*</span>
				</label>
			</div>
			<div class="form-control">
				<g:textField name="nome" required="" value="${clienteInstance?.nome}"/>

			</div>
		</div>
	</div>
</div>

<div class="col-lg-6">
	<div class="control-group">
		<div class="fieldcontain ${hasErrors(bean: clienteInstance, field: 'nuit', 'error')} ">
			<div class="control-label">
				<label for="nuit">
					<g:message code="cliente.nuit.label" default="Nuit" />
					
				</label>
			</div>
			<div class="form-control">
				<g:textField name="nuit" value="${clienteInstance?.nuit}"/>

			</div>
		</div>
	</div>
</div>

<div class="col-lg-6">
	<div class="control-group">
		<div class="fieldcontain ${hasErrors(bean: clienteInstance, field: 'mail', 'error')} ">
			<div class="control-label">
				<label for="mail">
					<g:message code="cliente.mail.label" default="Mail" />
					
				</label>
			</div>
			<div class="form-control">
				<g:field type="email" name="mail" value="${clienteInstance?.mail}"/>

			</div>
		</div>
	</div>
</div>

<div class="col-lg-6">
	<div class="control-group">
		<div class="fieldcontain ${hasErrors(bean: clienteInstance, field: 'numTelefone', 'error')} ">
			<div class="control-label">
				<label for="numTelefone">
					<g:message code="cliente.numTelefone.label" default="Num Telefone" />
					
				</label>
			</div>
			<div class="form-control">
				<g:textField name="numTelefone" value="${clienteInstance?.numTelefone}"/>

			</div>
		</div>
	</div>
</div>

<div class="col-lg-6">
	<div class="control-group">
		<div class="fieldcontain ${hasErrors(bean: clienteInstance, field: 'endereco', 'error')} ">
			<div class="control-label">
				<label for="endereco">
					<g:message code="cliente.endereco.label" default="Endereco" />
					
				</label>
			</div>
			<div class="form-control">
				<g:textField name="endereco" value="${clienteInstance?.endereco}"/>

			</div>
		</div>
	</div>
</div>

