<%-- 
    Document   : login
    Created on : 6 Nov, 2013, 6:47:14 AM
    Author     : gautham
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
        <link href="css/bootstrap.min.css" rel="stylesheet" media="screen">
        <link href="css/main.css" rel="stylesheet" media="screen">
        <script src="js/jquery.min.js"></script>
        <script src="js/bootstrap.min.js"></script>
        <script>
                function Init() {
                        <% if (session.getAttribute("id") != null) out.println("$(location).attr('href', 'index.jsp');"); %>
                }
                <%
                %>
        </script>
    </head>
      <body onload="Init()">
    <div id="form-signin-container">
      <form id="form-signin" class="box" action="">
        <h2 id="form-signin-heading">Please Sign In</h2>
        <div class="input-prepend">
			<span class="add-on">000000</span>
			<input class="span2" name="ID" id="id" type="text" placeholder="User ID">
		</div>
        <input type="password" id="password" name="password" class="input-block-level" placeholder="Password">
        <button class="btn btn-large btn-primary centerh" style="width: 100px;" id="btn-login" type="submit">Sign in</button>
      </form>
    </div>
    <script>
		$("#btn-login").click(function() {
			$("#alertdiv").remove();
			var id = $("#id").val();
			var pass = $("#password").val();
			if (id.length !=  2 || pass.length < 3) { 
				$('#form-signin-container').append('<div id="alertdiv" class="alert alert-error"> <button type="button" class="close" data-dismiss="alert">&times;</button><strong>Ooops!</strong>Looks like the ID or password you entered is not of correct length!</div>');
				setTimeout(function() { $("#alertdiv").remove(); }, 5000);
			}
			else
				$.ajax({  
					type: "POST", url: "login", data: 'id=000000'  + id + '&password=' + $("#password").val(),  
					success: function(data) {
												p = $(data).html();
												if (p === "YES") $(location).attr('href', 'index.jsp');
												else {
													$('#form-signin-container').append('<div id="alertdiv" class="alert alert-error"> <button type="button" class="close" data-dismiss="alert">&times;</button><strong>Ooops!</strong>Looks like the ID or password you entered is incorrect!</div>');
													setTimeout(function() { $("#alertdiv").remove(); }, 5000);							
												}
											},
					error: function() {
						$('#form-signin-container').append('<div id="alertdiv" class="alert alert-error"> <button type="button" class="close" data-dismiss="alert">&times;</button><strong>Ooops!</strong>Something went wrong. Try Again!</div>');
						setTimeout(function() { $("#alertdiv").remove(); }, 5000);
					}
				});
			return false;  
		} );
    </script>
</html>
