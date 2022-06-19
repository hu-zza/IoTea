package hu.zza.iotea.service.connection;

import hu.zza.iotea.model.DeviceAddress;
import java.io.*;
import java.net.Socket;
import org.springframework.stereotype.Service;

@Service
public class TelnetSender implements Sender {

  public String send(DeviceAddress address, Integer port, String payload) {
    if (address.isReachable()) {
      try (var socket = new Socket(address.getIp(), port);
          var out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
          var in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

        return send(out, in, payload);

      } catch (Exception exception) {
        throw new SenderException(
            "Cannot send payload to the address '%s' (port: %d)".formatted(address.getIp(), port),
            exception);
      }
    }
    throw new SenderException(
        "Cannot send payload due to unreachable address",
        new IllegalStateException(
            "The address '%s' (port: %d) is unreachable".formatted(address.getIp(), port)));
  }

  private String send(BufferedWriter out, BufferedReader in, String payload) throws IOException {
    out.write(payload + "\r\n");
    out.flush();
    return in.readLine();
  }
}
