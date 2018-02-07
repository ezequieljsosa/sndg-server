package ar.com.bia.aspects;


import org.apache.log4j.Logger;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.util.StopWatch;

//@Component
//@Aspect
public class LoggingAspect {

	@Pointcut("execution(* ar.com.bia.controllers.services.*.*(..))")
    public void controllerMethods() { }
	
	@Around("controllerMethods()")
    public Object profile(ProceedingJoinPoint joinPoint) throws Throwable {
		final Logger logger = Logger.getLogger(joinPoint.getTarget().getClass().getName());
        Object retVal = null;

        try {
            StringBuffer startMessageStringBuffer = new StringBuffer();

            startMessageStringBuffer.append("Start method ");
            startMessageStringBuffer.append(joinPoint.getSignature().getName());
            startMessageStringBuffer.append("(");

            Object[] args = joinPoint.getArgs();
            for (int i = 0; i < args.length; i++) {
                startMessageStringBuffer.append(args[i]).append(",");
            }
            if (args.length > 0) {
                startMessageStringBuffer.deleteCharAt(startMessageStringBuffer.length() - 1);
            }

            startMessageStringBuffer.append(")");

            logger.trace(startMessageStringBuffer.toString());

            StopWatch stopWatch = new StopWatch();
            stopWatch.start();

            retVal = joinPoint.proceed();            retVal = joinPoint.proceed();            retVal = joinPoint.proceed();
          

            stopWatch.stop();

            StringBuffer endMessageStringBuffer = new StringBuffer();
            endMessageStringBuffer.append("Finish method ");
            endMessageStringBuffer.append(joinPoint.getSignature().getName());
            endMessageStringBuffer.append("(..); execution time: ");
            endMessageStringBuffer.append(stopWatch.getTotalTimeMillis());
            endMessageStringBuffer.append(" ms;");

            logger.trace(endMessageStringBuffer.toString());
        } catch (Throwable ex) {
            StringBuffer errorMessageStringBuffer = new StringBuffer();

             // Create error message 
             logger.error(errorMessageStringBuffer.toString(), ex);

            throw ex;
        }

        return retVal;
    }
	
}
