package hu.zza.iotea.controller;

import hu.zza.iotea.model.dto.DeviceInput;
import hu.zza.iotea.model.dto.DeviceOutput;
import hu.zza.iotea.model.util.mapping.DeviceInputMapper;
import hu.zza.iotea.model.util.mapping.DeviceOutputMapper;
import hu.zza.iotea.repository.DeviceRepository;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.IntStream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.web.reactive.server.EntityExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class DeviceControllerIT {
  @Autowired DeviceRepository repository;
  @Autowired WebTestClient client;

  @Autowired DeviceInputMapper inMapper;
  @Autowired DeviceOutputMapper outMapper;

  private DeviceOutput[] mockOutputs = new DeviceOutput[0];

  @BeforeEach
  void setUp() {
    repository.deleteAll();
  }

  private List<DeviceInput> createDummyDeviceInputs(int start, int end) {
    return IntStream.rangeClosed(start, end).mapToObj(this::createDummyDeviceInput).toList();
  }

  private DeviceInput createDummyDeviceInput(int i) {
    var result = new DeviceInput();
    result.setUid("uid_" + i);
    result.setName("name_" + i);
    result.setIp("127.0.0.1");
    result.setPort(80);
    return result;
  }

  private void postDevices(List<DeviceInput> devices) {
    devices.stream().forEach(d -> client.post().uri("/api/devices").bodyValue(d).exchange());
  }

  private void putDevices(
      List<DeviceInput> devices, String part, Function<DeviceInput, Object> partExtractor) {
    devices.forEach(
        d ->
            client
                .put()
                .uri("/api/devices/%s/%s".formatted(part, partExtractor.apply(d)))
                .bodyValue(d)
                .exchange());
  }

  private void updateMockOutputs(List<DeviceInput> devices) {
    mockOutputs = getOutputsFromInputs(devices).toArray(new DeviceOutput[0]);
  }

  private List<DeviceOutput> getOutputsFromInputs(List<DeviceInput> devices) {
    return outMapper.toDto(inMapper.toEntity(devices));
  }

  private void updateMockOutputsWithIDs(
      EntityExchangeResult<List<DeviceOutput>> entityExchangeResult) {
    var body = entityExchangeResult.getResponseBody();

    var IDs =
        Objects.requireNonNull(body, "entityExchangeResult is null").stream()
            .map(DeviceOutput::getId)
            .sorted()
            .toList();

    IntStream.range(0, mockOutputs.length).forEach(i -> mockOutputs[i].setId(IDs.get(i)));
  }

  @Test
  void getAllDevices() {
    var devices = createDummyDeviceInputs(1, 3);
    postDevices(devices);

    updateMockOutputs(devices);

    client
        .get()
        .uri("/api/devices")
        .exchange()
        .expectStatus()
        .isOk()
        .expectBodyList(DeviceOutput.class)
        .hasSize(3)
        .consumeWith(this::updateMockOutputsWithIDs)
        .contains(mockOutputs);
  }
/*
  @Test
  void getDevicesByIdentifier() {}

  @Test
  void getDeviceById() {}

  @Test
  void getDeviceByUid() {}

  @Test
  void getDeviceByName() {}

  @Test
  void getDevicesByIp() {}

  @Test
  void postDevice() {}

  @Test
  void putDeviceToId() {}

  @Test
  void putDeviceToUid() {}

  @Test
  void putDeviceToName() {}

  @Test
  void deleteDeviceById() {}

  @Test
  void deleteDeviceByUid() {}

  @Test
  void deleteDeviceByName() {}
 */
}
