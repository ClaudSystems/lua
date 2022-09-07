<%=packageName%>
<!DOCTYPE html>
<html>

<head>
	<meta name="layout" content="main" />
	<g:set var="entityName" value="\${message(code: '${domainClass.propertyName}.label', default: '${className}')}" />
	<title><g:message code="default.create.label" args="[entityName]" /></title>
</head>

<body>
<div class="paper-back-full">
	<div class="login-form-full">
		<div class="transparent-div no-padding animated fadeInUp animation-delay-1" >
			<div class="tab-content">
				<h4 class=" section-title no-margin-top "><g:message code="default.create.label" args="[entityName]" /></h4>
				<section id="create-${domainClass.propertyName}" class="first">
					<g:hasErrors bean="\${${propertyName}}">
					<div class="alert alert-danger">
						<g:renderErrors bean="\${${propertyName}}" as="list" />
					</div>
					</g:hasErrors>
					<g:form action="save" class="form-horizontal" role="form" <%= multiPart ? ' enctype="multipart/form-data"' : '' %>>
						<g:render template="form"/>
						<div class="form-actions margin-top-medium">
							<g:submitButton name="create" class="btn btn-primary" value="\${message(code: 'default.button.create.label', default: 'Create')}" />
							<button class="btn" type="reset"><g:message code="default.button.reset.label" default="Reset" /></button>
						</div>
					</g:form>
				</section>
			</div>
		</div>
	</div>
</div>

</body>

</html>
