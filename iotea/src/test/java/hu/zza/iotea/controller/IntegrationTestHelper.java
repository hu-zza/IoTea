package hu.zza.iotea.controller;

import static org.assertj.core.api.Assertions.assertThat;

import hu.zza.iotea.model.Identifiable;
import hu.zza.iotea.model.util.mapping.InputMapper;
import hu.zza.iotea.model.util.mapping.OutputMapper;
import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.IntStream;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.test.web.reactive.server.EntityExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;

@AllArgsConstructor
public class IntegrationTestHelper<ID, TYPE, IN, OUT extends Identifiable> {
  private String group;
  private WebTestClient client;
  private JpaRepository<TYPE, ID> repository;
  private InputMapper<TYPE, IN> inMapper;
  private OutputMapper<TYPE, OUT> outMapper;

  private Class<OUT> outType;

  private Function<Integer, IN> dummyInputGenerator;
  @Getter private final List<OUT> mockOutputs = new ArrayList<>();
  @Getter private final List<Integer> IDs = new ArrayList<>();

  void cleanRepository() {
    repository.deleteAll();
  }

  List<IN> createDummyInputs(Integer start, Integer end) {
    return IntStream.rangeClosed(start, end).mapToObj(this::createDummyInput).toList();
  }

  IN createDummyInput(Integer i) {
    return dummyInputGenerator.apply(i);
  }

  void postInputs(List<IN> inputs) {
    inputs.forEach(input -> client.post().uri("/api/" + group).bodyValue(input).exchange());
  }

  void putInputs(List<IN> inputs, String idType, Function<IN, Object> idExtractor) {
    inputs.forEach(
        d ->
            client
                .put()
                .uri("/api/%s/%s/%s".formatted(group, idType, idExtractor.apply(d)))
                .bodyValue(d)
                .exchange());
  }

  void updateMockOutputs(List<IN> devices) {
    updateMockOutputs(devices, o -> true);
  }

  void updateMockOutputs(List<IN> inputs, Predicate<OUT> filter) {
    mockOutputs.clear();
    getOutputsFromInputs(inputs).stream().filter(filter).forEachOrdered(mockOutputs::add);
  }

  List<OUT> getOutputsFromInputs(List<IN> inputs) {
    return outMapper.toDto(inMapper.toEntity(inputs));
  }

  void resultListCheckWithMockOutputs(String uri, int size) {
    assertThat(
            client
                .get()
                .uri(uri)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(outType)
                .hasSize(size)
                .consumeWith(this::updateMockOutputsWithIDs)
                .returnResult()
                .getResponseBody())
        .containsExactlyInAnyOrderElementsOf(mockOutputs);
  }

  void updateMockOutputsWithIDs(EntityExchangeResult<List<OUT>> entityExchangeResult) {
    var body = entityExchangeResult.getResponseBody();

    IDs.clear();
    Objects.requireNonNull(body, "entityExchangeResult is null").stream()
        .map(OUT::getId)
        .sorted()
        .forEach(IDs::add);

    assertThat(IDs.size()).as("DB IDs.size() == mockOutputs.size()").isEqualTo(mockOutputs.size());
    IntStream.range(0, mockOutputs.size()).forEach(i -> mockOutputs.get(i).setId(IDs.get(i)));
  }

  void resultCheckWithMockOutputs(String uri) {
    client
        .get()
        .uri(uri)
        .exchange()
        .expectStatus()
        .isOk()
        .expectBody(outType)
        .consumeWith(this::updateSingleMockOutputWithID)
        .isEqualTo(mockOutputs.get(0));
  }

  void updateSingleMockOutputWithID(EntityExchangeResult<OUT> entityExchangeResult) {
    var body = entityExchangeResult.getResponseBody();

    IDs.clear();
    IDs.add(Objects.requireNonNull(body, "entityExchangeResult is null").getId());

    assertThat(IDs.size())
        .as("DB IDs.size() == mockOutputs.length")
        .isEqualTo(mockOutputs.size())
        .as("DB IDs.size() == 1  && mockOutputs.size() == 1")
        .isEqualTo(1);

    IntStream.range(0, mockOutputs.size()).forEach(i -> mockOutputs.get(i).setId(IDs.get(i)));
  }

  void updateIDsWithAllID() {
    client
        .get()
        .uri("/api/" + group)
        .exchange()
        .expectBodyList(outType)
        .consumeWith(
            list -> {
              IDs.clear();
              var body = list.getResponseBody();
              if (body!= null) body.stream().map(OUT::getId).sorted().forEach(IDs::add);
            });
  }

  void deleteBy(String part, Object identifier) {
    client.delete().uri("/api/%s/%s/%s".formatted(group, part, identifier)).exchange();
  }
}
