package hu.zza.iotea.model.util;

public interface NumberUtil {
  static byte[] getIntArrayAsByteArray(int[] integers) {
    byte[] result = new byte[integers.length];
    for (int i = 0; i < integers.length; i++) {
      result[i] = (byte) integers[i];
    }
    return result;
  }

  static Integer parseDatabaseIdIfPossible(String id) {
    try {
      int result = Integer.parseInt(id);
      return Math.max(-1, result);
    } catch (Exception ignored) {
    }
    return -1;
  }
}
