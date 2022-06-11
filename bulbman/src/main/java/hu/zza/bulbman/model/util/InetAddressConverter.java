package hu.zza.bulbman.model.util;

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
      return convertToDatabaseColumn(InetAddress.getLoopbackAddress());
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

  @Override
  public InetAddress convertToEntityAttribute(String address) {
    if (address == null) {
      return InetAddress.getLoopbackAddress();
    }
    return parse(address);
  }

  private InetAddress parse(String address) {
    try {
      return InetAddress.getByAddress(
          ByteArrayUtil.getAsByteArray(
              Stream.of(address)
                  .flatMap(string -> Arrays.stream(string.split(";")))
                  .mapToInt(Integer::parseInt)
                  .toArray()));
    } catch (UnknownHostException exception) {
      // TODO: log...
      return InetAddress.getLoopbackAddress();
    }
  }
}
