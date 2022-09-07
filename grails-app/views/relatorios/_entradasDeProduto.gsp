
    <div class="panel panel-default">
  <div class="panel-heading">Entrada de Produtos</div>

    <g:form action="imprimirEntradas" class="button-bar" role="form"  >
        <div class="panel-body">


        <div class="panel-body">
            <div class="col-lg-2">
                <div >
                    <label class="control-label"><g:message code="cliente.dataDeExpiracao.label" default="De" /><span class="required-indicator">*</span></label>
            </div>
        </div>
        <div class="col-lg-3">
            <calendar:datePicker  name="inicio_e" precision="day"  defaultValue="${new Date()-30}"  />
        </div>

        </div>
        <div class="panel-body">
            <div class="col-lg-2">
                <div >
                    <label class="control-label"><g:message code="cliente.dataDeExpiracao.label" default="A" /><span class="required-indicator">*</span></label>
                </div>
            </div>
            <div class="col-lg-3">
                <calendar:datePicker  name="fim_e" precision="day"  defaultValue="${new Date()+1}"  />
            </div>

        </div>
        <div class="panel-body">
            <div class="col-lg-2">
                <div >
                    <label class="control-label"><g:message code="cliente.dataDeExpiracao.label" default="Entensão" /><span class="required-indicator">*</span></label>
                </div>
            </div>
            <div class="col-lg-3">
                <g:select  name="ext" from="${["pdf","doc","docx","html","xls","xlsx","ppt"]}" />

            </div>

        </div>
        <div class="form-actions margin-top-medium">
            <g:submitButton name="create" class="btn btn-danger" value="Imprimir"/>
        </div>

    </g:form>
    </div>
    </div>
