package hu.zza.iotea.model.exception;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.zalando.problem.spring.webflux.advice.ProblemHandling;

@ControllerAdvice
public class DefaultExceptionHandler implements ProblemHandling {

  @ExceptionHandler({DataIntegrityViolationException.class})
  public ResponseEntity<Problem> handleDataIntegrityViolation(
      DataIntegrityViolationException exception) {

    String messageChain = buildExceptionMessageChain(exception);
    return ResponseEntity.internalServerError().body(new ServiceProblem(messageChain));
  }

  private String buildExceptionMessageChain(DataIntegrityViolationException exception) {
    var msg = exception.getMessage();
    StringBuilder builder = new StringBuilder(msg == null ? "null" : msg);

    Throwable t = exception;

    while ((t = t.getCause()) != null) {
      builder.insert(0, "%s >>> ".formatted(t.getMessage()));
    }
    return builder.toString();
  }
}
