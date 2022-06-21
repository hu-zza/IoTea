package hu.zza.iotea.model.exception;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.zalando.problem.spring.webflux.advice.ProblemHandling;

@ControllerAdvice
public class DefaultExceptionHandler implements ProblemHandling {}
