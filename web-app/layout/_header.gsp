<header id="header-full-top" class="hidden-xs header-full-dark">
    <div class="container z-navbar-collapsed"  >
        <div class="header-full-title">
            <span class="animated fadeInRight">
                <g:link controller="home" action="index" class="active"><span class="titled-pane">LUA<span class="label-warning"></span> ${meta(name:'app.version')}</span></g:link>

            </span>

        </div>
        <g:if test="${session.getAttribute("user")}">
            Utilizador:  <span class="label label-info"> ${session.getAttribute("user").username}</span>
        </g:if>





        <nav class="top-nav">
          %{--  <ul class="top-nav-social hidden-sm">
                <li><a href="#"
                       class="animated fadeIn animation-delay-6 rss"><i
                            class="fa fa-rss"></i></a></li>
                <li><a href="#"
                       class="animated fadeIn animation-delay-7 twitter"><i
                            class="fa fa-twitter"></i></a></li>
                <li><a href="#"
                       class="animated fadeIn animation-delay-8 facebook"><i
                            class="fa fa-facebook"></i></a></li>
                <li><a href="#"
                       class="animated fadeIn animation-delay-9 googleplus"><i
                            class="fa fa-google-plus"></i></a></li>
                <li><a href="#"
                       class="animated fadeIn animation-delay-9 instagram"><i
                            class="fa fa-instagram"></i></a></li>
                <li><a href="#"
                       class="animated fadeIn animation-delay-8 vine"><i
                            class="fa fa-vine"></i></a></li>
                <li><a href="#"
                       class="animated fadeIn animation-delay-7 linkedin"><i
                            class="fa fa-linkedin"></i></a></li>
                <li><a href="#"
                       class="animated fadeIn animation-delay-6 flickr"><i
                            class="fa fa-flickr"></i></a></li>
            </ul>--}%

          %{--  <div class="dropdown animated fadeInDown animation-delay-11">
                <a href="#" class="dropdown-toggle" data-toggle="dropdown"><i
                        class="fa fa-user"></i> Login</a>
                <div
                        class="dropdown-menu dropdown-menu-right dropdown-login-box animated fadeInUp">


                </div>
            </div>--}%
            <!-- dropdown -->

            <div class="dropdown animated fadeInDown animation-delay-13">
                <a href="#" class="dropdown-toggle" data-toggle="dropdown"><i
                        class="fa fa-search"></i></a>
                <div
                        class="dropdown-menu dropdown-menu-right dropdown-search-box animated fadeInUp">
                    <form role="form">
                        <div class="input-group">
                            <input type="text" class="form-control"
                                   placeholder="Search..."> <span
                                class="input-group-btn">
                            <button class="btn btn-ar btn-primary" type="button">Go!</button>
                        </span>
                        </div>
                        <!-- /input-group -->
                    </form>
                </div>
            </div>
            <!-- dropdown -->
        </nav>

    </div>
    <!-- container -->

</header>