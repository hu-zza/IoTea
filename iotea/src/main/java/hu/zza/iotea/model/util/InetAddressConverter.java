package hu.zza.iotea.model.util;

import hu.zza.iotea.model.DeviceAddress;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.StringJoiner;
import java.util.stream.Stream;
import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Converter
public class InetAddressConverter implements AttributeConverter<InetAddress, String> {
  private final Logger logger = LoggerFactory.getLogger(InetAddressConverter.class);

  @Override
  public String convertToDatabaseColumn(InetAddress inetAddress) {
    if (inetAddress == null) {
      return convertToDatabaseColumn(DeviceAddress.NULL_IP);
    }
    return inetAddress.getHostAddress();
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
      return InetAddress.getByName(address);
    } catch (UnknownHostException | ConverterException exception) {
      logger.warn("Cannot parse IP address '%s'".formatted(address), exception);
      logger.warn("Returns with '{}' as null-safe placeholder", DeviceAddress.NULL_IP);
      return DeviceAddress.NULL_IP;
    }
  }
}
