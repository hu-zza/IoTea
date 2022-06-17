package hu.zza.iotea.service.connection;

public class SenderException extends RuntimeException {
  public SenderException(String message, Throwable cause) {
    super(message, cause);
  }
}
