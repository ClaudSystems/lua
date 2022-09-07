<!DOCTYPE html>
<!--[if lt IE 7 ]> <html lang="en" class="no-js ie6"> <![endif]-->
<!--[if IE 7 ]>    <html lang="en" class="no-js ie7"> <![endif]-->
<!--[if IE 8 ]>    <html lang="en" class="no-js ie8"> <![endif]-->
<!--[if IE 9 ]>    <html lang="en" class="no-js ie9"> <![endif]-->
<!--[if (gt IE 9)|!(IE)]><!--> <html lang="en" class="no-js"><!--<![endif]-->
<%
	def userService = grailsApplication.mainContext.getBean("utilizadorService")
%>
<?xml version="1.0" encoding="UTF-8"?>
<?init class="org.zkoss.zkplus.databind.AnnotateDataBinderInit" ?>
<?variable-resolver class="org.zkoss.zkplus.spring.DelegatingVariableResolver"?>
<?page zscriptLanguage="GroovyGrails"?>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
		<title><g:layoutTitle default="Lua"/></title>
		<meta name="viewport" content="width=device-width, initial-scale=1.0">
		<title><g:layoutTitle default="Grails"/></title>
		<g:layoutHead/>
		<link href="${request.contextPath}/bootstrap/css/bootstrap.min.css" rel="stylesheet" media="screen">
		<link href="${request.contextPath}/bootstrap/css/bootstrap.css" rel="stylesheet" media="screen">
		<link href="${request.contextPath}/bootstrap/css/bootstrap-responsive.css" rel="stylesheet" media="screen">
		<link href="${request.contextPath}/bootstrap/css/bootstrap-responsive.min.css" rel="stylesheet" media="screen">
		<link href="${request.contextPath}/bootstrap/css/jquery.dataTables.min.css" rel="stylesheet" media="screen">

		<%-- Manual switch for the skin can be found in /view/_menu/_config.gsp --%>
		<r:require modules="jquery"/> <%-- jQuery is required for Bootstrap! --%>
		<r:require modules="bootstrap"/>
		<r:layoutResources />
		<calendar:resources lang="en" theme="tiger"/>
	</head>
	<body background="/images/bgimage.jpg" >
	<style>
	body {
		background-color: #eaeaea;
	}



	</style>

		<div style="background:#EAEAEA " >



			<g:layoutBody/>

		</div>



		<r:layoutResources />

		<div style="height:200px">

	</div>

	<link rel="stylesheet" href="${resource(dir: 'js', file: 'jquery-3.3.1.js')}" type="text/js">
	<link rel="stylesheet" href="${resource(dir: 'js', file: 'dataTables.min.js')}" type="text/js">
	</body>
</html>
