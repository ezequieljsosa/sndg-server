package ar.com.bia.config;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

import ar.com.bia.controllers.exceptions.ResourceNotFoundException;

//https://spring.io/blog/2013/11/01/exception-handling-in-spring-mvc
@ControllerAdvice
public class GlobalControllerExceptionHandler {
	
	public static final String DEFAULT_ERROR_VIEW = "error";
	
	@ExceptionHandler(value = ResourceNotFoundException.class)
	  public ModelAndView
	  resourceNotFoundExceptionHandler(HttpServletRequest req, Exception e) throws Exception {
	    

	    // Otherwise setup and send the user to a default error-view.
	    ModelAndView mav = new ModelAndView();
	    mav.addObject("exception", e);
	    mav.addObject("url", req.getRequestURL());
	    mav.setViewName(DEFAULT_ERROR_VIEW);
	    return mav;
	  }
	
	@ExceptionHandler(value = Exception.class)
	  public ModelAndView
	  defaultErrorHandler(HttpServletRequest req,HttpServletResponse response, Exception e) throws Exception {
	    // If the exception is annotated with @ResponseStatus rethrow it and let
	    // the framework handle it - like the OrderNotFoundException example
	    // at the start of this post.
	    // AnnotationUtils is a Spring Framework utility class.
	    if (AnnotationUtils.findAnnotation
	                (e.getClass(), ResponseStatus.class) != null)
	      throw e;
	    e.printStackTrace();
	    response.setStatus(500);
	    //TODO logguear error
	    // Otherwise setup and send the user to a default error-view.
	    ModelAndView mav = new ModelAndView();
	    mav.addObject("exception", e);
	    mav.addObject("url", req.getRequestURL());
	    mav.setViewName(DEFAULT_ERROR_VIEW);
	    return mav;
	  }
	
	
}
