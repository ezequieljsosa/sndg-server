package ar.com.bia.aspects;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.ui.ModelMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Before advice for adding SiteMesh attributes to a request.
 *
 * @author bestguy
 * @since Mar 14, 2009 4:15:44 PM
 */
@Aspect
public class SiteMeshAspect {

    public static final String BASE = "base";
    public static final String BODY = "body";
    public static final String HEAD = "head";
    public static final String PAGE = "page";
    public static final String REQUEST = "request";
    public static final String RESPONSE = "response";
    public static final String TITLE = "title";

    @Before("execution(@org.springframework.web.bind.annotation.RequestMapping * *(..)) && args(request,response,model)")
    public void before(HttpServletRequest request, HttpServletResponse response, ModelMap model)
            throws Throwable {
//        HTMLPage page = (HTMLPage)request.getAttribute(RequestConstants.PAGE);
//
//        // TODO needed, or just filter out forwards/redirects instead?
//        if (model == null || page == null) {
//            return;
//        }
//
//        model.addAttribute(BASE, request.getContextPath());
//        model.addAttribute(REQUEST, request);
//        model.addAttribute(RESPONSE, response);
//
//        model.addAttribute(TITLE, OutputConverter.convert(page.getTitle()));
//
//        StringWriter buffer = new StringWriter();
//        page.writeBody(OutputConverter.getWriter(buffer));
//        model.addAttribute(BODY, buffer.toString());
//
//        buffer = new StringWriter();
//        page.writeHead(OutputConverter.getWriter(buffer));
//        model.addAttribute(HEAD, buffer.toString());
//
//        model.addAttribute(PAGE, page);
    }
}