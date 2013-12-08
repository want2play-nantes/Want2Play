<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page isELIgnored="false" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<jsp:include page="/WEB-INF/includes/header.jsp"></jsp:include>

<div class="page-header">
			<h1>Abonnement</h1>
			<p>
			Lorem ipsum dolor sit amet, consectetur adipiscing elit. Mauris ut nisi dui. Curabitur mattis et nisi vitae egestas. Ut porta tellus porttitor, ultricies turpis at, iaculis augue. Aenean pharetra tempus dolor, a tincidunt urna lobortis sed. Suspendisse aliquet eu magna non interdum. Pellentesque habitant morbi tristique senectus et netus et malesuada fames ac turpis egestas. Curabitur vitae pellentesque libero. Aliquam erat volutpat. Etiam non justo ac ante dapibus vehicula. Sed elementum ultricies facilisis. Mauris sed diam a lorem tempor laoreet vitae imperdiet justo. Suspendisse fringilla quam id odio lacinia, vitae volutpat lorem luctus. In nec ullamcorper risus. Proin sollicitudin pulvinar sem, a consectetur lacus rutrum ut. Praesent dapibus felis nec arcu rhoncus viverra.
		</p>
		</div>
		<div id="subscription">
		<form class="form-horizontal" role="form" action= "SubscriptionSave">
			
			<c:forEach var="e" items="${subs.enabledSubscriptions}">
			<div class="checkbox-inline">
					<label>
						<input type="checkbox" name="${e.key.label}" <c:if test = "${e.value}">checked</c:if> >
						${e.key.label}
					</label>
			</div>
			</c:forEach>
			
			<hr/>
			<div class="form-group">
				<div class="checkbox">
						<label>
							<input type="checkbox" class="checkAll" value="">
					 			Cocher/décocher tout
						</label>
					</div>
			</div>
			<div class="form-group">
				<div class="text-center">
					<button type="submit" class="btn btn-info">Enregistrer les modifications</button>
				</div>
			</div>
		</form>
		</div>
		
		<script>
		$(function () {
			$(".checkAll").click( function(){
			    $(':checkbox').prop('checked', this.checked);
			});
		});
		</script>
		 
		
		<jsp:include page="/WEB-INF/includes/footer.jsp"></jsp:include>