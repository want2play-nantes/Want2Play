<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
	
<%@ page isELIgnored="false"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="joda" uri="http://www.joda.org/joda/time/tags" %>

<jsp:include page="/WEB-INF/includes/header.jsp"></jsp:include>

<c:choose>
	<c:when test="${not empty user}">
		<div class="row">
			<div class="spanSubscription well text-center">
				<h2>Abonnement</h2>
				<p>Vous voulez être tenu au courant lorsqu'une activité se crée à côté
					de chez vous ?</p>
				<p>Abonnez vous !</p>
				<br /> <a href="/Subscription" role="button"
					class="btn btn-success">S'abonner / Editer</a>
			</div>
			<div class="spanCreateEvent text-center well">
				<h2>Créer un évènement</h2>
				<p>Vous avez envie de jouer mais vous êtes seul ?</p>
				<p>Il vous manque un joueur pour une partie équitable ?</p>
				<br /> <a href="/Event" role="button" class="btn btn-primary ">Créer un évènement</a>
			</div>
		</div>
	</c:when>
	<c:otherwise>
		<div class="row">
			<div class="well text-center">
				<p>Connectez vous pour pouvoir profitez de toutes les activités et vous abonnez !</p>
				<a href="/Login" role="button" class="btn btn-success">Se connecter</a>
			</div>
		</div>
	</c:otherwise>
</c:choose>
<c:if test="${user != null}">
	<c:if test="${fn:length(eventUser) > 0}">
		<div class="page-header">
			<h1>Mes évènements</h1>
		</div>
		<c:forEach var="item" items="${eventUser}">
		<div class="panel panel-default">
			<div class="panel-heading text-center"><strong><joda:format value="${item.key}" pattern="EEEE dd MMMM yyyy"/></strong></div>
			<table class="table">
				<tbody>
					<c:forEach var="e" items="${item.value}">
						<tr>
							<td width="20%"><h4 class="list-group-item-heading">${e.sport.label}</h4></td>
							<td width="20%"><p class="list-group-item-text">${e.hourStr}</p></td>
							<td><p class="list-group-item-text">${e.place}</p></td>
							<td width="10%" class="text-right"><p class="list-group-item-text">${e.nbParticipants} / ${e.nbParticipantsMax}</p></td>
							<td width="130px" class="text-right">
								<a href="/Event?mode=edit&event=${e.keyStr}" role="button" class="btn btn-warning btn-xs">
									<span class="glyphicon glyphicon-pencil"></span>
								</a>
								&nbsp;&nbsp;
								<!--<a href="/Event?mode=delete&event=${e.keyStr}" role="button" class="btn btn-danger btn-xs">-->
								<a href="/DeleteEvent?mode=delete&event=${e.keyStr}" role="button" class="btn btn-danger btn-xs"  onclick="return confirm('Voulez-vous vraiment supprimer cette évènement?')">
									<span class="glyphicon glyphicon-trash"></span>
								</a>
							</td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
		</div>
		</c:forEach>
	</c:if>
	<c:if test="${fn:length(participationsUser) > 0}">
		<div class="page-header">
			<h1>Mes participations</h1>
		</div>
		
		<c:forEach var="item" items="${participationsUser}">
		<div class="panel panel-default">
			<div class="panel-heading text-center"><strong><joda:format value="${item.key}" pattern="EEEE dd MMMM yyyy"/></strong></div>
			<table class="table">
				<tbody>
					<c:forEach var="e" items="${item.value}">
						<tr>
							<td width="20%"><h4 class="list-group-item-heading">${e.sport.label}</h4></td>
							<td width="20%"><p class="list-group-item-text">${e.hourStr}</p></td>
							<td><p class="list-group-item-text">${e.place}</p></td>
							<td width="10%" class="text-right"><p class="list-group-item-text">${e.nbParticipants} / ${e.nbParticipantsMax}</p></td>
							<td width="130px" class="text-right">
								<a href="/Participation?event=${e.keyStr}&mode=delete" role="button" class="btn btn-danger btn-xs">Je ne participe plus</a>
							</td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
		</div>
		</c:forEach>
	</c:if>
</c:if>
	<div class="page-header">
		<h1>Prochains évènements</h1>
	</div>
	<c:choose>
	<c:when test="${fn:length(allEvents) > 0}">
	
	<c:forEach var="item" items="${allEvents}">
		<div class="panel panel-default">
			<div class="panel-heading text-center"><strong><joda:format value="${item.key}" pattern="EEEE dd MMMM yyyy"/></strong></div>
			<table class="table">
				<tbody>
					<c:forEach var="e" items="${item.value}">
						<tr>
						<td width="20%"><h4 class="list-group-item-heading">${e.sport.label}</h4></td>
						<td width="20%"><p class="list-group-item-text">${e.hourStr}</p></td>
						<td><p class="list-group-item-text">${e.place}</p></td>
						<td width="10%" class="text-right"><p class="list-group-item-text">${e.nbParticipants} / ${e.nbParticipantsMax}</p></td>
						<td width="130px" class="text-right">
							<a href="/Participation?event=${e.keyStr}&mode=add" role="button" class="btn btn-success btn-xs <c:if test="${e.nbParticipants >= e.nbParticipantsMax || empty user}">disabled</c:if>">Je participe</a>
						</td>
					</tr>
					</c:forEach>
				</tbody>
			</table>
		</div>
		</c:forEach>
	 </c:when>
	 <c:otherwise>
	 	<div class="well text-center"><h4>Aucun évènement</h4></div>
	 </c:otherwise>
	 </c:choose>


<jsp:include page="/WEB-INF/includes/footer.jsp"></jsp:include>
