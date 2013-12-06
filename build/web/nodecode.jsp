<%-- 
    Document   : index
    Created on : 6 Nov, 2013, 5:17:06 AM
    Author     : gautham
--%>

<%@page import="java.sql.SQLException"%>
<%@page contentType="text/html" pageEncoding="UTF-8" import="java.math.*, java.sql.Statement, Helpers.DB, Base.PaillierCryptoSystem, java.sql.Connection"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>SimpleChat v0.1</title>
        <link href="css/bootstrap.min.css" rel="stylesheet" media="screen">
        <link href="css/main.css" rel="stylesheet" media="screen">
        <script src="js/jquery.min.js"></script>
        <script src="js/bootstrap.min.js"></script>
        <script>
                function Init() {
                        <% if (session.getAttribute("id") == null) out.println("$(location).attr('href', 'login.jsp');"); else out.println("Refresh();"); %>
                }
        </script>
    </head>
    
    <body onload="Init()">
        
        <div id="content" class="box" style=" margin-bottom: 90px; overflow-y: scroll; width: 70%; height: 400px; top: 70px; position: relative; ">
            <%
                if (session.getAttribute("id") != null) {
                       Connection con = DB.GetCon();  
                    Statement st = con.createStatement();
                    String query = "SELECT * FROM `simplechat`.`users` WHERE id = '" + session.getAttribute("id").toString() + "'";
                    java.sql.ResultSet rs = st.executeQuery(query);
                    PaillierCryptoSystem P = new PaillierCryptoSystem();
                    PaillierCryptoSystem Q = new PaillierCryptoSystem();
                    if (rs.next()) { P = new PaillierCryptoSystem(rs.getInt(4), rs.getInt(5)); }
                    String Me = "<div class='Me'>" + rs.getString(3) + "</div>";
                    if (session.getAttribute("id").equals("00000001")) query = "SELECT * FROM `simplechat`.`users` WHERE id = '00000002'";
                    else query = "SELECT * FROM `simplechat`.`users` WHERE id = '00000001'";
                    rs = st.executeQuery(query);
                    if (rs.next()) { Q = new PaillierCryptoSystem(rs.getInt(4), rs.getInt(5)); }
                    String OtherGuy = "<div class='OtherGuy'>" + rs.getString(3) + "</div>";
                    query = "SELECT * FROM `simplechat`.`messages` ORDER BY timestamp ASC";
                    rs = st.executeQuery(query);
                    while (rs.next()) {
                        out.print("<div class='date'>[" + rs.getString(4) + "]</div>  ");
                        if (session.getAttribute("id").equals(rs.getString(1))) {
                            out.print(Me);
                            out.print(": ");
                            out.print(rs.getString(3));
                        }
                        else {
                            out.print(OtherGuy);
                            out.print(": ");
                            out.print(rs.getString(3));
                        }
                        out.print("<br/>");
                    }
                        %>
        </div>
        <form id="form-msg" style="position: relative; left: 27%; width: 70%; " action="">
            <input type="text" id="msg" name="msg" style="width: 500px;" class="input-block-level" placeholder="Message">
            <button class="btn btn-large btn-primary" style="width: 100px;" id="btn-sendmsg" type="submit">Send</button>
        </form>
       
        <script>
            function Refresh() {
                setTimeout(function() {
                    $.ajax({  
                            type: "GET", url: "chat2", data: '',  
                            success: function(data) {
                                    p = $(data).html();
                                    $('#content').html(p);
                                    setTimeout(Refresh(), 3000);
                            },
                            error: function() {
                                setTimeout(Refresh(), 3000);
                            }
                    });
                }, 3000);
            }
            $("#btn-sendmsg").click(function() {
                $("#alertdiv").remove();
                var msg = $("#msg").val();
                if (msg.length <  1) { 
                        $('body').append('<div id="alertdiv" class="alert alert-error"> <button type="button" class="close" data-dismiss="alert">&times;</button><strong>Ooops!</strong>Message Too Short</div>');
                        setTimeout(function() { $("#alertdiv").remove(); }, 5000);
                }
                else
                    $.ajax({  
                            type: "POST", url: "sendmsg", data: 'msg=' + msg + '&receiver=<%  if (session.getAttribute("id").equals("00000001")) out.print("00000002"); else out.print("00000001"); %>',  
                            success: function(data) {
                                    p = $(data).html();
                                    if (p === "YES") window.location.href = 'nodecode.jsp';
                                    else {
                                            $('body').append('<div id="alertdiv" class="alert alert-error"> <button type="button" class="close" data-dismiss="alert">&times;</button><strong>Ooops!</strong>Something went wrong! Try Again!</div>');
                                            setTimeout(function() { $("#alertdiv").remove(); }, 5000);							
                                    }
                            },
                            error: function() {
                                    $('body').append('<div id="alertdiv" class="alert alert-error"> <button type="button" class="close" data-dismiss="alert">&times;</button><strong>Ooops!</strong>Something went wrong. Try Again!</div>');
                                    setTimeout(function() { $("#alertdiv").remove(); }, 5000);
                            }
                    });
            return false;  
    } );

        </script>
        
        <%
                        }
                            %>
    </body>
</html>

