<%@ page session="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ page import="in.yagnyam.digid.*"%>

<%
pageContext.setAttribute("persons", SetupPersonsServlet.allPersons());
%>

<!DOCTYPE html>
<html>
<head>
<%@ include file="../../header.jsp"%>
<title>DigiD</title>
</head>

<%@ include file="../../brand.jsp"%>


<body>

		<div class="container">
		
			<section class="hidden-print">
				<p class="lead">DigiD Users</p>
			</section>


			<section>
                <div class="panel panel-default">
                    <div class="panel-heading">
                        <h3 class="panel-title">Avialable Users</h3>
                    </div>
                    <div class="panel-body">
                        <table class="table table-striped">
                            <thead>
                                <tr>
                                    <th>DigiD</th>
                                    <th>Password</th>
                                    <th>BSN</th>
                                    <th>Name</th>
                                    <th>DOB</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:forEach var="i" items="${persons}">
                                    <tr>
                                        <td>${i.digid}</td>
                                        <td>${i.password}</td>
                                        <td>${i.bsn}</td>
                                        <td>${i.name}</td>
                                        <td>${i.dob}</td>
                                    </tr>
                                </c:forEach>
                            </tbody>
                        </table>
                    </div>
                </div>
		    <section>


	</div>

</body>

	<%@ include file="../../footer.jsp"%>

</html>