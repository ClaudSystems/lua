<!DOCTYPE html>
<html lang="en">

<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta name="viewport"
	content="width=device-width, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0, user-scalable=no">

<title>Lua</title>

<link rel="shortcut icon"
	href="${request.contextPath}/reason/img/favicon.png">

<meta name="description" content="">

<!-- CSS -->
<link href="${request.contextPath}/reason/css/preload.css"
	rel="stylesheet" media="screen">
<link href="${request.contextPath}/reason/css/bootstrap.css"
	rel="stylesheet" media="screen">
<link href="${request.contextPath}/reason/css/font-awesome.min.css"
	rel="stylesheet" media="screen">
<link href="${request.contextPath}/reason/css/animate.min.css"
	rel="stylesheet" media="screen">
<link href="${request.contextPath}/reason/css/slidebars.css"
	rel="stylesheet" media="screen">
<link href="${request.contextPath}/reason/css/lightbox.css"
	rel="stylesheet" media="screen">
<link href="${request.contextPath}/reason/css/jquery.bxslider.css"
	rel="stylesheet">
<link
	href="${request.contextPath}/reason/css/syntaxhighlighter/shCore.css"
	rel="stylesheet" media="screen">

<link href="${request.contextPath}/reason/css/style-green3.css"
	rel="stylesheet" media="screen" title="default">
<link href="${request.contextPath}/reason/css/width-full.css"
	rel="stylesheet" media="screen" title="default">

%{--<link href="/buttons.css" rel="stylesheet" media="screen">--}%

<!-- HTML5 shim and Respond.js IE8 support of HTML5 elements and media queries -->
<!--[if lt IE 9]>
        <script src="js/html5shiv.min.js"></script>
        <script src="js/respond.min.js"></script>
    <![endif]-->
</head>

<!-- Preloader -->
<div id="preloader">
	<div id="status">&nbsp;</div>
</div>

<body>


	<div id="theme-options" class="hidden-xs">

		<div id="icon-options">
			<i class="fa fa-gears fa-2x fa-flip-horizontal"></i>
		</div>
	</div>
	<div id="sb-site">
		<div class="boxed">

			<header id="header-full-top" class="hidden-xs header-full-dark">
				<div class="container">
					<div class="header-full-title">
						<h1 class="animated fadeInRight" >
							<a href="/lua"  class="active">Lua<span>Nova</span></a>
						</h1>
						<p class="animated fadeInRight">Gestor do seu Negócio</p>


					</div>

					<nav class="top-nav">

						<div>
							<iframe
								src="http://www.facebook.com/plugins/like.php?href=http%3A%2F%2Fwww.mozambiquetranslators.com&amp;layout=button_count&amp;show_faces=true&amp;action=like&amp;colorscheme=light&amp"
								style="overflow: hidden; width: 100%; height: 80px;"
								scrolling="no" frameborder="0" allowTransparency="true">
								<a href="http://www.trivoo.net" class="fbook">www.trivoo.net</a>
							</iframe>
						</div>
						<ul class="top-nav-social hidden-sm">

							<li></li>

						</ul>


					</nav>
				</div>
				<!-- container -->
			</header>
			<!-- header-full -->
			<nav
				class="navbar navbar-static-top navbar-default navbar-header-full navbar-inverse"
				role="navigation" id="header">
				<div class="container">
					<!-- Brand and toggle get grouped for better mobile display -->
					<div class="navbar-header">
						<button type="button" class="navbar-toggle" data-toggle="collapse"
							data-target="#bs-example-navbar-collapse-1">
							<span class="sr-only">Toggle navigation</span> <i
								class="fa fa-bars"></i>
						</button>
						<a class="navbar-brand hidden-lg hidden-md hidden-sm active"
							href="index.html">Inove <span>IT</span></a>
					</div>
					<!-- navbar-header -->

					<!-- Collect the nav links, forms, and other content for toggling -->
					<div class="pull-right">
						<a href="javascript:void(0);"
							class="sb-icon-navbar sb-toggle-right"><i class="fa fa-bars"></i></a>
					</div>
					<div class="collapse navbar-collapse"
						id="bs-example-navbar-collapse-1">
						<ul class="nav navbar-nav">
							<li><a href="/it.inove" class="active">Home</a></li>


							<li class="dropdown">

							</li>
							<li><g:link controller="home" action="servicos" class="active">Serviços </g:link></li>
							<li><g:link controller="Contact" action="index">Contact Us </g:link></li>


							<li><a href="#about" class="active">About Us</a></li>



						</ul>
					</div>

					<!-- navbar-collapse -->
				</div>
				<!-- container -->
			</nav>


			<!-- carousel -->

			<section class="margin-bottom">
				<div class="container">
					<g:layoutBody />
				</div>
			</section>



			<div class="container">
				<section class="margin-bottom">

					<div class="row">
						<div id="about" class="col-md-6">
							<h2 class="no-margin-top">Quem Somos</h2>
							<p>A LuaNova é um sistema de gestão de compras, stock e vendas, baseado na plataforma web através da tecnologia java,
								ela foi desenvolvida para ajudar a o sector comercial, nos seus processos de compras e vendas de mercadorias e outros
								 serviços.
								Ela é apropriado para empreas que tem seus sucursais distantes, pois através da internet podemos aceder ao nosso sistema de
								quaquer parte do mundo, permitindo para a integração com servidores na nuvem.
							</p>
							<p class="titled-pane">Este sistema foi criado para preencher a lacuna dos sistemas tradocionais, que consume elevados recursos para integração via rede com sites distantes.</p>
							<p>
							</p>
							<h2 class="no-margin-top">Missão:</h2>
							<p>
								Ser o sistema de referência a nível nacional e internacional

							</p>

							<h2 class="no-margin-top">Valores:</h2>
							<p>
								Sistema baseado na ética, segurança e integridade  dos dados
							</p>
							<h2 class="no-margin-top">Visão:</h2>
							<p>Ser uma referência ao nível do País e no mercado Global, em sistemas ERPs, com impacto significativo no
							desenvolvimento do capital Financeiro e Humano.
							</p>


							<div class="fb-like" data-send="true" data-width="450"
								data-show-faces="true"></div>
						</div>

						<div class="col-md-6">
							<div class="panel-group" id="accordion">
								<div class="panel panel-default">
									<div class="panel-heading panel-heading-link">
										<a data-toggle="collapse" data-parent="#accordion"
											href="#collapseOne"> <i class="fa fa-lightbulb-o"></i>
											qualidade e a
											satisfação do cliente.
										</a>
									</div>
									<div id="collapseOne" class="panel-collapse collapse in">
										<div class="panel-body">
											<p>Nós trabalhamos com uma equipe talentosa e disciplinada</p>
											<p>
												com excelentes habilidades de comunicação e técnicas
												com pessoal altamente formada</p>
										</div>
									</div>
								</div>
								<div class="panel panel-default">
									<div class="panel-heading panel-heading-link">
										<a data-toggle="collapse" data-parent="#accordion"
											href="#collapseTwo" class="collapsed"> <i
											class="fa fa-desktop"></i> comprometimento  e precisão
										</a>
									</div>
									<div id="collapseTwo" class="panel-collapse collapse">
										<div class="panel-body">
											<p>Nosso talento são o resultado de uma  aprendizagem contínua
											 com com alto nível de responsalibidade!</p>
											<p>
												A nossa gama de clientes é o resultado de nosso compromisso com a
												servir melhor.</p>
										</div>
									</div>
								</div>
								<div class="panel panel-default">
									<div class="panel-heading panel-heading-link">
										<a data-toggle="collapse" data-parent="#accordion"
											href="#collapseThree" class="collapsed"> <i
											class="fa fa-user"></i> Suporte com qualidade
										</a>
									</div>
									<div id="collapseThree" class="panel-collapse collapse">
										<div class="panel-body">
											<p>um dos nossos principais objetivos é oferecer cada vez melhor
											Serviços!</p>
											<p>Fornecer vários meios de comunicação eficiente
											com nossos clientes.</p>
										</div>
									</div>
								</div>
							</div>
						</div>


					</div>
				</section>

				%{--<section class="margin-bottom">
					<h2 class="section-title">Últimos Trabalhos</h2>
					<div class="bxslider-controls">
						<span id="bx-prev4"></span> <span id="bx-next4"></span>
					</div>
					<ul class="bxslider" id="latest-works">
						<li>
							<div class="img-caption-ar">
								<img src="${request.contextPath}/reason/img/last_works/img1.jpg"
									class="-responsive" alt="Image">
								<div class="caption-ar">
									<div class="caption-content">
									<!--  <a href="#" class="animated fadeInDown"><i
											class="fa fa-search"></i>More info</a>-->
										<h4 class="caption-title">Title Caption</h4>
									</div>
								</div>
							</div>
						</li>
						<li>
							<div class="img-caption-ar">
								<img src="${request.contextPath}/reason/img/last_works/img2.jpg"
									class="img-responsive" alt="Image">
								<div class="caption-ar">
									<div class="caption-content">
										<!--  <a href="#" class="animated fadeInDown"><i
											class="fa fa-search"></i>More info</a>-->
										<h4 class="caption-title">Title Caption</h4>
									</div>
								</div>
							</div>
						</li>
						<li>
							<div class="img-caption-ar">
								<img src="${request.contextPath}/reason/img/last_works/img3.jpg"
									class="img-responsive" alt="Image">
								<div class="caption-ar">
									<div class="caption-content">
										<!--  <a href="#" class="animated fadeInDown"><i
											class="fa fa-search"></i>More info</a>-->
										<h4 class="caption-title">Title Caption</h4>
									</div>
								</div>
							</div>
						</li>
						<li>
							<div class="img-caption-ar">
								<img src="${request.contextPath}/reason/img/last_works/img4.jpg"
									class="img-responsive" alt="Image">
								<div class="caption-ar">
									<div class="caption-content">
										<!--  <a href="#" class="animated fadeInDown"><i
											class="fa fa-search"></i>More info</a>-->
										<h4 class="caption-title">Title Caption</h4>
									</div>
								</div>
							</div>
						</li>
						<li>
							<div class="img-caption-ar">
								<img src="${request.contextPath}/reason/img/last_works/img5.jpg"
									class="img-responsive" alt="Image">
								<div class="caption-ar">
									<div class="caption-content">
										<!--  <a href="#" class="animated fadeInDown"><i
											class="fa fa-search"></i>More info</a>-->
										<h4 class="caption-title">Title Caption</h4>
									</div>
								</div>
							</div>
						</li>
						<li>
							<div class="img-caption-ar">
								<img src="${request.contextPath}/reason/img/last_works/img6.jpg"
									class="img-responsive" alt="Image">
								<div class="caption-ar">
									<div class="caption-content">
										<!--  <a href="#" class="animated fadeInDown"><i
											class="fa fa-search"></i>More info</a>-->
										<h4 class="caption-title">Title Caption</h4>
									</div>
								</div>
							</div>
						</li>
						
						
						<li>
							<div class="img-caption-ar">
								<img src="${request.contextPath}/reason/img/last_works/img7.jpg"
									class="img-responsive" alt="Image">
								<div class="caption-ar">
									<div class="caption-content">
										<!--  <a href="#" class="animated fadeInDown"><i
											class="fa fa-search"></i>More info</a>-->
										<h4 class="caption-title">Title Caption</h4>
									</div>
								</div>
							</div>
						</li>
						<li>
							<div class="img-caption-ar">
								<img src="${request.contextPath}/reason/img/last_works/img8.jpg"
									class="img-responsive" alt="Image">
								<div class="caption-ar">
									<div class="caption-content">
										<!--  <a href="#" class="animated fadeInDown"><i
											class="fa fa-search"></i>More info</a>-->
										<h4 class="caption-title">Title Caption</h4>
									</div>
								</div>
							</div>
						</li>
						<li>
							<div class="img-caption-ar">
								<img src="${request.contextPath}/reason/img/last_works/img9.jpg"
									class="img-responsive" alt="Image">
								<div class="caption-ar">
									<div class="caption-content">
										
										<h4 class="caption-title">Title Caption</h4>
									</div>
								</div>
							</div>
						</li>
						
					</ul>
				</section>--}%

				%{--<section>
					<p class="slogan text-center">
						Descubra os nossos trabalhos profissionais de<span> serviços de TI</span>. Nossos princípios são <span>Inovação</span>, <span>Comprometimento</span>,
						<span>Excelência</span> e <span>Conhecimento</span>. Nós somos uma equipa jovem resultado
						de empenho e  <span>dedicação</span>.
					</p>
					<h2 class="section-title">Nossos Clientes</h2>
					<p>São nossos clientes todas as instituições que acreditam que a inovação através das tecnologias
					de informação pode melhorar a sua produtividade e garantir uma crescente satisfação dos
					consumidores dos seus produtos e serviços.</p>
					<div class="col-md-8">

						<div class="col-md-3 col-sm-3 col-xs-6">
							<img src="${request.contextPath}/reason/img/demo/actionAid.jpg"
								alt="" class="img-responsive">
						</div>
						
						<div class="col-md-3 col-sm-3 col-xs-6">
							<img
								src="${request.contextPath}/reason/img/demo/bancoOportonidade.jpg"
								alt="" class="img-responsive">
						</div>
						<div class="col-md-3 col-sm-3 col-xs-6">
							<img src="${request.contextPath}/reason/img/demo/bim.jpg" alt=""
								class="img-responsive">
						</div>
						<div class="col-md-3 col-sm-3 col-xs-6">
							<img src="${request.contextPath}/reason/img/demo/jhpiego.jpg"
								alt="" class="img-responsive">
						</div>
						
						<div class="col-md-3 col-sm-3 col-xs-6">
							<img src="${request.contextPath}/reason/img/demo/uba.jpg" alt=""
								class="img-responsive">
						</div>
					</div>
					<div class="row">
						<div class="col-md-3 col-sm-3 col-xs-6">
							<img src="${request.contextPath}/reason/img/demo/cahorabassa.jpg"
								alt="" class="img-responsive">
						</div>
						
						
						<div class="col-md-3 col-sm-3 col-xs-6">
							<img
								src="${request.contextPath}/reason/img/demo/generalElectricInternational.jpg"
								alt="" class="img-responsive">
						</div>
						<div class="col-md-3 col-sm-3 col-xs-6">
							<img src="${request.contextPath}/reason/img/demo/merrilBrink.jpg"
								alt="" class="img-responsive">
						</div>
						<div class="col-md-3 col-sm-3 col-xs-6">
							<img
								src="${request.contextPath}/reason/img/demo/savethe children.jpg"
								alt="" class="img-responsive">
						</div>
					</div>
					</section>--}%
			</div>
			
		</div>
		

	</div>

	<aside id="footer-widgets">
		<div class="container">
			<div class="row">
				<div class="col-md-6">
					<h3 class="footer-widget-title">Sitemap</h3>
					<ul class="list-unstyled three_cols">
						<li><g:link controller="Home" action="index">
								<a class="active">Home</a>
							</g:link></li>
						<li><a href="blog.html">Services</a></li>
						<li><g:link controller="about">Acerca de nós</g:link></li>

					</ul>
					<h3 class="footer-widget-title">Subscrição</h3>

					<div class="input-group">
						<input type="text" class="form-control" placeholder="Email Adress">
						<span class="input-group-btn">
							<button class="btn btn-ar btn-primary" type="button">Subscreva</button>
						</span>
					</div>
					<!-- /input-group -->
				</div>

				<div class="col-md-4">
					<div class="footer-widget">
						<h3 class="footer-widget-title">Contactos</h3>
						<div class="row">
							<div class="foot-item-content address">
								<!-- Heading -->
								<h6 class="bold">
									<i class="fa fa-home"></i>&nbsp;&nbsp;InoveIT

								</h6>
								<!-- Paragraph -->
								<p class="add">Maputo - Mozambique</p>
								<p class="tel">
									<i class="fa fa-phone"></i> Tel : + (258) - 84 207 3024<br />
									<i class="fa fa-envelope"></i> Mail : <a href="#">technical@inove.it</a><br />
									<i class="fa fa-calendar"></i> Horas de serviço : 8:30 - 5:30
								</p>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
		<!-- row -->
		</aside>
		
	<div>
	<!-- footer-widgets -->
	<footer id="footer">
		<p>
			&copy; 2014 <a href="#">InoveIT </a>, Lda. Todos os direitos reservados
		</p>
	</footer>

	</div>
	<!-- boxed -->
	
	<!-- sb-site -->

	<div class="sb-slidebar sb-right">
		<div class="input-group">
			<input type="text" class="form-control" placeholder="Search...">
			<span class="input-group-btn">
				<button class="btn btn-default" type="button">
					<i class="fa fa-search"></i>
				</button>
			</span>
		</div>
		<!-- /input-group -->

		<h2 class="slidebar-header no-margin-bottom">Navegação</h2>
		<ul class="slidebar-menu">
			<li><a href="/it.inove/" class="active">Home</a></li>
			<li><g:link controller="home" action="servicos" class="active">Serviços</g:link></li>
			<li><g:link controller="about" action="index" >Contact Us</g:link></li>

			<li><a href="#about" class="active">Acerca de Nós</a></li>
		</ul>
<!-- 
		<h2 class="slidebar-header">Social Media</h2>

		<div class="slidebar-social-icons">

			<a href="#" class="social-icon-ar rss"><i class="fa fa-rss"></i></a>
			<a href="http://www.mozambiquetranslators.com"
				class="social-icon-ar facebook"><i class="fa fa-facebook"></i></a> <a
				href="#" class="social-icon-ar twitter"><i class="fa fa-twitter"></i></a>
			<a href="#" class="social-icon-ar pinterest"><i
				class="fa fa-pinterest"></i></a> <a href="#"
				class="social-icon-ar instagram"><i class="fa fa-instagram"></i></a>
			<a href="#" class="social-icon-ar wordpress"><i
				class="fa fa-wordpress"></i></a> <a href="#"
				class="social-icon-ar linkedin"><i class="fa fa-linkedin"></i></a> <a
				href="#" class="social-icon-ar flickr"><i class="fa fa-flickr"></i></a>
			<a href="#" class="social-icon-ar vine"><i class="fa fa-vine"></i></a>
			<a href="#" class="social-icon-ar dribbble"><i
				class="fa fa-dribbble"></i></a>
		</div>
	</div>
	 -->
	<!-- sb-slidebar sb-right -->
	<div id="back-top">
		<a href="#header"><i class="fa fa-chevron-up"></i></a>
	</div>
	<!-- Scripts -->
	<script src="${request.contextPath}/reason/js/jquery.min.js"></script>
	<script src="${request.contextPath}/reason/js/jquery.cookie.js"></script>
	<script src="${request.contextPath}/reason/js/bootstrap.min.js"></script>
	<script src="${request.contextPath}/reason/js/wow.min.js"></script>
	<script src="${request.contextPath}/reason/js/slidebars.js"></script>
	<script src="${request.contextPath}/reason/js/jquery.bxslider.min.js"></script>
	<script src="${request.contextPath}/reason/js/holder.js"></script>
	<script src="${request.contextPath}/reason/js/buttons.js"></script>
	<script src="${request.contextPath}/reason/js/styleswitcher.js"></script>
	<script src="${request.contextPath}/reason/js/jquery.mixitup.min.js"></script>
	<script src="${request.contextPath}/reason/js/circles.min.js"></script>

	<!-- Syntaxhighlighter -->
	<script
		src="${request.contextPath}/reason/js/syntaxhighlighter/shCore.js"></script>
	<script
		src="${request.contextPath}/reason/js/syntaxhighlighter/shBrushXml.js"></script>
	<script
		src="${request.contextPath}/reason/js/syntaxhighlighter/shBrushJScript.js"></script>

	<script src="${request.contextPath}/reason/js/app.js"></script>
	<script src="${request.contextPath}/reason/js/index.js"></script>
	<script src="${request.contextPath}/reason/js/facebook.js"></script>
</div>
</body>

</html>
