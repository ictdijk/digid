<%@ page session="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ page import="in.yagnyam.digid.*"%>

<%
pageContext.setAttribute("requestURI", request.getRequestURI());
ServletUtils.consumeStatusAttributes(request, pageContext);
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
				<p class="lead">Register</p>
			</section>


		<section>
            <div class="panel panel-default">
                <div class="panel-heading">
                    <h3 class="panel-title">Login</h3>
                </div>
                <div class="panel-body">

                    <form action="/validate-and-register?requestURI=${requestURI}" method="post">

                        <div class="form-group">
                            <label for="digid">DigiD:</label>
                            <input required="required" type="text" class="form-control" name="digid" placeholder="DigiD"/>
                        </div>

                        <div class="form-group">
                            <label for="password">Password:</label>
                            <input required="required" type="text" class="form-control" name="password" placeholder="Password"/>
                        </div>

                        <div class="form-group">
                            <label for="verificationKey">Verification Key:</label>
                            <textarea rows="10" cols="50" required="required" class="form-control" name="verificationKey" placeholder="Verification (Public) Key"></textarea>
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