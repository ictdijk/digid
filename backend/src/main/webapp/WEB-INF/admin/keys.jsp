<%@ page session="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ page import="in.yagnyam.digid.*"%>

<%
pageContext.setAttribute("requestURI", request.getRequestURI());
ServletUtils.consumeStatusAttributes(request, pageContext);
pageContext.setAttribute("keys", SetupKeysServlet.allSigningKeys());
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
				<p class="lead">DigiD Keys Setup</p>
			</section>

			<section>
                <div class="panel panel-default">
                    <div class="panel-heading">
                        <h3 class="panel-title">Signing Keys</h3>
                    </div>
                    <div class="panel-body">
                        <table class="table table-striped">
                            <thead>
                                <tr>
                                    <th>Name</th>
                                    <th>Signing Key</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:forEach var="i" items="${keys}">
                                    <tr>
                                        <td>${i.name}</td>
                                        <td>${i.signingKey}</td>
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
                    <h3 class="panel-title">Add/Amend Key</h3>
                </div>
                <div class="panel-body">

                    <form action="/admin/setup-keys?requestURI=${requestURI}" method="post">

                        <div class="form-group">
                            <label for="name">Key Name:</label>
                            <input required="required" type="text" class="form-control" name="name" placeholder="Key Name"/>
                        </div>

                        <div class="form-group">
                            <label for="signingKey">Signing Private Key:</label>
                            <textarea rows="10" cols="50" required="required" class="form-control" name="signingKey" placeholder="Signing Private Key"></textarea>
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