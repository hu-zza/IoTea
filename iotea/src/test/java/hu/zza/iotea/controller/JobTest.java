package hu.zza.iotea.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;

import hu.zza.iotea.model.*;
import org.junit.jupiter.api.Test;

class JobTest {
  private final Device deviceA = new Device(11, "u11", "n11", new DeviceAddress("192.168.0.1"), 10);
  private final Device deviceB = new Device(22, "u22", "n22", new DeviceAddress("192.168.0.2"), 20);
  private final Command commandA = new Command(111, "A", "TemplateA", "NoteA");
  private final Command commandB = new Command(222, "B", "TemplateB", "NoteB");

  @Test
  void equals() {
    assertEquals(new Job(11, "A", deviceA, commandA), new Job(11, "B", deviceB, commandB));
  }
}
