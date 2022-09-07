%{--
<%  def userService = grailsApplication.mainContext.getBean("utilizadorService") %>
--}%
<%@ page import="lua.security.RoleGroup" %>
<!DOCTYPE html>
<html>

<head>
	<meta name="layout" content="main" />
	<g:set var="entityName" value="${message(code: 'roleGroup.label', default: 'RoleGroup')}" />
	<title><g:message code="default.index.label" args="[entityName]" /></title>
	<z:head/>
</head>

<body>

<sec:ifNotGranted roles="role_group">
	<g:render template="/layouts/acessoNegado"/>
</sec:ifNotGranted>
<sec:access expression="hasRole('role_group')">
	<section id="index-aluno" class="first">

	<z:body/>
	</section>
</sec:access>
</body>

</html>
