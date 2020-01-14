package ar.com.bia.controllers;


import ar.com.bia.controllers.exceptions.ForbiddenException;
import org.aspectj.util.FileUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.security.Principal;

@Controller
@RequestMapping("/reactome_map")
public class ReactomeController {
    @Autowired
    private MongoOperations mongoTemplate;


    @RequestMapping(value = { "", "/" }, method = RequestMethod.GET, produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    @ResponseBody
    public byte[] downloadAnnotation( HttpServletResponse response,
                                     Principal principal) throws IOException {


        File file = new File("/data/databases/target/reactome_map.txt" );


        response.setContentType("application/txt");
        response.setHeader("Content-Disposition", "attachment; filename=reactome_map.txt");

        return FileUtil.readAsByteArray(file);
    }
}
