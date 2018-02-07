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


<title>XomeQ <spring:message code="login.login"/></title>

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



<h1>Error Page</h1>
  <p>Application has encountered an error. Do you want to leave your contact, describe what you were doing and report the error?</p>
    
   <form method="POST" action="${baseURL}/user/issue/">
   Failed URL: ${url}<br />
  	Your mail: <input name="email" type="email" /><br />
    <b>Error Description:</b><br /> 
    <textarea name="description" rows="15" cols="100">-</textarea><br />
    (don't be shy, it helps a lot!)
    <input type="submit"  value="Report Error" />
    
    <input type="hidden" name="exception" value="${exception.message}" />
    <textarea  style="display:None" name="stackTrace">
    
    ${exception.message}
        <c:forEach items="${exception.stackTrace}" var="ste">    ${ste} 
    </c:forEach>
    
    </textarea>
    
    </form>
   
  
			
		



	
</body>
	</html>

</jsp:root>