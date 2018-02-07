<jsp:root xmlns:jsp="http://java.sun.com/JSP/Page"
	xmlns:spring="http://www.springframework.org/tags"
	xmlns:c="http://java.sun.com/jsp/jstl/core"
	xmlns:form="http://www.springframework.org/tags/form"
	xmlns:sec="http://www.springframework.org/security/tags" version="2.0"	
	xmlns:fn="http://java.sun.com/jsp/jstl/functions"
		>
	<jsp:directive.page language="java" contentType="text/html" />

		<c:set var="reqUrl">${pageContext.request.requestURL}</c:set>
	<c:set var="baseURL" value="${fn:replace(reqUrl, pageContext.request.requestURI,pageContext.request.contextPath)}" />

	<html xmlns="http://www.w3.org/1999/xhtml" lang="en" xml:lang="en">
<head>


<title>Patho <spring:message code="login.login"/></title>

<link href="public/theme/css/bootstrap.min.css" rel="stylesheet"
	media="screen" />


<meta name="viewport"
	content="width=device-width, initial-scale=1, maximum-scale=1" />


<style type="text/css">
.modal-footer {
	border-top: 0px;
}
</style>



</head>
<body>


	<sec:authorize var="loggedIn" access="isAuthenticated()" />

	<c:choose>
		<c:when test="${loggedIn}">

			<script type="text/javascript">
				window.location = '${baseURL}/genome'
				
				
			</script>

		</c:when>
		<c:otherwise>



			<![CDATA[
	

	     <script type="text/javascript"	src="public/jslibs/jquery/jquery-1.11.0.min.js"></script>
	<script src="public/theme/js/bootstrap.min.js"></script>

	<script src="${baseURL}/public/bia/bia.js"></script>
 ]]>


			<div id="loginModal" class="show">
				<div class="modal-dialog">
					<div class="modal-content">
					
						<div class="modal-header"
							style="cursor: pointer;"> 
							<a href="${baseURL}/user/main"><img height="200px" src="${baseURL}/public/html/Logo PathogenTARGET.jpg" /></a>
						</div>
						<div class="modal-body">
							<form:form action="login" class="form col-md-12 center-block"
								method="POST">

								

								<c:if test="${param.error != null}">
									<div class="alert alert-danger">Invalid username and
										password.</div>
								</c:if>
								<c:if test="${param.logout != null}">
									<div class="alert alert-success">You have been logged
										out.</div>
								</c:if>

								<input type="hidden" id="returnTo" name="returnTo" />



								<div class="form-group">
									<input type="text" class="form-control input-lg"
										name="username" placeholder="username"
										value="${username}" />
								</div>
								<div class="form-group">
									<input type="password" class="form-control input-lg"
										placeholder="password'" name="password" />
								</div>
								<div class="form-group">
									<button class="btn btn-primary btn-lg btn-block"><spring:message code="login.login"/> </button>
								</div>
								<div class="form-group">
								<spring:message code="login.or"/>
									<a id="btnRegister" class="btn btn-primary btn-lg btn-block" href="${baseURL}/user/register" ><spring:message code="register.register"/> </a>
								</div>
								<div class="form-group">
									<spring:message code="login.or"/>
									<input id="guest_btn" type="button" onclick="window.location = '${baseURL}/genome'" 
									value="" class="btn btn-primary btn-lg btn-block" />
								</div>

								<input type="hidden" name="${_csrf.parameterName}"
									value="${_csrf.token}" />
							</form:form>

						</div>

						<div class="modal-footer">
							<div class="col-md-12">
								

							</div>
						</div>
					</div>
				</div>
			</div>
			<script type="text/javascript">
			var txt = '<spring:message code="login.guest"/>';
			$("#guest_btn").val(txt)
			</script>
<script type="text/javascript">

if(window.location.href.indexOf("_return") != -1){
	$("#returnTo").val(window.location.href.split("_return=")[1])	
	$("#btnRegister").attr("href",$("#btnRegister").attr("href") + "?_return=" + window.location.href.split("_return=")[1])
}



</script>


		</c:otherwise>
	</c:choose>
</body>
	</html>

</jsp:root>