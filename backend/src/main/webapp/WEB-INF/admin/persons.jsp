<%@ page session="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ page import="in.yagnyam.digid.*"%>

<%
pageContext.setAttribute("requestURI", request.getRequestURI());
ServletUtils.consumeStatusAttributes(request, pageContext);
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
                        <h3 class="panel-title">Existing Users</h3>
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


		<section>
            <div class="panel panel-default">
                <div class="panel-heading">
                    <h3 class="panel-title">Add/Amend User</h3>
                </div>
                <div class="panel-body">

                    <form action="/admin/setup-persons?requestURI=${requestURI}" method="post">
					
                        <div class="form-group">
                            <label for="digid">DigiD:</label>
                            <input required="required" type="text" class="form-control" name="digid" placeholder="DigiD"/>
                        </div>

						
                        <div class="form-group">
                            <label for="password">Password:</label>
                            <input required="required" type="text" class="form-control" name="password" placeholder="Password"/>
                        </div>

                        <div class="form-group">
                            <label for="bsn">BSN:</label>
                            <input required="required" type="number" class="form-control" name="bsn" placeholder="BSN"/>
                        </div>

                        <div class="form-group">
                            <label for="name">Name:</label>
                            <input required="required" type="text" class="form-control" name="name" placeholder="Name"/>
                        </div>


                        <div class="form-group">
                            <label for="dob">DOB:</label>
                            <input required="required" type="date" class="form-control" name="dob" placeholder="Date of Birth"/>
                        </div>


                        <input type="submit" class="btn btn-primary" value="Save" />
                        <c:if test="${not empty success}">
                            <span class="label label-success">${success}</span>
                        </c:if>
                        <c:if test="${not empty error}">
                            <span class="label label-danger">${error}</span>
                        </c:if>
                    </form>

                </div>
            </div>
	    </section>


	</div>

</body>

<%@ include file="../../footer.jsp"%>

</html>