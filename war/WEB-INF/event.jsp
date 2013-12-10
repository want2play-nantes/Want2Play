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

	<div class="form-group" id="formGroupSport">
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
	<div class="form-group" id="formGroupDate">
		<label for="inputDate" class="col-sm-2 control-label">Date</label>
		<div class="col-sm-10 input-group">
				<input 
					type="date"
					class="form-control"
					value="<c:choose><c:when test="${not empty event}"><joda:format value="${event.date}" pattern="yyyy-MM-dd" /></c:when><c:otherwise><joda:format value="${now}" pattern="yyyy-MM-dd" /></c:otherwise></c:choose>"
					name="date"
					id="inputDate"
					min="<joda:format value="${now}" pattern="yyyy-MM-dd" />"
					placeholder="yyyy-MM-dd"
					required
				/>
				<span class="help-block">La date doit être de la forme yyyy-MM-dd.</span>
		</div>
	</div>
	<div class="form-group" id="formGroupHeure">
		<label for="inputHeure" class="col-sm-2 control-label">Heure</label>
		<div class="col-sm-10 input-group">
				<input
					type="time"
					class="form-control"
					value="<c:choose><c:when test="${not empty event}"><joda:format value="${event.hour}" pattern="HH:mm" /></c:when><c:otherwise><joda:format value="${now}" pattern="HH:mm" /></c:otherwise></c:choose>"
					name="heure"
					id="inputHeure"
					placeholder="HH:mm"
					required
				/>
				<span class="help-block">L'heure doit être de la forme HH:mm.</span>
		</div>
	</div>
	<div class="form-group" id="formGroupLieu">
		<label for="inputLieu" class="col-sm-2 control-label">Lieu</label>
		<div class="col-sm-10 input-group">
				<input type="text"
					class="form-control"
					value="<c:if test="${not empty event}">${event.place}</c:if>"
					name="lieu" id="inputLieu"
					required
				/>
				<span class="help-block">Le lieu de activité. Soyez le plus précis possible.</span>
		</div>
	</div>
	<div class="form-group" id="formGroupPart">
		<label for="inputParticipants" class="col-sm-2 control-label">Participants</label>
		<div class="col-sm-10 input-group">
			<input 
				type="number"
				class="form-control"
				value="<c:choose><c:when test="${not empty event}">${event.nbParticipantsMax}</c:when><c:otherwise>1</c:otherwise></c:choose>"
				name="nbPart"
				id="inputParticipants"
				step="1"
				min="1"
				max="100"
				placeholder="Nombre maximum de participants"
				required
				data-validation="number"
				data-validation-allowing="range[1;100]"
			/>
			<span class="help-block">Nombre de participant que vous recherchez (entre 1 et 100).</span>
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

<script type="text/javascript" src="/js/checkEventForm.js"></script>

<jsp:include page="/WEB-INF/includes/footer.jsp"></jsp:include>