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

<link href="${baseURL}/public/theme/css/bootstrap.min.css" rel="stylesheet"
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





			<div id="loginModal" class="show">
				<div class="modal-dialog">
					<div class="modal-content">
					
						<div class="modal-header"
							style="cursor: pointer;"> 
							<a href="${baseURL}/user/main"><img height="200px" src="${baseURL}/public/html/Logo PathogenTARGET.jpg" /></a>
						</div>
						<div class="modal-body">
							<form:form action="${baseURL}/user" class="form col-md-12 center-block"
								method="POST">

								

								<c:if test="${param.error != null}">
									<div class="alert alert-danger">${param.error}</div>
								</c:if>
								

								<input type="hidden" id="returnTo" name="returnTo" />
								
<script type="text/javascript">

if(window.location.href.indexOf("_return") != -1){
	$("#returnTo").val(window.location.href.split("_return=")[1])
	
	
}
</script>


								<div class="form-group">
									<input id="username" type="text" class="form-control input-lg"
										name="username" placeholder="User"
										/>
								</div>
								<div class="form-group">
									<input type="email" class="form-control input-lg"
										name="email" placeholder="Email"
										/>
								</div>
								<div class="form-group">
									<input type="text" class="form-control input-lg"
										name="institutions" placeholder="Institution/s"
										 />
								</div>
								<div class="form-group">
									<input type="password" class="form-control input-lg"
										placeholder="Password" name="password" />
								</div>
								<div class="form-group">
									<input type="password" class="form-control input-lg"
										placeholder="Repeat Password'" name="password2" />
								</div>
								<div class="form-group">
									<button type="submit" class="btn btn-primary btn-lg btn-block"><spring:message code="register.register"/> </button>
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
			
			function init(){
				var txt = '<spring:message code="login.guest"/>';
				$("#guest_btn").val(txt)
			
			}
			
			$(document).ready(init);
			
			
			
			</script>



		</c:otherwise>
	</c:choose>
</body>
	</html>

</jsp:root>