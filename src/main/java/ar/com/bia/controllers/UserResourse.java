package ar.com.bia.controllers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.security.Principal;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.Credentials;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.DefaultHttpClient;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.mongodb.BasicDBObject;

import ar.com.bia.DataTablesUtils;
import ar.com.bia.backend.dao.impl.JobsRepositoryImpl;
import ar.com.bia.backend.dao.impl.ProjectRepositoryImpl;
import ar.com.bia.backend.dao.impl.UserRepositoryImpl;
import ar.com.bia.dto.UserDTO;
import ar.com.bia.entity.JobDoc;
import ar.com.bia.entity.ProjectDoc;
import ar.com.bia.entity.SeqCollectionDoc;
import ar.com.bia.entity.UserDoc;
import ar.com.bia.services.UserService;

@Controller
@RequestMapping("/user")
public class UserResourse {

	@Autowired
	private MongoOperations mongoTemplate;

	@Autowired
	private UserRepositoryImpl userRepository;
	
	@Autowired
	private JobsRepositoryImpl jobsRepo;

	@Autowired
	private ProjectRepositoryImpl projectRepository;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private AuthenticationManager authenticationManager;
		
	private @Autowired DataTablesUtils dataTablesUtils;

	@RequestMapping(value = { "/" , ""}, method = RequestMethod.GET,
			produces = MediaType.APPLICATION_XHTML_XML_VALUE)
	public String dashboard( Model model, Principal principal) {
		
		UserDoc user = this.userRepository.findUser(principal.getName());
		int projectCount = this.projectRepository.projects_from_user(
				user.getId()).size();
		List<ObjectId> auth =  this.dataTablesUtils.authCriteria(principal);
		
		long genomeCount =  mongoTemplate.count(new Query( Criteria.where("auth").in(auth) ), SeqCollectionDoc.class); ;
		
		
		model.addAttribute("user", principal);
		model.addAttribute("genomeCount", genomeCount);
		model.addAttribute("projectCount", projectCount);
		
		
		
		return "user/Dashboard";
	}
	
	@RequestMapping(value = { "/" , ""}, method = RequestMethod.POST)
	public String addUser(
			Model model,@ModelAttribute UserDTO userDTO,Principal principal) {
		
		String qpReturnTo = (userDTO.getReturnTo().isEmpty()) ? "" : "?_return=" +  userDTO.getReturnTo();
		UserDoc user = this.userRepository.findUser(userDTO.getUsername());
		if (user != null ){
			model.addAttribute("error", "user already exists" );
			return "redirect:/user/register" + qpReturnTo;
		}
		if (!userDTO.getPassword().equals(userDTO.getPassword2())){
			model.addAttribute("error", "passwords do not match" );
			
			return "redirect:/user/register" + qpReturnTo;
		}
		if (userDTO.getUsername().trim().isEmpty()){
			model.addAttribute("error", "username is mandatoty" );
			
			return "redirect:/user/register" + qpReturnTo;
		}
		if (userDTO.getPassword().trim().isEmpty()){
			model.addAttribute("error", "password is mandatoty" );
			return "redirect:/user/register" + qpReturnTo;
		}
		
		user = new UserDoc();
		user.setEmail(userDTO.getEmail());
		user.setUsername(userDTO.getUsername());
		user.setName(userDTO.getUsername());
		user.setPassword(userDTO.getPassword());
		user.setInstitutions(userDTO.getInstitutions());
		
		
		this.userRepository.save(user)	;
		
		
        Authentication request = new UsernamePasswordAuthenticationToken( user.getName(), user.getPassword() );
    Authentication result = authenticationManager.authenticate( request );
    SecurityContextHolder.getContext().setAuthentication( result );
		return "redirect:"  + (( userDTO.getReturnTo().isEmpty() ) ? "/genome/" : userDTO.getReturnTo());
	}
	
	@RequestMapping(value = "/register", method = RequestMethod.GET,
			produces = MediaType.APPLICATION_XHTML_XML_VALUE)
	public String register( Model model, Principal principal) {
		
		
		
		
		return "user/register";
	}
	
	
	@RequestMapping(value = "tutorial", method = RequestMethod.GET,
			produces = MediaType.APPLICATION_XHTML_XML_VALUE)
	public String tutorial( Model model, Principal principal) {		
		model.addAttribute("user", principal);
		return "user/tutorial";
	}
	@RequestMapping(value = "test", method = RequestMethod.GET,
			produces = MediaType.APPLICATION_XHTML_XML_VALUE)
	public String test( Model model, Principal principal) {		
		model.addAttribute("user", principal);
		return "user/test";
	}
	
	@RequestMapping(value = "about", method = RequestMethod.GET,
			produces = MediaType.APPLICATION_XHTML_XML_VALUE)
	public String about( Model model, Principal principal) {		
		model.addAttribute("user", principal);
		return "user/about";
	}
	@RequestMapping(value = "methodology", method = RequestMethod.GET,
			produces = MediaType.APPLICATION_XHTML_XML_VALUE)
	public String methodology( Model model, Principal principal) {		
		model.addAttribute("user", principal);
		return "user/methodology";
	}
	
	@RequestMapping(value = "user_guide", method = RequestMethod.GET,
			produces = MediaType.APPLICATION_XHTML_XML_VALUE)
	public String user_guide( Model model, Principal principal) {		
		model.addAttribute("user", principal);
		return "user/user_guide";
	}
	
	@RequestMapping(value = "main", method = RequestMethod.GET,
			produces = MediaType.APPLICATION_XHTML_XML_VALUE)
	public String main( Model model, Principal principal) {		
		model.addAttribute("user", principal);
		return "user/main";
	}
	
	
	@RequestMapping(value = "/project/", method = RequestMethod.GET,
			produces = MediaType.APPLICATION_XHTML_XML_VALUE)
	public String projectsPAge( Model model, Principal principal) {
		model.addAttribute("user", principal);
		return "user/ProjectList";
	}
	
	@RequestMapping(value = "/issue/", method = RequestMethod.POST)
	public String addIssue( HttpServletRequest request, Model model, Principal principal) throws ClientProtocolException, IOException {
		//https://www.mkyong.com/webservices/jax-rs/restful-java-client-with-apache-httpclient/
		
		DefaultHttpClient httpClient = new DefaultHttpClient();


		CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
		Credentials credentials = new UsernamePasswordCredentials("esosa", "a7674009");
		credentialsProvider.setCredentials(AuthScope.ANY,credentials);
		
		httpClient.setCredentialsProvider(credentialsProvider);
		
		String description =request.getParameter("email").replaceAll("\"", "'") + " -- "
				+ request.getParameter("description").replaceAll("\"", "'") + " -- "    
				+ request.getParameter("exception").replaceAll("\"", "'") + " -- "
				+ Arrays.asList(request.getParameter("stackTrace").replaceAll("\"", "'").split("\n")).stream().map(x -> x.trim()).collect(Collectors.joining(", ")) + " -- ";
		System.out.println(description);
		HttpPost postRequest = new HttpPost(
			"http://arkham.exp.dc.uba.ar/phoenix/issues.json");
		String date = new SimpleDateFormat("yyyy.MM.dd  'at' HH:mm:ss z").format(new Date());
		StringEntity input = new StringEntity("{\"issue\":{\"project_id\":\"2\",\"subject\":\"Reported Error " 
							+ date + " \",\"description\":\"" + description + "\",\"priority_id\": \"4\"}}");
		input.setContentType("application/json");
		postRequest.setEntity(input);

		HttpResponse response = httpClient.execute(postRequest);
		
		if (response.getStatusLine().getStatusCode() != 201) {
			//TODO logguear
			throw new RuntimeException("Failed : HTTP error code : "
				+ response.getStatusLine().getStatusCode());
		}

		BufferedReader br = new BufferedReader(
                        new InputStreamReader((response.getEntity().getContent())));

		String output;
		System.out.println("Output from Server .... \n");
		while ((output = br.readLine()) != null) {
			//TODO logguear
			System.out.println(output);
		}

		httpClient.getConnectionManager().shutdown();
		
		
		model.addAttribute("user", principal);
		return "redirect:/genome/";
	}
	
	
	@RequestMapping(value = "/{username}", method = RequestMethod.GET,
			produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public UserDoc user(@PathVariable("username") String username) {
		UserDoc user = this.userRepository.findUser(username);
//		user.setProjectCount(this.projectRepository.projects_from_user(
//				user.getId()).size());
		return user;
	}

	
	
	@RequestMapping(value = "/{username}/project", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public List<ProjectDoc> projects(@PathVariable("username") String username) {
		UserDoc user = this.userRepository.findUser(username);
		return this.projectRepository.projects_from_user(user.getId());
	}

	@RequestMapping(value = "/organism", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public List<String> organisms(Principal principal) {
		UserDoc user = this.userRepository.findUser(principal.getName());		
		return  this.userService.organisms(user);
	}
	
	@RequestMapping(value = "genomes", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public List<SeqCollectionDoc> genomeList(
			@RequestParam(value = "length", defaultValue = "10") Integer perPage,
			@RequestParam(value = "start", defaultValue = "0") Integer offset,
			@RequestParam(value = "search[value]", defaultValue = "") String search) {

		User authenticatedUser = (User) SecurityContextHolder.getContext()
				.getAuthentication().getPrincipal();
		String name = authenticatedUser.getUsername();
		
		List<ObjectId> userGenomes = userRepository.findUser(name)
				.getSeqCollections();
		
		List<SeqCollectionDoc> genomeList = this.mongoTemplate.find(new Query(Criteria.where("_id").in(userGenomes)),
				SeqCollectionDoc.class);
		
		return genomeList;
	}
	
	@RequestMapping(value = "jobs", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public List<JobDoc> jobList(Principal principal) {

		String name = principal.getName();
		
		
		
		return this.jobsRepo.findAll(new Query(Criteria.where("user").is(name).and("type").is("blastp")).with(new Sort(Direction.DESC,"date")) );
	}
	
	// @RequestMapping(value = "/{username}/links", method = RequestMethod.GET,
	// produces = MediaType.APPLICATION_JSON_VALUE)
	// @ResponseBody
	// public UserDoc links(@PathVariable("username") String username) {
	// return this.userRepository.findUser(username);
	// }
	//
	// @RequestMapping(value = "/{username}/clipboard", method =
	// RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	// @ResponseBody
	// public UserDoc clipboard(@PathVariable("username") String username) {
	// return this.userRepository.findUser(username);
	// }
	//
	// @RequestMapping(value = "/{username}/menues", method = RequestMethod.GET,
	// produces = MediaType.APPLICATION_JSON_VALUE)
	// @ResponseBody
	// public UserDoc menues(@PathVariable("username") String username) {
	// return this.userRepository.findUser(username);
	// }

}
