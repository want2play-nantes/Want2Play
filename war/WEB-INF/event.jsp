<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page isELIgnored="false" %>

<jsp:include page="/WEB-INF/includes/header.jsp"></jsp:include>

<div class="page-header">
	<h1>Evènement</h1>
	<p>
		Lorem ipsum dolor sit amet, consectetur adipiscing elit. Proin in libero vel urna lobortis scelerisque id luctus diam. Maecenas aliquet, tellus at faucibus pulvinar, elit felis lacinia risus, blandit gravida est nunc sit amet augue. Etiam tempus magna lacinia augue posuere, vel varius massa euismod. Lorem ipsum dolor sit amet, consectetur adipiscing elit. Donec a ipsum lacus. Aliquam sit amet imperdiet velit. Etiam ullamcorper, mauris ultricies egestas pretium, urna felis rhoncus arcu, ac cursus elit mi et elit. Sed at cursus nibh. Aenean quam neque, rutrum eget nisl eget, dignissim malesuada arcu. Quisque sodales eleifend lorem, eget auctor est placerat quis. Vestibulum ante ipsum primis in faucibus orci luctus et ultrices posuere cubilia Curae; Donec auctor mi a euismod fringilla.
	</p>
</div>
		<form class="form-horizontal" role="form">
			<div class="form-group">
				<label class="col-sm-2 control-label">Créateur</label>
    			<div class="col-sm-10">
      				<p class="form-control-static">${nickname}</p>
    			</div>
			</div>
			<div class="form-group">
			<label class="col-sm-2 control-label">Sport</label>
			<div class="col-sm-10">
				<select class="form-control" name="sport" required>
  					<option>Football</option>
  					<option>Basket</option>
				  	<option>Handball</option>
				  	<option>Tennis</option>
				  	<option>Footing</option>
				</select>
				</div>
			</div>
			<div class="form-group">
				<label for="inputDate" class="col-sm-2 control-label">Date</label>
				<div class="col-sm-10 input-group">
					<span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span></span>
					<input type="date" class="form-control" name="date" id="inputDate" placeholder="Date" required>
				</div>
			</div>
			<div class="form-group">
				<label for="inputHeure" class="col-sm-2 control-label">Heure</label>
				<div class="col-sm-10 input-group">
					<span class="input-group-addon"><span class="glyphicon glyphicon-time"></span></span>
					<input type="time" class="form-control" name="heure"  id="inputHeure" placeholder="Date" required>
				</div>
			</div>
			<div class="form-group">
				<label for="inputLieu" class="col-sm-2 control-label">Lieu</label>
				<div class="col-sm-10 input-group">
					<span class="input-group-addon"><span class="glyphicon glyphicon-screenshot"></span></span>
					<input type="text" class="form-control" name="lieu" id="inputLieu" placeholder="Soyez le plus précis possible" required>
				</div>
			</div>
			<div class="form-group">
				<label for="inputParticipants" class="col-sm-2 control-label">Participants</label>
				<div class="col-sm-10 input-group">
					
					<input type="text" class="form-control" name="nbPart" id="inputParticipants" placeholder="Nombre maximum de participants" required>
				</div>
			</div>
			<hr class="soften" />
			
			<div class="form-group">
				<div class="text-center">
					<button type="submit" class="btn btn-info" formaction="/CreateEvent">Enregistrer les modifications</button>
				</div>
			</div>
		</form>

<jsp:include page="/WEB-INF/includes/footer.jsp"></jsp:include>