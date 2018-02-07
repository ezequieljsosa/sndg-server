package ar.com.bia.controllers.services;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import ar.com.bia.dto.SessionResurce;



@Controller
@RequestMapping("/session")
public class SessionController {
	// get log4j handler
	private static final Logger logger = Logger.getLogger(SessionController.class);
		
	private @Autowired
	SessionService sessionService;

	
	
	@RequestMapping(value = "/", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody	
	public void addResource(String resourceName,String name,String id){
		logger.debug("adding [" + resourceName + "] resource to session");
		sessionService.addResource(resourceName, name, id);
	}
	
	@RequestMapping(value = "/", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody	
	public void removeResource(String resourceName,String name){
		sessionService.removeResource(resourceName, name);
	}
	
	@RequestMapping(value = "/proteins", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody	
	public SessionResurce proteins(){
	
		return sessionService.proteins();
	}
	
	@RequestMapping(value = "/queries", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public SessionResurce queries(){
		return new SessionResurce();
	}
	
		
}
