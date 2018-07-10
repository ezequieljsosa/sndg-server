package ar.com.bia.config;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.security.web.savedrequest.DefaultSavedRequest;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Map;

@Component
public class AuthenticationSuccessHandler
		extends SavedRequestAwareAuthenticationSuccessHandler{

	private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

	
	
	public AuthenticationSuccessHandler() {
		super();

	}

	public boolean loginRedirect(String requestURL){
		return requestURL.endsWith("login") || requestURL.endsWith("login/") ||  requestURL.endsWith("patho/") ||  requestURL.endsWith("patho");  
	}

	@Override
	public void handle(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException{
		super.handle(request, response, authentication);
	}
	
	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException, ServletException {
		 
		
		HttpSession session = request.getSession();
		@SuppressWarnings("rawtypes")
		Map parameterMap = request.getParameterMap();
		
	        if (session != null) {
	        	DefaultSavedRequest redirectUrl = (DefaultSavedRequest) session.getAttribute("SPRING_SECURITY_SAVED_REQUEST");
	            if (redirectUrl != null) {
	                // then we redirect
	                String requestURL = redirectUrl.getRequestURL();
	                if(this.loginRedirect(requestURL)){
	                	requestURL = "/genome";
	                }
					getRedirectStrategy().sendRedirect(request, response, requestURL);
	                // we do not forget to clean this attribute from session
	                session.removeAttribute("SPRING_SECURITY_SAVED_REQUEST");

	                return;
	            }
		}	
	        if (parameterMap.containsKey("returnTo") &&
	        		!request.getParameter("returnTo").isEmpty()){
	        	
	        	
	    		Object object = parameterMap.get("returnTo");
	    		
	    		String[] strings = (String[]) object;
	    		
	        	
				getRedirectStrategy().sendRedirect(request, response, strings[0]);
			} else {
				redirectStrategy.sendRedirect(request, response, "/genome");		
			}
		
	}

}
