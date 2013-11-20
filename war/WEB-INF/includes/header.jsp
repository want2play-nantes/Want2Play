<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel=stylesheet type="text/css" href="/css/bootstrap.css">
<link rel=stylesheet type="text/css" href="/css/bootstrap-theme.css">
<link rel=stylesheet type="text/css" href="/css/want2play.css">
<title>${initParam['applicationTitle']}</title>
</head>
<body>
	<script type="text/javascript" src="/scripts/bootstrap.js"></script>
	<script type="text/javascript" src="/scripts/jquery-2.0.3.js"></script>
	<div id="wrapper">
		<div id="header">
			<h1 id="logo">
				<a href="/">${initParam['applicationTitle']}</a>
			</h1>
			<div id="auth">
				<p>
					<a href="${loginLogoutHref}">${loginLogoutButtonLabel}</a>
				</p>
				<p>
					<b>${userStatus}</b>
				</p>
			</div>
		</div>
		<!-- End of header -->
		<div id="content">