package hu.zza.iotea.controller;

import static org.assertj.core.api.Assertions.assertThat;

import hu.zza.iotea.model.Device;
import hu.zza.iotea.model.dto.DeviceInput;
import hu.zza.iotea.model.dto.DeviceOutput;
import hu.zza.iotea.model.util.mapping.DeviceInputMapper;
import hu.zza.iotea.model.util.mapping.DeviceOutputMapper;
import hu.zza.iotea.repository.DeviceRepository;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.web.reactive.server.WebTestClient;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class DeviceControllerIT {
  @Autowired private DeviceRepository repository;
  @Autowired private WebTestClient client;
  @Autowired private DeviceInputMapper inMapper;
  @Autowired private DeviceOutputMapper outMapper;

  private IntegrationTestHelper<Integer, Device, DeviceInput, DeviceOutput> helper;

  @PostConstruct
  void initHelper() {
    helper =
        new IntegrationTestHelper<>(
            "devices",
            client,
            repository,
            inMapper,
            outMapper,
            DeviceOutput.class,
            this::createDummyDeviceInput);
  }

  @BeforeEach
  void setUp() {
    helper.cleanRepository();
  }

  DeviceInput createDummyDeviceInput(Integer i) {
    return createDummy("uid_" + i, "name_" + i, "127.0.0.1", i);
  }

  private DeviceInput createDummy(String uid, String name, String ip, Integer port) {
    var result = new DeviceInput();
    result.setUid(uid);
    result.setName(name);
    result.setIp(ip);
    result.setPort(port);
    return result;
  }

  @Test
  void getAllDevices() {
    var devices = helper.createDummyInputs(1, 3);
    helper.postInputs(devices);

    helper.updateMockOutputs(devices);

    helper.resultListCheckWithMockOutputs("/api/devices", 3);
  }

  @Test
  void getDevicesByIdentifier() {
    var d = helper.createDummyInputs(4, 6);
    var devices = new ArrayList<>(d);

    // Add "duplicates", but switch UID and name
    d.stream()
        .map(e -> createDummy(e.getName(), e.getUid(), "127.0.0.1", 80))
        .forEach(devices::add);

    helper.postInputs(devices);
    helper.updateMockOutputs(
        devices, o -> "uid_5".equals(o.getUid()) || "uid_5".equals(o.getName()));

    // There is two Device with 'uid_5' identifier: One with UID: 'uid_5', other with name: 'uid_5'
    helper.resultListCheckWithMockOutputs("/api/devices/uid_5", 2);
  }

  @Test
  void getDeviceById() {
    var devices = helper.createDummyInputs(7, 8);

    helper.postInputs(devices);

    // only the first element
    helper.updateMockOutputs(devices, o -> "uid_7".equals(o.getUid()));

    helper.updateIDsWithAllID();

    helper.resultCheckWithMockOutputs("/api/devices/id/" + helper.getIDs().get(0));
  }

  @Test
  void getDeviceByUid() {
    var devices = helper.createDummyInputs(9, 10);
    helper.postInputs(devices);

    helper.updateMockOutputs(devices, o -> "name_9".equals(o.getName()));
    helper.resultCheckWithMockOutputs("/api/devices/uid/uid_9");
  }

  @Test
  void getDeviceByName() {
    var devices = helper.createDummyInputs(11, 12);
    helper.postInputs(devices);

    helper.updateMockOutputs(devices, o -> "uid_11".equals(o.getUid()));
    helper.resultCheckWithMockOutputs("/api/devices/name/name_11");
  }

  @Test
  void getDevicesByIp() {
    var devices = helper.createDummyInputs(13, 15);
    helper.postInputs(devices);

    helper.updateMockOutputs(devices);
    helper.resultListCheckWithMockOutputs("/api/devices/ip/127.0.0.1", 3);
  }

  @Test
  void putDeviceToId() {
    var devices = helper.createDummyInputs(16, 18);
    // port equals dummy's ordinal 16, 17, 18
    helper.putInputs(devices, "id", DeviceInput::getPort);

    helper.updateIDsWithAllID();
    assertThat(helper.getIDs())
        .as("PUT .../id/16 .. 18 -> IDs = {16, 17, 18}")
        .isEqualTo(List.of(16, 17, 18));

    helper.updateMockOutputs(devices);
    helper.resultListCheckWithMockOutputs("/api/devices", 3);
  }

  @Test
  void putDeviceToUid() {
    var devices = helper.createDummyInputs(19, 21);
    // "uid_19" + "0" etc.
    helper.putInputs(devices, "uid", d -> d.getUid() + "0");

    // port equals dummy's ordinal 19, 20, 21
    devices.forEach(d -> d.setUid("uid_" + (d.getPort() * 10)));

    helper.updateMockOutputs(devices);
    helper.resultListCheckWithMockOutputs("/api/devices", 3);
  }

  @Test
  void putDeviceToName() {
    var devices = helper.createDummyInputs(22, 24);
    // port equals dummy's ordinal 22, 23, 24
    helper.putInputs(devices, "name", DeviceInput::getPort);

    // "name_22" -> "22"
    devices.forEach(d -> d.setName(d.getName().substring(5)));

    helper.updateMockOutputs(devices);
    helper.resultListCheckWithMockOutputs("/api/devices", 3);
  }

  @Test
  void deleteDeviceById() {
    var devices = helper.createDummyInputs(25, 27);
    helper.postInputs(devices);

    helper.updateMockOutputs(devices, o -> "uid_25".equals(o.getUid()));

    helper.updateIDsWithAllID();
    // delete all except the first : uid_25
    helper.deleteBy("id", helper.getIDs().get(1));
    helper.deleteBy("id", helper.getIDs().get(2));

    helper.resultListCheckWithMockOutputs("/api/devices", 1);
  }

  @Test
  void deleteDeviceByUid() {
    var devices = helper.createDummyInputs(28, 30);
    helper.postInputs(devices);

    helper.updateMockOutputs(devices, o -> "name_29".equals(o.getName()));

    helper.deleteBy("uid", "uid_28");
    // delete all except the second : uid_29
    helper.deleteBy("uid", "uid_30");

    helper.resultListCheckWithMockOutputs("/api/devices", 1);
  }

  @Test
  void deleteDeviceByName() {
    var devices = helper.createDummyInputs(31, 33);
    helper.postInputs(devices);

    helper.updateMockOutputs(devices, o -> "uid_33".equals(o.getUid()));

    helper.deleteBy("name", "name_31");
    helper.deleteBy("name", "name_32");
    // delete all except the third : name_33

    helper.resultListCheckWithMockOutputs("/api/devices", 1);
  }
}
