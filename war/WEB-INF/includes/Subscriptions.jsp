

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ page import="com.want2play.core.Subscriptions" %>
<%@ page import="com.want2play.datastore.DatastoreController" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page Abonnement</title>
    </head>
    <body>

    
<h1 align="center"> Abonnez-vous !</h1>
 
<table>
 
<tr>
<td><a class="foot"></a></td>
<td><a class="hande"></a></td>
<td><a class="footing"></a></td>
<td><a class="volley"></a></td>
</tr> 

<tr>

<td><input type="checkbox" name="Football" value="Java"/>Football </td>
<td><input type="checkbox" name="Handball" value="Java1"/>Handball </td>
<td><input type="checkbox" name="Footing" value="Java2"/>Footing</td>
<td><input type="checkbox" name="Volleyball" value="Java3"/>Volley ball </td>
</tr>

</table>
<table>
<tr>
<td><h5> 
<%--<jsp:useBean id="abonne" scope="request" class="com.want2play.core.DatastoreController"></jsp:useBean>--%>
    vous êtes abonné : </h5></td>
    <%--<%=abonne.getSubscriptionUser(Subscriptions.user)%> --%>
</tr> 
</table>
<input type="submit" name ="bout" value="Valider" />
    </body>
</html>
