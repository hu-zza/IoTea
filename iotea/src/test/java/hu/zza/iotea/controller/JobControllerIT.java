package hu.zza.iotea.controller;

import static org.assertj.core.api.Assertions.assertThat;

import hu.zza.iotea.model.Job;
import hu.zza.iotea.model.dto.*;
import hu.zza.iotea.model.util.mapping.JobInputMapper;
import hu.zza.iotea.model.util.mapping.JobOutputMapper;
import hu.zza.iotea.repository.*;
import hu.zza.iotea.service.CommandService;
import hu.zza.iotea.service.DeviceService;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;
import javax.annotation.PostConstruct;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.web.reactive.server.WebTestClient;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class JobControllerIT {
  @Autowired private JobRepository repository;
  @Autowired private WebTestClient client;
  @Autowired private JobInputMapper inMapper;
  @Autowired private JobOutputMapper outMapper;

  @Autowired private DeviceService deviceService;
  @Autowired private CommandService commandService;
  @Autowired private DeviceRepository deviceRepository;
  @Autowired private CommandRepository commandRepository;
  private static boolean prepared = false;

  private IntegrationTestHelper<Integer, Job, JobInput, JobOutput> helper;

  @PostConstruct
  void initHelper() {
    helper =
        new IntegrationTestHelper<>(
            "jobs",
            client,
            repository,
            inMapper,
            outMapper,
            JobOutput.class,
            this::createDummyJobInput);
  }

  JobInput createDummyJobInput(Integer i) {
    return createDummy("name_" + i, i, i);
  }

  private JobInput createDummy(String name, Integer deviceId, Integer commandId) {
    var result = new JobInput();
    result.setName(name);
    result.setDeviceId(deviceId);
    result.setCommandId(commandId);
    return result;
  }

  @BeforeEach
  void setUp() {
    if (!prepared) {
      deviceRepository.deleteAll();
      IntStream.rangeClosed(1, 40)
          .mapToObj(i -> createDummyDevice("uid_" + i, "name_" + i, "127.0.0.1", i))
          .toList()
          .forEach(d -> deviceService.updateDevice(() -> Optional.of(d.getPort()), d));

      commandRepository.deleteAll();
      IntStream.rangeClosed(1, 40)
          .mapToObj(
              i ->
                  createDummyCommand(
                      "name_" + i, "{\"id\": %d, \"command\": \"%%s\"}".formatted(i), "note_" + i))
          .toList()
          .forEach(
              c ->
                  commandService.updateCommand(
                      () -> Optional.of(Integer.valueOf(c.getNote().substring(5))), c));

      prepared = true;
    }

    helper.cleanRepository();
  }

  private DeviceInput createDummyDevice(String uid, String name, String ip, Integer port) {
    var result = new DeviceInput();
    result.setUid(uid);
    result.setName(name);
    result.setIp(ip);
    result.setPort(port);
    return result;
  }

  private CommandInput createDummyCommand(String name, String template, String note) {
    var result = new CommandInput();
    result.setName(name);
    result.setTemplate(template);
    result.setNote(note);
    return result;
  }

  @Test
  void getAllJobs() {
    var jobs = helper.createDummyInputs(1, 3);
    helper.postInputs(jobs);

    helper.updateMockOutputs(jobs);

    helper.resultListCheckWithMockOutputs("/api/jobs", 3);
  }

  @Test
  @Disabled
  void getJobsByIdentifier() {}

  @Test
  void getJobById() {
    var jobs = helper.createDummyInputs(7, 8);

    helper.postInputs(jobs);

    // only the first element
    helper.updateMockOutputs(jobs, o -> "name_7".equals(o.getName()));

    helper.updateIDsWithAllID();

    helper.resultCheckWithMockOutputs("/api/jobs/id/" + helper.getIDs().get(0));
  }

  @Test
  void getJobByName() {
    var jobs = helper.createDummyInputs(11, 12);
    helper.postInputs(jobs);

    helper.updateMockOutputs(jobs, o -> "name_11".equals(o.getCommand().getName()));
    helper.resultCheckWithMockOutputs("/api/jobs/name/name_11");
  }

  @Test
  @Disabled
  void runJobById() {}

  @Test
  @Disabled
  void runJobByName() {}

  @Test
  void putJobToId() {
    var jobs = helper.createDummyInputs(16, 18);
    // "name_16" -> "16" -> 16
    helper.putInputs(jobs, "id", i -> Integer.valueOf(i.getName().substring(5)));

    helper.updateIDsWithAllID();
    assertThat(helper.getIDs())
        .as("PUT .../id/16 .. 18 -> IDs = {16, 17, 18}")
        .isEqualTo(List.of(16, 17, 18));

    helper.updateMockOutputs(jobs);
    helper.resultListCheckWithMockOutputs("/api/jobs", 3);
  }

  @Test
  void putJobToName() {
    var jobs = helper.createDummyInputs(22, 24);
    // "name_22" + "0" -> "name_220"
    helper.putInputs(jobs, "name", i -> i.getName() + "0");

    jobs.forEach(d -> d.setName(d.getName() + "0"));

    helper.updateMockOutputs(jobs);
    helper.resultListCheckWithMockOutputs("/api/jobs", 3);
  }

  @Test
  void deleteJobById() {
    var jobs = helper.createDummyInputs(25, 27);
    helper.postInputs(jobs);

    helper.updateMockOutputs(jobs, o -> "name_25".equals(o.getName()));

    helper.updateIDsWithAllID();
    // delete all except the first : name_25
    helper.deleteBy("id", helper.getIDs().get(1));
    helper.deleteBy("id", helper.getIDs().get(2));

    helper.resultListCheckWithMockOutputs("/api/jobs", 1);
  }

  @Test
  void deleteJobByName() {
    var jobs = helper.createDummyInputs(31, 33);
    helper.postInputs(jobs);

    helper.updateMockOutputs(jobs, o -> "name_33".equals(o.getCommand().getName()));

    helper.deleteBy("name", "name_31");
    helper.deleteBy("name", "name_32");
    // delete all except the third : name_33

    helper.resultListCheckWithMockOutputs("/api/jobs", 1);
  }
}
