package ar.com.bia.controllers.services;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ar.com.bia.dto.SessionResurce;



@Service
public class SessionService {
	
	
	private @Autowired	HttpServletRequest request;
	
	public void addResource(String resourceName,String name,String id){
		if(request.getSession().getAttribute(resourceName) == null){
			request.getSession().setAttribute(resourceName, new SessionResurce(resourceName));
		}
		SessionResurce attribute = (SessionResurce)request.getSession().getAttribute(resourceName);
		attribute.put(name, id);
	}
	

	public void removeResource(String resourceName,String name){
		if(request.getSession().getAttribute(resourceName) == null){
			request.getSession().setAttribute(resourceName, new SessionResurce(resourceName));
		}
		SessionResurce attribute = (SessionResurce)request.getSession().getAttribute(resourceName);
		attribute.remove(name);
	}
	

	public SessionResurce proteins(){
	
		SessionResurce attribute = (SessionResurce)request.getSession().getAttribute("proteins");
		if(null == attribute){
			return new SessionResurce("proteins");
		}
		return attribute;
	}
	

	public SessionResurce queries(){
		return new SessionResurce();
	}
	
		
}
