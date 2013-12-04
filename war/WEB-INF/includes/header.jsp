<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page isELIgnored="false" %>

<!doctype html>
<html>
<head>
<meta charset="utf-8">
<title>${initParam['applicationTitle']}</title>
<link rel=stylesheet type="text/css" href="../css/bootstrap.css">
<link rel=stylesheet type="text/css" href="../css/bootstrap-theme.css">
<link rel=stylesheet type="text/css" href="../css/want2play.css">
</head>
<body>
<!-- Intégration de la libraire jQuery --> 
<script src="../js/jquery-2.0.2.min.js"></script>

<!-- Intégration de la libraire de composants du Bootstrap --> 
<script src="../js/bootstrap.js"></script> 

<div id="wrapper">		
	<nav class="navbar navbar-inverse navbar-fixed-top" role="navigation">
		<div class="container">
			<div class="navbar-header">
				<button type="button" class="navbar-toggle" data-toggle="collapse" data-target="#bs-example-navbar-collapse-1"> <span class="sr-only">Toggle navigation</span> <span class="icon-bar"></span> <span class="icon-bar"></span> <span class="icon-bar"></span> </button>
				<a href="/" id="logo" class="navbar-brand">${initParam['applicationTitle']}</a> </div>
			<div class="navbar-collapse collapse" role="navigation" id="bs-example-navbar-collapse-1">
				<ul class="nav navbar-nav navbar-right">
					<c:choose>
					<c:when test="${not empty user}">
					<li class="dropdown"> <a href="#" class="dropdown-toggle" data-toggle="dropdown"><span class="glyphicon glyphicon-user"> </span>&nbsp;&nbsp;${user.email} <b class="caret"></b> </a>
						<ul class="dropdown-menu">
							<li><a href="/Event">Créer un évènement</a></li>
							<li><a href="/Subscription">Gérer mes abonnements</a></li>
							<li class="divider"></li>
							<li><a href="/Logout">Déconnexion</a></li>
						</ul>
					</li>
					</c:when>
					<c:otherwise>
					<li><a href="/Login">Connexion</a></li>
					</c:otherwise>
					</c:choose>
				</ul>
			</div>
		</div>
	</nav>
	
	<div id="content" class="container">