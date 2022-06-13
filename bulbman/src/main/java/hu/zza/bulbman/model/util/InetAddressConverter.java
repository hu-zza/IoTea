package hu.zza.bulbman.model.util;

import hu.zza.bulbman.model.DeviceAddress;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.StringJoiner;
import java.util.stream.Stream;
import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter
public class InetAddressConverter implements AttributeConverter<InetAddress, String> {

  @Override
  public String convertToDatabaseColumn(InetAddress inetAddress) {
    if (inetAddress == null) {
      return convertToDatabaseColumn(DeviceAddress.NULL_IP);
    }
    return stringify(inetAddress.getAddress());
  }

  private String stringify(byte[] address) {
    StringJoiner joiner = new StringJoiner(";");

    for (byte b : address) {
      joiner.add(String.valueOf(b));
    }
    return joiner.toString();
  }

  /**
   * @param address An IP address to parse. It can be in general form (which is parsable by
   *     InetAddress) or in a custom one. In the second case, the converter tries to split it by
   *     [^//d-] and then get the result from it.
   * @return InetAddress - the parsed address or loopback in case of invalid input
   */
  public InetAddress parse(String address) {
    try {
      return InetAddress.getByName(address);
    } catch (UnknownHostException ignored) {
    }
    return convertToEntityAttribute(address);
  }

  @Override
  public InetAddress convertToEntityAttribute(String address) {
    if (address == null) {
      return DeviceAddress.NULL_IP;
    }
    return tryToParse(address);
  }

  private InetAddress tryToParse(String address) {
    try {
      return InetAddress.getByAddress(ByteArrayUtil.getAsByteArray(getAsIntArray(address)));
    } catch (UnknownHostException exception) {
      // TODO: log...
      return DeviceAddress.NULL_IP;
    }
  }

  private int[] getAsIntArray(String address) {
    return Stream.of(address)
        .flatMap(string -> Arrays.stream(string.split("[^\\d-]+")))
        .mapToInt(Integer::parseInt)
        .toArray();
  }
}
