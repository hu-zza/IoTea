package hu.zza.iotea.controller;

import static org.assertj.core.api.Assertions.assertThat;

import hu.zza.iotea.model.dto.DeviceInput;
import hu.zza.iotea.model.dto.DeviceOutput;
import hu.zza.iotea.model.util.mapping.DeviceInputMapper;
import hu.zza.iotea.model.util.mapping.DeviceOutputMapper;
import hu.zza.iotea.repository.DeviceRepository;
import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
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
  private final List<Integer> IDs = new ArrayList<>();

  @BeforeEach
  void setUp() {
    repository.deleteAll();
  }

  private List<DeviceInput> createDummyDeviceInputs(int start, int end) {
    return IntStream.rangeClosed(start, end).mapToObj(this::createDummyDeviceInput).toList();
  }

  private DeviceInput createDummyDeviceInput(int i) {
    return createDummyDeviceInput("uid_" + i, "name_" + i, "127.0.0.1", i);
  }

  private DeviceInput createDummyDeviceInput(String uid, String name, String ip, Integer port) {
    var result = new DeviceInput();
    result.setUid(uid);
    result.setName(name);
    result.setIp(ip);
    result.setPort(port);
    return result;
  }

  private void postDevices(List<DeviceInput> devices) {
    devices.forEach(d -> client.post().uri("/api/devices").bodyValue(d).exchange());
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
    updateMockOutputs(devices, o -> true);
  }

  private void updateMockOutputs(List<DeviceInput> devices, Predicate<DeviceOutput> filter) {
    mockOutputs =
        getOutputsFromInputs(devices).stream().filter(filter).toArray(DeviceOutput[]::new);
  }

  private List<DeviceOutput> getOutputsFromInputs(List<DeviceInput> devices) {
    return outMapper.toDto(inMapper.toEntity(devices));
  }

  private void resultListCheckWithMockOutputs(String uri, int size) {
    client
        .get()
        .uri(uri)
        .exchange()
        .expectStatus()
        .isOk()
        .expectBodyList(DeviceOutput.class)
        .hasSize(size)
        .consumeWith(this::updateMockOutputsWithIDs)
        .contains(mockOutputs);
  }

  private void updateMockOutputsWithIDs(
      EntityExchangeResult<List<DeviceOutput>> entityExchangeResult) {
    var body = entityExchangeResult.getResponseBody();

    IDs.clear();
    Objects.requireNonNull(body, "entityExchangeResult is null").stream()
        .map(DeviceOutput::getId)
        .sorted()
        .forEach(IDs::add);

    assertThat(IDs.size()).as("DB IDs.length == mockOutputs.length").isEqualTo(mockOutputs.length);
    IntStream.range(0, mockOutputs.length).forEach(i -> mockOutputs[i].setId(IDs.get(i)));
  }

  private void resultCheckWithMockOutputs(String uri) {
    client
        .get()
        .uri(uri)
        .exchange()
        .expectStatus()
        .isOk()
        .expectBody(DeviceOutput.class)
        .consumeWith(this::updateSingleMockOutputWithID)
        .isEqualTo(mockOutputs[0]);
  }

  private void updateSingleMockOutputWithID(
      EntityExchangeResult<DeviceOutput> entityExchangeResult) {
    var body = entityExchangeResult.getResponseBody();

    IDs.clear();
    IDs.add(Objects.requireNonNull(body, "entityExchangeResult is null").getId());

    assertThat(IDs.size())
        .as("DB IDs.size() == mockOutputs.length")
        .isEqualTo(mockOutputs.length)
        .as("DB IDs.size() == 1  && mockOutputs.length == 1")
        .isEqualTo(1);
    IntStream.range(0, mockOutputs.length).forEach(i -> mockOutputs[i].setId(IDs.get(i)));
  }

  private void updateIDsWithAllID() {
    client
        .get()
        .uri("/api/devices")
        .exchange()
        .expectBodyList(DeviceOutput.class)
        .consumeWith(
            list -> {
              IDs.clear();
              list.getResponseBody().stream().map(DeviceOutput::getId).sorted().forEach(IDs::add);
            });
  }

  private void deleteBy(String part, Object identifier) {
    client.delete().uri("/api/devices/%s/%s".formatted(part, identifier)).exchange();
  }

  @Test
  void getAllDevices() {
    var devices = createDummyDeviceInputs(1, 3);
    postDevices(devices);

    updateMockOutputs(devices);

    resultListCheckWithMockOutputs("/api/devices", 3);
  }

  @Test
  void getDevicesByIdentifier() {
    var d = createDummyDeviceInputs(4, 6);
    var devices = new ArrayList<>(d);

    // Add "duplicates", but switch UID and name
    d.stream()
        .map(e -> createDummyDeviceInput(e.getName(), e.getUid(), "127.0.0.1", 80))
        .forEach(devices::add);

    postDevices(devices);
    updateMockOutputs(devices, o -> "uid_5".equals(o.getUid()) || "uid_5".equals(o.getName()));

    // There is two Device with 'uid_5' identifier: One with UID: 'uid_5', other with name: 'uid_5'
    resultListCheckWithMockOutputs("/api/devices/uid_5", 2);
  }

  @Test
  void getDeviceById() {
    var devices = createDummyDeviceInputs(7, 8);

    postDevices(devices);

    // only the first element
    updateMockOutputs(devices, o -> "uid_7".equals(o.getUid()));

    updateIDsWithAllID();

    resultCheckWithMockOutputs("/api/devices/id/" + IDs.get(0));
  }

  @Test
  void getDeviceByUid() {
    var devices = createDummyDeviceInputs(9, 10);
    postDevices(devices);

    updateMockOutputs(devices, o -> "name_9".equals(o.getName()));
    resultCheckWithMockOutputs("/api/devices/uid/uid_9");
  }

  @Test
  void getDeviceByName() {
    var devices = createDummyDeviceInputs(11, 12);
    postDevices(devices);

    updateMockOutputs(devices, o -> "uid_11".equals(o.getUid()));
    resultCheckWithMockOutputs("/api/devices/name/name_11");
  }

  @Test
  void getDevicesByIp() {
    var devices = createDummyDeviceInputs(13, 15);
    postDevices(devices);

    updateMockOutputs(devices);
    resultListCheckWithMockOutputs("/api/devices/ip/127.0.0.1", 3);
  }

  @Test
  void putDeviceToId() {
    var devices = createDummyDeviceInputs(16, 18);
    // port equals dummy's ordinal 16, 17, 18
    putDevices(devices, "id", DeviceInput::getPort);

    updateIDsWithAllID();
    assertThat(IDs).as("PUT .../id/16 .. 18 -> IDs = {16, 17, 18}").isEqualTo(List.of(16, 17, 18));

    updateMockOutputs(devices);
    resultListCheckWithMockOutputs("/api/devices", 3);
  }

  @Test
  void putDeviceToUid() {
    var devices = createDummyDeviceInputs(19, 21);
    // "uid_19" + "0" etc.
    putDevices(devices, "uid", d -> d.getUid() + "0");

    // port equals dummy's ordinal 19, 20, 21
    devices.forEach(d -> d.setUid("uid_" + (d.getPort() * 10)));

    updateMockOutputs(devices);
    resultListCheckWithMockOutputs("/api/devices", 3);
  }

  @Test
  void putDeviceToName() {
    var devices = createDummyDeviceInputs(22, 24);
    // port equals dummy's ordinal 22, 23, 24
    putDevices(devices, "name", DeviceInput::getPort);

    // "name_22" -> "22"
    devices.forEach(d -> d.setName(d.getName().substring(5)));

    updateMockOutputs(devices);
    resultListCheckWithMockOutputs("/api/devices", 3);
  }

  @Test
  void deleteDeviceById() {
    var devices = createDummyDeviceInputs(25, 27);
    postDevices(devices);

    updateMockOutputs(devices, o -> "uid_25".equals(o.getUid()));

    updateIDsWithAllID();
    // delete all except the first : uid_25
    deleteBy("id", IDs.get(1));
    deleteBy("id", IDs.get(2));

    resultListCheckWithMockOutputs("/api/devices", 1);
  }

  @Test
  void deleteDeviceByUid() {
    var devices = createDummyDeviceInputs(28, 30);
    postDevices(devices);

    updateMockOutputs(devices, o -> "name_29".equals(o.getName()));

    deleteBy("uid", "uid_28");
    // delete all except the second : uid_29
    deleteBy("uid", "uid_30");

    resultListCheckWithMockOutputs("/api/devices", 1);
  }

  @Test
  void deleteDeviceByName() {
    var devices = createDummyDeviceInputs(31, 33);
    postDevices(devices);

    updateMockOutputs(devices, o -> "uid_33".equals(o.getUid()));

    deleteBy("name", "name_31");
    deleteBy("name", "name_32");
    // delete all except the third : name_33

    resultListCheckWithMockOutputs("/api/devices", 1);
  }
}
