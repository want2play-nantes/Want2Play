<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%@ page import="com.google.appengine.api.users.UserService" %>
<%@ page import="com.google.appengine.api.users.UserServiceFactory" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel=stylesheet type="text/css" href="/design.css">
<link rel=stylesheet type="text/css" href="/scripts/css/smoothness/jquery-ui-1.10.3.custom.min.css">
<script type="text/javascript" src="/scripts/jquery-2.0.2.min.js"></script>
<script type="text/javascript" src="/scripts/ui/jquery.ui.core.min.js"></script>
<title>${initParam['applicationTitle']}</title>
</head>
<body>
	<div id="wrapper">
		<div id="header">
			<h1 id="logo"><a href="/">${initParam['applicationTitle']}</a></h1>
			<div id="auth">
				<% 
				if(UserServiceFactory.getUserService().isUserLoggedIn()) {
					if(request.getSession(true).getAttribute("user") == null) {
						response.sendRedirect("/Login");
                                                //test
                                        }
					else {
				%>
					<p><a href="/Logout">Déconnexion</a></p>
					<p><b>Vous être connecté en tant que ${user.email}</b></p>
				<%
					}
				} else {
				%>
					<p><a href="/Login">Connexion</a></p>
					<p>Vous êtes déconnecté</p>
				<% } %>
			</div>
		</div> <!-- End of header -->
		<div id="content">