package org.gene.exceptionHandler;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
//import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.http.HttpStatus;


@Component
// 메소드에 "@ExceptionHandler" 어노테이션을 사용하려면 클래스에 "@ControllerAdvice" 어노테이션을 붙여줘야 한다.
// http://spring.io/blog/2013/11/01/exception-handling-in-spring-mvc
@ControllerAdvice
public class CustomExceptionHandler
{
//	@ExceptionHandler(NoHandlerFoundException.class)
//	or
//	@ExceptionHandler
//	@ResponseStatus(HttpStatus.NOT_FOUND)

	@ExceptionHandler
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public String handle404(Exception ex, HttpServletRequest request)
	{
		System.out.println("!!!!!!!!!!!!!!!!!!!!!!!handle404!!!!!!!!!!!!!!!!!!!!!!!");
		System.out.println(request.getRequestURI());
		System.out.println(ex.getMessage());
		return "404";
	}

	@ExceptionHandler(Throwable.class)
	public String handleDefault(Exception ex, HttpServletRequest request)
	{
		System.out.println("!!!!!!!!!!!!!!!!!!!handleDefault!!!!!!!!!!!!!!!!!!!!!!!!!!!");
		System.out.println(request.getRequestURI());
		System.out.println(ex.getMessage());
		return "404";
	}
}







// http://stackoverflow.com/questions/23574869/404-error-redirect-in-spring-with-java-config
// http://www.javabeat.net/spring-mvc-404-error-page/
// http://www.jayway.com/2013/02/03/improve-your-spring-rest-api-part-iii/
// http://stackoverflow.com/questions/9909806/spring-mvc-exceptionhandler-method-in-controller-never-being-invoked




// http://www.jayway.com/2013/02/03/improve-your-spring-rest-api-part-iii/
// http://stackoverflow.com/questions/18322279/spring-mvc-spring-security-and-error-handling
// http://stackoverflow.com/questions/2066946/trigger-404-in-spring-mvc-controller
// http://www.mkyong.com/spring-mvc/404-error-code-is-not-working-in-spring-mvc/
// http://stackoverflow.com/questions/23574869/404-error-redirect-in-spring-with-java-config
// http://www.mkyong.com/spring-mvc/spring-mvc-exceptionhandler-example/