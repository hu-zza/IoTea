package hu.zza.iotea;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.zalando.problem.jackson.ProblemModule;
import org.zalando.problem.violations.ConstraintViolationProblemModule;

@SpringBootApplication
public class IoTeaApplication {
  public static void main(String[] args) {
    SpringApplication.run(IoTeaApplication.class, args);
  }

  @Bean
  public ObjectMapper objectMapper() {
    return new ObjectMapper()
        .registerModules(
            new ProblemModule(), new ConstraintViolationProblemModule(), new JavaTimeModule());
  }
}
