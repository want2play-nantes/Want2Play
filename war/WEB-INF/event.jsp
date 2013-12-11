<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="joda" uri="http://www.joda.org/joda/time/tags" %>
<%@page isELIgnored="false"%>

<jsp:include page="/WEB-INF/includes/header.jsp"></jsp:include>

<div class="page-header">
	<h1>Evènement</h1>
	<p>Lorem ipsum dolor sit amet, consectetur adipiscing elit. Proin
		in libero vel urna lobortis scelerisque id luctus diam. Maecenas
		aliquet, tellus at faucibus pulvinar, elit felis lacinia risus,
		blandit gravida est nunc sit amet augue. Etiam tempus magna lacinia
		augue posuere, vel varius massa euismod. Lorem ipsum dolor sit amet,
		consectetur adipiscing elit. Donec a ipsum lacus. Aliquam sit amet
		imperdiet velit. Etiam ullamcorper, mauris ultricies egestas pretium,
		urna felis rhoncus arcu, ac cursus elit mi et elit. Sed at cursus
		nibh. Aenean quam neque, rutrum eget nisl eget, dignissim malesuada
		arcu. Quisque sodales eleifend lorem, eget auctor est placerat quis.
		Vestibulum ante ipsum primis in faucibus orci luctus et ultrices
		posuere cubilia Curae; Donec auctor mi a euismod fringilla.</p>
</div>

<form id="eventForm" class="form-horizontal" role="form" action="/SaveEvent" method="get">

	<div class="form-group">
		<label class="col-sm-2 control-label">Créateur</label>
		<div class="col-sm-10"><p class="form-control-static">${user.email}</p></div>
	</div>

	<div class="form-group">
		<label for="selectSport" class="col-sm-2 control-label">Sport</label>
		<div class="col-sm-10">
			<select 
				class="form-control"
				name="sport"
				id="selectSport"
				required
				<c:if test="${not empty event}">disabled</c:if>
			>
				<option disabled></option>
				<c:forEach var="sport" items="${sportsList}">
					<option <c:if test="${event.sport == sport}">selected</c:if> value="${sport}">${sport.label}</option>
				</c:forEach>
			</select>
		</div>
	</div>
	
	<div class="form-group">
		<label for="inputDate" class="col-sm-2 control-label">Date</label>
		<div class="col-sm-10 input-group">
			<div
				class="bfh-datepicker"
				data-min="<joda:format value="${now}" pattern="dd/MM/yyyy" />"
				data-format="d/m/y"
				data-close="true"
				data-name="date"
				data-date="<c:choose><c:when test="${not empty event}"><joda:format value="${event.date}" pattern="dd/MM/yyyy" /></c:when><c:otherwise><joda:format value="${now}" pattern="dd/MM/yyyy" /></c:otherwise></c:choose>"
			>
			</div>
		</div>
	</div>
	
	<div class="form-group">
		<label for="inputHeure" class="col-sm-2 control-label">Heure</label>
		<div class="col-sm-10 input-group">
			<div
				class="bfh-timepicker"
				data-name="heure"
				data-time="<c:choose><c:when test="${not empty event}"><joda:format value="${event.hour}" pattern="HH:mm" /></c:when><c:otherwise><joda:format value="${now}" pattern="HH:mm" /></c:otherwise></c:choose>"
			>
			</div>
		</div>
	</div>
	
	<div class="form-group">
		<label for="inputLieu" class="col-sm-2 control-label">Lieu</label>
		
		<div class="col-sm-10 input-group">
			<span class="input-group-addon"><span class="glyphicon glyphicon-screenshot"></span></span>
				<input type="text"
					class="form-control"
					value="<c:if test="${not empty event}">${event.place}</c:if>"
					name="lieu"
					id="inputLieu"
					placeholder="Soyez le plus précis possible !"
					required
				/>
		</div>
	</div>
	
	<div class="form-group">
		<label for="inputParticipants" class="col-sm-2 control-label">Participants</label>
		<div class="col-sm-10 input-group">
			<div class="bfh-slider"
				data-name="nbPart"
				data-value="<c:choose><c:when test="${not empty event}">${event.nbParticipantsMax}</c:when><c:otherwise>1</c:otherwise></c:choose>"
				data-min="<c:choose><c:when test="${not empty event}">${event.nbParticipants}</c:when><c:otherwise>1</c:otherwise></c:choose>"
				data-max="30"
			>
			</div>
		</div>
	</div>
	
	<hr class="soften" />

	<div class="form-group">
		<div class="text-center">
			<input type="hidden" name="event" value="${param.event}" />
			<input type="hidden" name="mode" value="<c:choose><c:when test="${not empty event}">edit</c:when><c:otherwise>new</c:otherwise></c:choose>" />
			<button type="submit" class="btn btn-info">Enregistrer les modifications</button>
		</div>
	</div>
</form>

<jsp:include page="/WEB-INF/includes/footer.jsp"></jsp:include>