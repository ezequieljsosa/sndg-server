package ar.com.bia.aspects;

import java.security.Principal;
import java.util.Arrays;

import org.apache.log4j.Logger;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AuthorizationServiceException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

import ar.com.bia.backend.dao.GeneDocumentRepository;
import ar.com.bia.backend.dao.impl.UserRepositoryImpl;
import ar.com.bia.entity.GeneDoc;
import ar.com.bia.entity.UserDoc;

@Component
@Aspect
public class SecurityAspect {

	
	
	@Autowired
	private GeneDocumentRepository geneDocumentRepository;
	
	@Autowired
	private UserRepositoryImpl userRepository;
		
	
	@Pointcut("execution(* ar.com.bia.controllers.*.*(..))")
	public void secControllerMethods() {
	}

	@Around("secControllerMethods()")
	public Object validate(ProceedingJoinPoint joinPoint) throws Throwable {
//		final Logger logger = Logger.getLogger(joinPoint.getTarget().getClass()
//				.getName());
//		if(joinPoint.getThis().getClass().getName().equals("ar.com.bia.controllers.OntologyResourse"))
//			return;
		MethodSignature signature = (MethodSignature) joinPoint.getSignature();
		String[] parameterNames = signature.getParameterNames();
		Object[] args = joinPoint.getArgs();
		//org.jooq.lambda.Seq.seq
		int indexOfPrincipal =  Arrays.asList( parameterNames).indexOf("principal");
		if (indexOfPrincipal != -1){
			if (args[indexOfPrincipal] == null){
				Principal defaultPrincipal = new Principal() {
					
					@Override
					public String getName() {					
						return "demo";
					}
				};
				args[indexOfPrincipal] = defaultPrincipal;
			}
		}		
		Object proceed = joinPoint.proceed(args);
		return proceed;
		
	}

	private void validateGene(String geneId, Logger logger) {
		GeneDoc gene = this.geneDocumentRepository.findOne(geneId);
		if(gene==null){
			return;
		}
		this.validateSeqCollection(gene.strCollectionId(), logger);
		
	}

	private void validateSeqCollection(String genomeId, Logger logger) {
		User authenticatedUser = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
	    String name = authenticatedUser.getUsername();
	    UserDoc user = userRepository.findUser(name);
	    String msg = "El usuario " + name + " no esta utilizado a acceder a este genoma";
		if(user.getSeqCollections() == null ){
	    	logger.warn("El usuario " + name + " no tiene ningun genoma cargado");
	    	throw new AuthorizationServiceException(msg);
	    }
	    if(!user.getSeqCollections().contains(new ObjectId(genomeId))){
	    	logger.info(msg);
	    	throw new AuthorizationServiceException(msg);
	    }
	}

}
