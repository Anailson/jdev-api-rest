package curso.api.rest;

import java.sql.SQLException;
import java.util.List;

import org.hibernate.exception.ConstraintViolationException;
import org.postgresql.util.PSQLException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
@ControllerAdvice
public class ControleExcecoes extends ResponseEntityExceptionHandler{
	
	@ExceptionHandler({Exception.class, RuntimeException.class, Throwable.class})/*PEGANDO TODAS AS MSGS DE ERRO .class*/
	@Override
	protected ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body, HttpHeaders headers,
			HttpStatus status, WebRequest request) {
		
		String msg = "";
		
		/*MONTANDO A MSG DE ERRO*/
		if (ex instanceof MethodArgumentNotValidException) {
			List<ObjectError> list = ((MethodArgumentNotValidException) ex).getBindingResult().getAllErrors();
			for( ObjectError objectError : list) {
				msg += objectError.getDefaultMessage() + "\n";
			}
		} else {
			msg = ex.getMessage();
		}
		
		/*MONTANDO O OBJETO DE ERRO SERÁ EM JSON*/
		ObjetoErro objetoErro = new ObjetoErro();
		objetoErro.setError(msg);
		objetoErro.setCode(status.value() + " ==> " + status.getReasonPhrase());
		
		return new ResponseEntity<>(objetoErro, headers, status);
	}
	/*ERROS QUE VEM DO BANCO DE DADOS COMO INSERT POR EXEMPLO*/
	/*TRATAMENTO DA MAIORIA DOS ERROS A NIVEL DE BANCO DE DADOS*/
	@ExceptionHandler({DataIntegrityViolationException.class, ConstraintViolationException.class, PSQLException.class, SQLException.class})
	 protected ResponseEntity<Object> handleExceptionDataIntegry(Exception ex){
		
		/*CONSTRUINDO A MSG DE ERRO*/
		String msg  = "";
		
		
		/*MSG ESPECIFICA*/
		if (ex instanceof DataIntegrityViolationException) {
			msg = ((DataIntegrityViolationException) ex).getCause().getCause().getMessage();
		} else if(ex instanceof ConstraintViolationException){
			msg = ((ConstraintViolationException) ex).getCause().getCause().getMessage();
		}
		else if(ex instanceof PSQLException) {
			msg = ((PSQLException) ex).getCause().getCause().getMessage();
		}
		else if(ex instanceof SQLException) {
			msg = ((SQLException) ex).getCause().getCause().getMessage();
		}
		else {
			msg = ex.getMessage();/*OUTRAS MSG DE ERROS*/
		}
			
		ObjetoErro objectError = new ObjetoErro();
		objectError.setError(msg);
		objectError.setCode(HttpStatus.INTERNAL_SERVER_ERROR + " ==> " + HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
		//MOSTRA A MSG DE ERRO MAIS VISIVEIS
		
		return new ResponseEntity<>(objectError, HttpStatus.INTERNAL_SERVER_ERROR);
		 
	 }
	
}
