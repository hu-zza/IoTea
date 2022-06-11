package hu.zza.bulbman.model.util;

public interface ByteArrayUtil {
  static byte[] getAsByteArray(int[] integers) {
    byte[] result = new byte[integers.length];
    for (int i = 0; i < integers.length; i++) {
      result[i] = (byte) integers[i];
    }
    return result;
  }
}
