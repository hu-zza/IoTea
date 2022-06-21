package hu.zza.iotea.controller;

import hu.zza.iotea.model.Job;
import hu.zza.iotea.model.dto.*;
import hu.zza.iotea.model.util.mapping.JobInputMapper;
import hu.zza.iotea.model.util.mapping.JobOutputMapper;
import hu.zza.iotea.repository.*;
import hu.zza.iotea.service.CommandService;
import hu.zza.iotea.service.DeviceService;
import java.util.Optional;
import java.util.stream.IntStream;
import javax.annotation.PostConstruct;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.web.reactive.server.WebTestClient;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class EasyJobControllerIT {
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

    helper.resultListCheckWithMockOutputs("/api", 3);
  }

  @Test
  void getJobsByIdentifier() {
    var j = helper.createDummyInputs(4, 6);

    helper.postInputs(j);
    helper.updateMockOutputs(j, o -> "name_5".equals(o.getName()));

    helper.resultListCheckWithMockOutputs("/api/name_5", 1);
  }

  @Test
  void getJobById() {
    var jobs = helper.createDummyInputs(7, 8);

    helper.postInputs(jobs);

    // only the first element
    helper.updateMockOutputs(jobs, o -> "name_7".equals(o.getName()));

    helper.updateIDsWithAllID();

    helper.resultCheckWithMockOutputs("/api/id/" + helper.getIDs().get(0));
  }

  @Test
  void getJobByName() {
    var jobs = helper.createDummyInputs(11, 12);
    helper.postInputs(jobs);

    helper.updateMockOutputs(jobs, o -> "name_11".equals(o.getCommand().getName()));
    helper.resultCheckWithMockOutputs("/api/name/name_11");
  }

  @Test
  void createJob() {
    client.get().uri("/api/name_5/name_10").exchange().expectStatus().isOk();
  }

  @Test
  void createNamedJob() {
    client.get().uri("/api/name_15/name_20/as/15-20").exchange().expectStatus().isOk();
  }

  @Test
  void setJobName() {
    var jobs = helper.createDummyInputs(20, 22);
    helper.postInputs(jobs);

    helper.updateIDsWithAllID();

    client
        .get()
        .uri("/api/call/%d/as/jobWithName".formatted(helper.getIDs().get(0)))
        .exchange()
        .expectStatus()
        .isOk();
  }

  @Test
  @Disabled
  void runJobByNameAlias() {}

  @Test
  @Disabled
  void testRunJobByNameAlias() {}

  @Test
  @Disabled
  void deleteJobByName() {}
}
