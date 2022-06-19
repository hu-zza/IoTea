package hu.zza.iotea.model.util;

public interface Numbers {
  static Integer parseDatabaseIdIfPossible(String id) {
    try {
      int result = Integer.parseInt(id);
      return Math.max(-1, result);
    } catch (Exception ignored) {
    }
    return -1;
  }
}
