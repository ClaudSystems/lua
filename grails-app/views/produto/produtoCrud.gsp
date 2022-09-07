<%--
  Created by IntelliJ IDEA.
  User: Claudino
  Date: 18/01/2016
  Time: 12:49
--%>
%{--
<%  def userService = grailsApplication.mainContext.getBean("utilizadorService") %>
--}%
<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <title>Lua-Produtos</title>
    <meta name="layout" content="main"/>
    <z:head/>
    <script type="text/javascript">
    function readURL(input) {
        if (input.files && input.files[0]) {
            var reader = new FileReader();

            reader.onload = function (e) {
                $('#image_upload_preview').attr('src', e.target.result);
            }

            reader.readAsDataURL(input.files[0]);
        }
    }

    $("#inputFile").change(function () {
        readURL(this);
    });
</script>
</head>

<body>
<div  style="background: #ffbcaf;font-size:16px "  class="panel-heading">Produtos

</div>
<sec:ifNotGranted roles="produto">
    <g:render template="/layouts/acessoNegado"/>
</sec:ifNotGranted>
<sec:access expression="hasRole('produto')">
  %{--  <form id="form1" runat="server">
        <input type='file' id="inputFile" />
        <img id="image_upload_preview" src="http://placehold.it/100x100" alt="your image" />
    </form>--}%
    <z:body/>
</sec:access>

</body>
</html>