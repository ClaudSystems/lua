<%--
  Created by IntelliJ IDEA.
  User: Claudino
  Date: 14/09/2016
  Time: 21:25
--%>
<%  def userService = grailsApplication.mainContext.getBean("utilizadorService") %>
<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <title>Lua-Lote</title>
    <meta name="layout" content="main"/>
</head>

<body>
<sec:ifNotGranted roles="relatorios">
    <g:render template="/layouts/acessoNegado"/>
</sec:ifNotGranted>
<sec:access expression="hasRole('relatorios')">
    <div class="panel panel-default">
     <div class="panel-heading">Relatório do IVA</div>

    <g:form action="imprimirIva" class="button-bar" role="form"  >
        <div class="panel-body">


        <div class="panel-body">
            <div class="col-lg-2">
                <div >
                    <label class="control-label"><g:message code="cliente.dataDeExpiracao.label" default="De" /><span class="required-indicator">*</span></label>
            </div>
        </div>
        <div class="col-lg-6">
            <calendar:datePicker  name="inicio" precision="day"  defaultValue="${new Date()}"  />
        </div>

        </div>
        <div class="panel-body">
            <div class="col-lg-2">
                <div >
                    <label class="control-label"><g:message code="cliente.dataDeExpiracao.label" default="A" /><span class="required-indicator">*</span></label>
                </div>
            </div>
            <div class="col-lg-6">
                <calendar:datePicker  name="fim" precision="day"  defaultValue="${new Date()+30}"  />
            </div>

        </div>
        <div class="panel-body">
            <div class="col-lg-2">
                <div >
                    <label class="control-label"><g:message code="cliente.dataDeExpiracao.label" default="Entensão" /><span class="required-indicator">*</span></label>
                </div>
            </div>
            <div class="col-lg-6">
                <g:select  name="ext" from="${["pdf","doc","docx","html","xls","xlsx","ppt"]}" />

            </div>

        </div>
        <div class="form-actions margin-top-medium">
            <g:submitButton name="create" class="btn btn-danger" value="Imprimir"/>
        </div>

    </g:form>
    </div>


</sec:access>

</body>
</html>