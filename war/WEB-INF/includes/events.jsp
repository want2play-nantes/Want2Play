<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%@ page import="com.want2play.core.Event" %>
<%@ page import="com.want2play.datastore.DatastoreController" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="java.util.Formatter" %>
<%@ page import="java.util.Locale" %>
<%@ page import="com.google.appengine.api.users.User" %>
<%@ page import="com.google.appengine.api.users.UserService" %>
<%@ page import="com.google.appengine.api.users.UserServiceFactory" %>

<div id="events">
	<ul>
		<%
		for(Event e : DatastoreController.getAllEvents()) {
		%>
		<li>
			<h3><%= e.getSport() %></h3>
			<p><b>Organisateur :</b> <%= e.getCreator() %></p>
			<p><b>Endroit :</b> <%= e.getPlace() %></p>
			<p><b>Date/Heure :</b> <%= new SimpleDateFormat("EEEE d MMMM yyyy", Locale.FRANCE).format(e.getDate().getTime()) + " à " + new SimpleDateFormat("HH:mm", Locale.FRANCE).format(e.getDate().getTime()) %></p> 
		</li>
		<% 
		} 
		%>
	</ul>
</div>