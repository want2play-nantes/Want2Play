<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
	
<%@ page isELIgnored="false"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<jsp:include page="/WEB-INF/includes/header.jsp"></jsp:include>

<c:choose>
	<c:when test="${not empty user}">
		<div class="row">
			<div class="spanSubscription well text-center">
				<h2>Abonnement</h2>
				<p>Vous voulez être tenu au courant lors d'un évènement à côté
					de chez vous ?</p>
				<p>Abonnez vous !</p>
				<br /> <a href="/Subscription" role="button"
					class="btn btn-success">S'abonner / Editer</a>
			</div>
			<div class="spanCreateEvent text-center well">
				<h2>Créer un évènement</h2>
				<p>Vous avez envie de jouer mais vous êtes seul ?</p>
				<p>Il vous manque un joueur pour une partie équitable ?</p>
				<br /> <a href="/Event" role="button" class="btn btn-primary ">Créer
					un évènement</a>
			</div>
		</div>
	</c:when>
	<c:otherwise>
		<div class="row">
			<div class="well text-center">
				<p>Connectez vous pour profitez des évènements !</p>
				<a href="/Login" role="button" class="btn btn-success">Se connecter</a>
			</div>
		</div>
	</c:otherwise>
</c:choose>
<%-- <c:if test="${user != null}">
	<c:if test="${fn:length(eventUser) > 0}"> --%>
		<div class="page-header">
			<h1>Mes évènements</h1>
		</div>
		<div class="panel panel-default">
			<div class="panel-heading">18/12/2013</div>
			<table class="table">
				<tbody>
					<%-- <c:forEach var="e" items="${eventUser}"> --%>
						<tr>
							<td width="20%"><h4 class="list-group-item-heading">Football</h4></td>
							<td width="20%"><p class="list-group-item-text">15h15</p></td>
							<td><p class="list-group-item-text">Beaujoire</p></td>
							<td width="10%" class="text-right"><p class="list-group-item-text">16/22 part.</p></td>
							<td width="130px" class="text-right">
								<a href="#" role="button" class="btn btn-warning btn-xs">
									<span class="glyphicon glyphicon-pencil"></span>
								</a>&nbsp;&nbsp;
								<a href="#" role="button" class="btn btn-danger btn-xs">
									<span class="glyphicon glyphicon-trash"></span>
								</a>
							</td>
						</tr>
					<%-- </c:forEach> --%>
				</tbody>
			</table>
		</div>
	<%-- </c:if>
	<c:if test="${fn:length(participationsUser) > 0}"> --%>
		<div class="page-header">
			<h1>Mes participations</h1>
		</div>
		<div class="panel panel-default">
			<div class="panel-heading">15/12/2013</div>
			<table class="table">
				<tbody>
					<%-- <c:forEach var="e" items="${eventUser}"> --%>
						<tr>
							<td width="20%"><h4 class="list-group-item-heading">Basket</h4></td>
							<td width="20%"><p class="list-group-item-text">10h00</p></td>
							<td><p class="list-group-item-text">Cours Beaulieu</p></td>
							<td width="10%" class="text-right"><p class="list-group-item-text">1/2 part.</p></td>
							<td width="130px" class="text-right">
								<a href="#" role="button" class="btn btn-danger btn-xs">Je ne participe plus</a>
							</td>
						</tr>
					<%-- </c:forEach> --%>
				</tbody>
			</table>
		</div>
		<div class="panel panel-default">
			<div class="panel-heading">16/12/2013</div>
			<table class="table">
				<tbody>
					<%-- <c:forEach var="e" items="${eventUser}"> --%>
						<tr>
							<td width="20%"><h4 class="list-group-item-heading">Basket</h4></td>
							<td width="20%"><p class="list-group-item-text">10h00</p></td>
							<td><p class="list-group-item-text">Cours Beaulieu</p></td>
							<td width="10%" class="text-right"><p class="list-group-item-text">1/2 part.</p></td>
							<td width="130px" class="text-right">
								<a href="#" role="button" class="btn btn-danger btn-xs">Je ne participe plus</a>
							</td>
						</tr>
						<tr>
							<td width="20%"><h4 class="list-group-item-heading">Basket</h4></td>
							<td width="20%"><p class="list-group-item-text">10h00</p></td>
							<td><p class="list-group-item-text">Cours Beaulieu</p></td>
							<td width="10%" class="text-right"><p class="list-group-item-text">1/2 part.</p></td>
							<td width="130px" class="text-right">
								<a href="#" role="button" class="btn btn-danger btn-xs">Je ne participe plus</a>
							</td>
						</tr>
						<tr>
							<td width="20%"><h4 class="list-group-item-heading">Basket</h4></td>
							<td width="20%"><p class="list-group-item-text">10h00</p></td>
							<td><p class="list-group-item-text">Cours Beaulieu</p></td>
							<td width="10%" class="text-right"><p class="list-group-item-text">1/2 part.</p></td>
							<td width="130px" class="text-right">
								<a href="#" role="button" class="btn btn-danger btn-xs">Je ne participe plus</a>
							</td>
						</tr>
					<%-- </c:forEach> --%>
				</tbody>
			</table>
		</div>
	<%-- </c:if>
</c:if>
<c:if test="${fn:length(allEvents) > 0}"> --%>
	<div class="page-header">
		<h1>Prochains évènements</h1>
	</div>
	<div class="panel panel-default">
		<div class="panel-heading">10/12/2013</div>
		<table class="table">
			<tbody>
				<%-- <c:forEach var="e" items="${allEvents}"> --%>
					<tr>
						<td width="20%"><h4 class="list-group-item-heading">Footing</h4></td>
						<td width="20%"><p class="list-group-item-text">19h00</p></td>
						<td><p class="list-group-item-text">Parc de l'Erdre</p></td>
						<td width="10%" class="text-right"><p class="list-group-item-text">Venez tous !</p></td>
						<td width="130px" class="text-right">
							<a href="#" role="button" class="btn btn-success btn-xs">Je participe</a>
						</td>
					</tr>
					<tr>
						<td width="20%"><h4 class="list-group-item-heading">Footing</h4></td>
						<td width="20%"><p class="list-group-item-text">19h00</p></td>
						<td><p class="list-group-item-text">Parc de l'Erdre</p></td>
						<td width="10%" class="text-right"><p class="list-group-item-text">Venez tous !</p></td>
						<td width="130px" class="text-right">
							<a href="#" role="button" class="btn btn-success btn-xs">Je participe</a>
						</td>
					</tr>
					<tr>
						<td width="20%"><h4 class="list-group-item-heading">Footing</h4></td>
						<td width="20%"><p class="list-group-item-text">19h00</p></td>
						<td><p class="list-group-item-text">Parc de l'Erdre</p></td>
						<td width="10%" class="text-right"><p class="list-group-item-text">Venez tous !</p></td>
						<td width="130px" class="text-right">
							<a href="#" role="button" class="btn btn-success btn-xs">Je participe</a>
						</td>
					</tr>
				 <%-- </c:forEach> --%>
			</tbody>
		</table>
	</div>
 <%-- </c:if> --%>

<jsp:include page="/WEB-INF/includes/footer.jsp"></jsp:include>