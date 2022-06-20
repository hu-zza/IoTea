package hu.zza.iotea.controller;

import static org.assertj.core.api.Assertions.assertThat;

import hu.zza.iotea.model.Command;
import hu.zza.iotea.model.dto.CommandInput;
import hu.zza.iotea.model.dto.CommandOutput;
import hu.zza.iotea.model.util.mapping.CommandInputMapper;
import hu.zza.iotea.model.util.mapping.CommandOutputMapper;
import hu.zza.iotea.repository.CommandRepository;
import java.util.List;
import javax.annotation.PostConstruct;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.web.reactive.server.WebTestClient;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class CommandControllerIT {
  @Autowired private CommandRepository repository;
  @Autowired private WebTestClient client;
  @Autowired private CommandInputMapper inMapper;
  @Autowired private CommandOutputMapper outMapper;

  private IntegrationTestHelper<Integer, Command, CommandInput, CommandOutput> helper;

  @PostConstruct
  void initHelper() {
    helper =
        new IntegrationTestHelper<>(
            "commands",
            client,
            repository,
            inMapper,
            outMapper,
            CommandOutput.class,
            this::createDummyCommandInput);
  }

  CommandInput createDummyCommandInput(Integer i) {
    return createDummy("name_" + i, "template_" + i, i.toString());
  }

  private CommandInput createDummy(String name, String template, String note) {
    var result = new CommandInput();
    result.setName(name);
    result.setTemplate(template);
    result.setNote(note);
    return result;
  }

  @BeforeEach
  void setUp() {
    repository.deleteAll();
  }

  @Test
  void getAllCommands() {
    var commands = helper.createDummyInputs(1, 3);
    helper.postInputs(commands);

    helper.updateMockOutputs(commands);

    helper.resultListCheckWithMockOutputs("/api/commands", 3);
  }

  @Test
  @Disabled
  void getCommandsByIdentifier() {}

  @Test
  void getCommandById() {
    var commands = helper.createDummyInputs(7, 8);

    helper.postInputs(commands);

    // only the first element
    helper.updateMockOutputs(commands, o -> "name_7".equals(o.getName()));

    helper.updateIDsWithAllID();

    helper.resultCheckWithMockOutputs("/api/commands/id/" + helper.getIDs().get(0));
  }

  @Test
  void getCommandByName() {
    var commands = helper.createDummyInputs(11, 12);
    helper.postInputs(commands);

    helper.updateMockOutputs(commands, o -> "11".equals(o.getNote()));
    helper.resultCheckWithMockOutputs("/api/commands/name/name_11");
  }

  @Test
  void putCommandToId() {
    var commands = helper.createDummyInputs(16, 18);
    // note equals dummy's ordinal 16, 17, 18
    helper.putInputs(commands, "id", c -> Integer.valueOf(c.getNote()));

    helper.updateIDsWithAllID();
    assertThat(helper.getIDs())
        .as("PUT .../id/16 .. 18 -> IDs = {16, 17, 18}")
        .isEqualTo(List.of(16, 17, 18));

    helper.updateMockOutputs(commands);
    helper.resultListCheckWithMockOutputs("/api/commands", 3);
  }

  @Test
  void putCommandToName() {
    var commands = helper.createDummyInputs(22, 24);
    // note equals dummy's ordinal 22, 23, 24
    helper.putInputs(commands, "name", CommandInput::getNote);

    // "name_22" -> "22"
    commands.forEach(d -> d.setName(d.getName().substring(5)));

    helper.updateMockOutputs(commands);
    helper.resultListCheckWithMockOutputs("/api/commands", 3);
  }

  @Test
  void deleteCommandById() {
    var commands = helper.createDummyInputs(25, 27);
    helper.postInputs(commands);

    helper.updateMockOutputs(commands, o -> "name_25".equals(o.getName()));

    helper.updateIDsWithAllID();
    // delete all except the first : name_25
    helper.deleteBy("id", helper.getIDs().get(1));
    helper.deleteBy("id", helper.getIDs().get(2));

    helper.resultListCheckWithMockOutputs("/api/commands", 1);
  }

  @Test
  void deleteCommandByName() {
    var commands = helper.createDummyInputs(31, 33);
    helper.postInputs(commands);

    helper.updateMockOutputs(commands, o -> "33".equals(o.getNote()));

    helper.deleteBy("name", "name_31");
    helper.deleteBy("name", "name_32");
    // delete all except the third : name_33

    helper.resultListCheckWithMockOutputs("/api/commands", 1);
  }
}
